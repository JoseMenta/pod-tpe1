package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.server.exceptions.FlightAlreadyAssignedException;
import ar.edu.itba.pod.server.exceptions.InvalidRangeException;
import ar.edu.itba.pod.server.exceptions.SectorNotFoundException;
import ar.edu.itba.pod.server.exceptions.FlightAssignedToOtherAirlineException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class AirportServiceImpl implements AirportService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AirportServiceImpl.class);

    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final RangeRepository rangeRepository;
    private final SectorRepository sectorRepository;
    private final HistoryCheckIn historyCheckIn;

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
        return range;
    }

    @Override
    public void addBooking(String booking, String flight, String airline) {
        final Airline airline1 = airlineRepository.createAirlineIfAbsent(airline);
        final Flight flight1 = flightRepository.createFlightIfAbsent(flight,airline1);
        passengerRepository.createPassenger(booking,airline1,flight1);
    }

    @Override
    public List<Sector> listSectors() {
        List<Sector> sectors =   sectorRepository.getSectors();
        if (sectors.isEmpty()) {
            throw new NoSectorsInAirportException();
        }
        return sectors;
    }

    @Override
    public List<Range> listCounters(String sector, int from, int to) {
        if(to-from<=-1){
            throw new InvalidRangeException();
        }
        final Sector sector1 = sectorRepository.getSectorById(sector).orElseThrow(SectorNotFoundException::new);
        return sector1.getRangesInInterval(from,to);
    }

    @Override
    public Pair<Optional<Range>, Integer> assignRange(String sector, String airline, List<String> flights, int count) {
        Optional<Sector> sectorOptional = sectorRepository.getSectorById(sector);
        // No existe un sector con ese nombre
        if(sectorOptional.isEmpty()){
            throw new SectorNotFoundException();
        }
        synchronized (sectorOptional.get()) {
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
                if (flightOptional.get().getStatus() == Flight.Status.ASIGNED) {
                    throw new FlightAlreadyAssignedException();
                }
                // Ya existe una solicitud pendiente de un rango de mostradores para al menos uno de los vuelos solicitados
                if (flightOptional.get().getStatus() == Flight.Status.WAITING) {
                    throw new FlightInPendingQueueException();
                }
                // Ya se asignó y luego se liberó un rango de mostradores para al menos uno de los vuelos solicitados
                if (flightOptional.get().getRange() != null) {
                    throw new FlightAlreadyAssignedException();
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
            return auxRange;
        }
    }

    @Override
    public Range freeCounters(String sectorName, int counterFrom, String airlineName) {
        LOGGER.info("Freeing counters from {} in sector {} by airline {}", counterFrom, sectorName, airlineName);
        Sector sector = sectorRepository.getSectorById(sectorName).orElseThrow(SectorNotFoundException::new);
        Airline airline = airlineRepository.getAirlineByName(airlineName).orElseThrow(AirlineNotFoundException::new);
        try {
            return sector.free(counterFrom, airline);
        } catch (Exception e) {
            LOGGER.error("Error freeing counters: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Pair<List<Passenger>, List<Counter>> checkInCounters(String sector, int counterFrom, String airline) {
        Airline airline1 = airlineRepository.getAirlineByName(airline).orElseThrow(AirlineNotInRangeException::new);
        return sectorRepository.getSectorById(sector).orElseThrow(SectorNotFoundException::new).checkInCounters(counterFrom,airline1);
    }

    @Override
    public List<RequestRange> listPendingAssignments(String sector) {
        return sectorRepository.getSectorById(sector).orElseThrow(SectorNotFoundException::new).getPendingRequests();
    }

    @Override
    public Flight fetchCounter(String booking) {
        return passengerRepository.getPassengerByBookingId(booking).orElseThrow(PassengerNotFoundException::new).getFlight();
    }

    @Override
    public Pair<Passenger, Integer> addPassengerToQueue(String booking, String sectorName, int startCounter) {
        final Passenger passenger = passengerRepository.getPassengerByBookingId(booking).orElseThrow(PassengerNotFoundException::new);
        final Sector sector = sectorRepository.getSectorById(sectorName).orElseThrow(SectorNotFoundException::new);
        final int waitingAhead = sector.addPassengerToQueue(passenger,startCounter);
        return new Pair<>(passenger, waitingAhead);
    }

    @Override
    public Passenger checkPassengerStatus(String booking) {
        LOGGER.info("Checking passenger status with booking id {}",booking);
        Passenger passenger = passengerRepository.getPassengerByBookingId(booking).orElseThrow(PassengerNotFoundException::new);
        Flight flight = passenger.getFlight();
        Range range = flight.getRange();
        if (range == null) {
            throw new RangeNotAssignedException();
        }
        return passenger;
    }

    @Override
    public BlockingQueue<Notification> register(String airline) {
        Airline airline1 = airlineRepository.getAirlineByName(airline).orElseThrow(AirlineNotFoundException::new);
        BlockingQueue<Notification> notifications = airline1.subscribe();
        airline1.log(new SubscriptionNotification(airline1));
        return notifications;
    }

    @Override
    public void unregister(String airline) throws InterruptedException {
        airlineRepository.getAirlineByName(airline).orElseThrow(AirlineNotFoundException::new).unsubscribe();
    }

    @Override
    public List<Range> checkCountersStatus(Optional<String> sector) {
        if(sector.isEmpty()){
            List<Range> auxList = new ArrayList<>();
            for(Sector sectorData : sectorRepository.getSectors()){
                auxList.addAll(sectorData.getRanges());
            }
            return auxList;
        }else{
            return sectorRepository.getSectorById(sector.get()).orElseThrow(SectorNotFoundException::new).getRanges();
        }
    }

    @Override
    public List<Passenger> queryCheckInHistory(Optional<String> sector, Optional<String> airline) {
        return this.historyCheckIn.getHistoryCheckIn(sector,airline);
    }
}
