package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.server.exceptions.*;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.interfaces.repositories.*;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.models.*;
import ar.edu.itba.pod.server.models.Notifications.SubscriptionNotification;
import ar.edu.itba.pod.server.models.ds.Pair;
import ar.edu.itba.pod.server.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class AirportServiceImpl implements AirportService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AirportServiceImpl.class);

    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final RangeRepository rangeRepository;
    private final SectorRepository sectorRepository;
    private final HistoryCheckIn historyCheckIn;
    
    private final Object assignRangeLock = new Object();

    public AirportServiceImpl(){
        this.airlineRepository = new AirlineRepositoryImpl();
        this.flightRepository = new FlightRepositoryImpl();
        this.passengerRepository = new PassengerRepositoryImpl();
        this.rangeRepository = new RangeRepositoryImpl();
        this.sectorRepository = new SectorRepositoryImpl();
        this.historyCheckIn = new HistoryCheckIn();
    }

    @Override
    public void addSector(String sector) {
        LOGGER.debug("Adding sector {}", sector);
        sectorRepository.createSector(sector, historyCheckIn);
        LOGGER.info("Sector {} added", sector);
    }

    @Override
    public Range addCountersToSector(String sectorName, int amount) {
        LOGGER.debug("Adding {} counters to sector {}", amount, sectorName);
        if(amount <= 0) {
            throw new InvalidRangeException();
        }
        Sector sector = sectorRepository.getSectorById(sectorName).orElseThrow(SectorNotFoundException::new);
        Range range = rangeRepository.createRange(amount, sector);
        sector.addRange(range);
        LOGGER.info("Added {} counters to sector {}", amount, sectorName);
        return range;
    }

    @Override
    public void addBooking(String booking, String flight, String airline) {
        LOGGER.debug("Adding passenger with booking {} for flight {} of airline {}", booking, flight, airline);
        final Airline airline1 = airlineRepository.createAirlineIfAbsent(airline);
        final Flight flight1 = flightRepository.createFlightIfAbsent(flight,airline1);
        passengerRepository.createPassenger(booking,airline1,flight1);
        LOGGER.info("Passenger with booking {} for flight {} of airline {} added", booking, flight, airline);
    }

    @Override
    public List<Sector> listSectors() {
        LOGGER.debug("Listing sectors");
        List<Sector> sectors = sectorRepository.getSectors();
        if (sectors.isEmpty()) {
            throw new NoSectorsInAirportException();
        }
        LOGGER.info("Listed sectors");
        return sectors;
    }

    @Override
    public List<Range> listCounters(String sector, int from, int to) {
        LOGGER.debug("Listing counters from {} to {} in sector {}", from, to, sector);
        if(to-from<=-1){
            throw new InvalidRangeException();
        }
        final Sector sector1 = sectorRepository.getSectorById(sector).orElseThrow(SectorNotFoundException::new);
        LOGGER.info("Listed counters from {} to {} in sector {}", from, to, sector);
        return sector1.getRangesInInterval(from,to);
    }

    //Synchronization is general for all sectors and not only for one sector.
    //This is because using a sector for the checks if a flight is already assigned to a sector does not
    //prevent the flight for being assigned to another sector
    //Synchronization for all sectors prevent this case, because if a flight is being assigned to two sectors, only
    //one sector will be in the assignment method
    @Override
    public Pair<Optional<Range>, Integer> assignRange(String sector, String airline, List<String> flights, int count) {
        LOGGER.info("Assigning range of {} counters in sector {} for flights {} by airline {}", count, sector, flights, airline);
        Optional<Sector> sectorOptional = sectorRepository.getSectorById(sector);
        // No existe un sector con ese nombre
        if(sectorOptional.isEmpty()){
            throw new SectorNotFoundException();
        }
        synchronized (assignRangeLock) {
            List<Flight> flightList = new ArrayList<>();
            for (String flight : flights) {
                Optional<Flight> flightOptional = flightRepository.getFlightByFlightNumber(flight);
                // No se agregaron pasajeros esperados con el código de vuelo, para al menos un de los vuelos solicitados
                if (flightOptional.isEmpty()) {
                    throw new FlightNotFoundException();
                }
                // Se agregaron pasajeros esperados con el código de vuelo pero con otra aerolínea, para al menos uno de los vuelos solicitados
                if (!Objects.equals(flightOptional.get().getAirline().getName(), airline)) {
                    throw new FlightAssignedToOtherAirlineException();
                }
                // Ya existe al menos un mostrador asignado para al menos uno de los vuelos solicitados
                if (flightOptional.get().getStatus().equals(Flight.Status.ASSIGNED)) {
                    throw new FlightAlreadyAssignedException();
                }
                // Ya existe una solicitud pendiente de un rango de mostradores para al menos uno de los vuelos solicitados
                if (flightOptional.get().getStatus().equals(Flight.Status.WAITING)) {
                    throw new FlightInPendingQueueException();
                }
                // Ya se asignó y luego se liberó un rango de mostradores para al menos uno de los vuelos solicitados
                if (flightOptional.get().getStatus().equals(Flight.Status.CHECKED_IN)) {
                    throw new FlightAlreadyCheckedInException();
                }
                flightList.add(flightOptional.get());
            }
            if (flightList.isEmpty()) {
                throw new FlightsNotHavePassengersException();
            }
            Optional<Airline> airlineOptional = airlineRepository.getAirlineByName(airline);
            if (airlineOptional.isEmpty()) {
                throw new FlightAssignedToOtherAirlineException();
            }
            Pair<Optional<Range>, Integer> auxRange = sectorOptional.get().book(count, flightList, airlineOptional.get());
            for(Flight flight : flightList){
                if(auxRange.first().isPresent()){
                    flight.assignRange(auxRange.first().get());
                }
            }
            LOGGER.info("Assigned range of {} counters in sector {} for flights {} by airline {}", count, sector, flights, airline);
            return auxRange;
        }
    }

    @Override
    public Range freeCounters(String sectorName, int counterFrom, String airlineName) {
        LOGGER.debug("Freeing counters from {} in sector {} by airline {}", counterFrom, sectorName, airlineName);
        Sector sector = sectorRepository.getSectorById(sectorName).orElseThrow(SectorNotFoundException::new);
        Airline airline = airlineRepository.getAirlineByName(airlineName).orElseThrow(AirlineNotFoundException::new);
        try {
            LOGGER.info("Freed counters from {} in sector {} by airline {}", counterFrom, sectorName, airlineName);
            return sector.free(counterFrom, airline);
        } catch (Exception e) {
            LOGGER.error("Error freeing counters: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Pair<List<Passenger>, List<Counter>> checkInCounters(String sector, int counterFrom, String airline) {
        LOGGER.debug("Checking in counters from {} in sector {} by airline {}", counterFrom, sector, airline);
        Airline airline1 = airlineRepository.getAirlineByName(airline).orElseThrow(AirlineNotFoundException::new);
        Pair<List<Passenger>, List<Counter>> pair = sectorRepository.getSectorById(sector).orElseThrow(SectorNotFoundException::new).checkInCounters(counterFrom,airline1);
        LOGGER.info("Checked in counters from {} in sector {} by airline {}", counterFrom, sector, airline);
        return pair;
    }

    @Override
    public List<RequestRange> listPendingAssignments(String sector) {
        LOGGER.debug("Listing pending assignments in sector {}", sector);
        List<RequestRange> pendingRequests = sectorRepository.getSectorById(sector).orElseThrow(SectorNotFoundException::new).getPendingRequests();
        LOGGER.info("Listed pending assignments in sector {}", sector);
        return pendingRequests;
    }

    @Override
    public Flight fetchCounter(String booking) {
        LOGGER.debug("Fetching counter for passenger with booking id {}", booking);
        Flight flight = passengerRepository.getPassengerByBookingId(booking).orElseThrow(PassengerNotFoundException::new).getFlight();
        LOGGER.info("Fetched counter for passenger with booking id {}", booking);
        return flight;
    }

    @Override
    public Pair<Passenger, Integer> addPassengerToQueue(String booking, String sectorName, int startCounter) {
        LOGGER.debug("Adding passenger with booking id {} to queue in sector {} starting at counter {}", booking, sectorName, startCounter);
        final Passenger passenger = passengerRepository.getPassengerByBookingId(booking).orElseThrow(PassengerNotFoundException::new);
        final Sector sector = sectorRepository.getSectorById(sectorName).orElseThrow(SectorNotFoundException::new);
        final int waitingAhead = sector.addPassengerToQueue(passenger,startCounter);
        LOGGER.info("Added passenger with booking id {} to queue in sector {} starting at counter {}", booking, sectorName, startCounter);
        return new Pair<>(passenger, waitingAhead);
    }

    @Override
    public Passenger checkPassengerStatus(String booking) {
        LOGGER.debug("Checking passenger status with booking id {}",booking);
        Passenger passenger = passengerRepository.getPassengerByBookingId(booking).orElseThrow(PassengerNotFoundException::new);
        Flight flight = passenger.getFlight();
        Range range = flight.getRange();
        if (range == null) {
            throw new RangeNotAssignedException();
        }
        LOGGER.info("Checked passenger status with booking id {}",booking);
        return passenger;
    }

    @Override
    public BlockingQueue<Notification> register(String airline) {
        LOGGER.debug("Registering airline {} to receive notifications", airline);
        Airline airline1 = airlineRepository.getAirlineByName(airline).orElseThrow(AirlineNotFoundException::new);
        BlockingQueue<Notification> notifications = airline1.subscribe();
        airline1.log(new SubscriptionNotification(airline1));
        LOGGER.info("Registered airline {} to receive notifications", airline);
        return notifications;
    }

    @Override
    public void unregister(String airline) throws InterruptedException {
        LOGGER.debug("Unregistering airline {} from receiving notifications", airline);
        airlineRepository.getAirlineByName(airline).orElseThrow(AirlineNotFoundException::new).unsubscribe();
        LOGGER.info("Unregistered airline {} from receiving notifications", airline);
    }

    @Override
    public List<Range> checkCountersStatus(Optional<String> sector) {
        LOGGER.debug("Checking counters status {}", sector.map(s -> "in sector " + s).orElse("in all sectors"));
        if(sector.isEmpty()){
            List<Range> auxList = new ArrayList<>();
            for(Sector sectorData : sectorRepository.getSectors()){
                auxList.addAll(sectorData.getRanges());
            }
            if(auxList.isEmpty()){
                throw new RangeNotAssignedException();
            }
            LOGGER.info("Checked counters status for all sectors");
            return auxList;
        }else{
            List<Range> ranges = sectorRepository.getSectorById(sector.get()).map(Sector::getRanges).orElse(Collections.emptyList());
            LOGGER.info("Checked counters status in sector {}", sector.get());
            return ranges;
        }
    }

    @Override
    public List<Passenger> queryCheckInHistory(Optional<String> sector, Optional<String> airline) {
        LOGGER.debug("Querying check-in history {} {}", sector.map(s -> "in sector " + s).orElse("in all sectors"), airline.map(a -> "for airline " + a).orElse("for all airlines"));
        List<Passenger> passengers = this.historyCheckIn.getHistoryCheckIn(sector,airline);
        LOGGER.info("Queried check-in history {} {}", sector.map(s -> "in sector " + s).orElse("in all sectors"), airline.map(a -> "for airline " + a).orElse("for all airlines"));
        return passengers;
    }
}
