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

import java.util.ArrayList;
import java.util.List;
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
        sectorRepository.createSector(sector, historyCheckIn);
    }

    @Override
    public Range addCountersToSector(String sectorName, int amount) {
        if(amount <= 0) {
            InvalidRangeException e = new InvalidRangeException();
            LOGGER.error("Amount must be greater than 0: {}", amount, e);
            throw e;
        }
        Optional<Sector> maybeSector = sectorRepository.getSectorById(sectorName);
        if(maybeSector.isEmpty()) {
            InvalidSectorException e = new InvalidSectorException();
            LOGGER.error("Sector not found: {}", sectorName, e);
            throw e;
        }
        Sector sector = maybeSector.get();
        Range range = rangeRepository.createRange(amount, sector);
        sector.addRange(range);
        return range;
    }

    @Override
    public void addBooking(String booking, String flight, String airline) {
        final Airline airline1 = airlineRepository.createAirlineIfAbsent(airline);
        final Optional<Flight> flightOptional = flightRepository.getFlightByFlightNumber(flight);
        if (flightOptional.isPresent() && !airline1.equals(flightOptional.get().getAirline())){
            throw new FlightAssignedToOtherAirlineException();
        }
        final Flight flight1 = flightRepository.createFlightIfAbsent(flight,airline1);
        passengerRepository.createPassenger(booking,airline1,flight1);
    }

    @Override
    public List<Sector> listSectors() {
        return new ArrayList<>(sectorRepository.getSectors().values());
    }

    @Override
    public List<Range> listCounters(String sector, int from, int to) {
        if(to-from<=-1){
            throw new InvalidRangeException();
        }
        final Sector sector1 = sectorRepository.getSectorById(sector).orElseThrow(InvalidSectorException::new);
        return sector1.getRangesInInterval(from,to);
    }

    @Override
    public Pair<Range, Integer> assignRange(String sector, String airline, List<String> flights, int count) {
        return null;
    }

    @Override
    public void freeCounters(String sectorName, int counterFrom, String airlineName) {
        Optional<Sector> maybeSector = sectorRepository.getSectorById(sectorName);
        if(maybeSector.isEmpty()) {
            InvalidSectorException e = new InvalidSectorException();
            LOGGER.error("Sector not found: {}", sectorName, e);
            throw e;
        }
        Optional<Airline> maybeAirline = airlineRepository.getAirlineByName(airlineName);
        if(maybeAirline.isEmpty()) {
            InvalidSectorException e = new InvalidSectorException();
            LOGGER.error("Airline not found: {}", airlineName, e);
            throw e;
        }
        Sector sector = maybeSector.get();
        Airline airline = maybeAirline.get();
        try {
            sector.free(counterFrom, airline);
        } catch (Exception e) {
            LOGGER.error("Error freeing counters: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Pair<List<Passenger>, List<Counter>> checkInCounters(String sector, int counterFrom, String airline) {
        Airline airline1 = airlineRepository.getAirlineByName(airline).orElseThrow(AirlineNotInRangeException::new);
        return sectorRepository.getSectorById(sector).orElseThrow(InvalidSectorException::new).checkInCounters(counterFrom,airline1);
    }

    @Override
    public List<RequestRange> listPendingAssignments(String sector) {
        return null;
    }

    // TODO: Preguntar si la lista tiene que obtener el valor de la cantidad de gente antes o es li mismo.
    @Override
    public Flight fetchCounter(String booking) {
        return passengerRepository.getPassengerByBookingId(booking).orElseThrow(InvalidPassengerException::new).getFlight();
    }

    @Override
    public Range addPassengerToQueue(String booking, String sector, int startCounter) {
        final Passenger passenger = passengerRepository.getPassengerByBookingId(booking).orElseThrow(InvalidPassengerException::new);
        final Sector sector1 = sectorRepository.getSectorById(sector).orElseThrow(InvalidSectorException::new);
        return sector1.addPassengerToQueue(passenger,startCounter);
    }

    @Override
    public Passenger checkPassengerStatus(String booking) {
        return null;
    }

    @Override
    public BlockingQueue<Notification> register(String airline) {
        Airline airline1 = airlineRepository.getAirlineByName(airline).orElseThrow(() ->new AirlineNotFoundException(airline));
        BlockingQueue<Notification> notifications = airline1.subscribe();
        airline1.log(new SubscriptionNotification(airline1));
        return notifications;
    }

    @Override
    public void unregister(String airline) {
        airlineRepository.getAirlineByName(airline).orElseThrow(() ->new AirlineNotFoundException(airline)).unsubscribe();
    }

    @Override
    public List<Range> checkCountersStatus(Optional<String> sector) {
        return null;
    }

    @Override
    public List<Passenger> queryCheckInHistory(String sector, String airline) {
        return this.historyCheckIn.getHistoryCheckIn(sector,airline);
    }
}
