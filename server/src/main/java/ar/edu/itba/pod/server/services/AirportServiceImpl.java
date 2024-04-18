package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.grpc.admin.RangeRequest;
import ar.edu.itba.pod.server.exceptions.FlightAlreadyAssigned;
import ar.edu.itba.pod.server.exceptions.InvalidRangeException;
import ar.edu.itba.pod.server.exceptions.InvalidSectorException;
import ar.edu.itba.pod.server.exceptions.FlightAssignedToOtherAirlineException;
import ar.edu.itba.pod.server.interfaces.repositories.*;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.models.*;
import ar.edu.itba.pod.server.models.ds.Pair;
import ar.edu.itba.pod.server.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public List<Range> listCounters(String sector, int start, int end) {
        Optional<Sector> sectorOptional = sectorRepository.getSectorById(sector);
        // Preguntar a JOSE como recibir el rango o si tenemos que implementarlo
        return null;
    }

    @Override
    public Pair<Range, Integer> assignRange(String sector, String airline, List<String> flights, int count) {
        Optional<Sector> sectorOptional = sectorRepository.getSectorById(sector);
        // No existe un sector con ese nombre
        if(sectorOptional.isEmpty()){
            throw new InvalidSectorException();
        }

        List<Flight> flightList = new ArrayList<>();
        for(String flight : flights){
            Optional<Flight> flightOptional = flightRepository.getFlightByFlightNumber(flight);
            // No se agregaron pasajeros esperados con el código de vuelo, para al menos un de los vuelos solicitados
            if(flightOptional.isEmpty()){
                throw new FlightAssignedToOtherAirlineException();
            }
            // Se agregaron pasajeros esperados con el código de vuelo pero con otra aerolínea, para al menos uno de los vuelos solicitados
            if(!Objects.equals(flightOptional.get().getAirline().getName(), airline)){
                throw new FlightAssignedToOtherAirlineException();
            }
            // Ya existe al menos un mostrador asignado para al menos uno de los vuelos solicitados
            // Ya existe una solicitud pendiente de un rango de mostradores para al menos uno de los vuelos solicitados
            if(flightOptional.get().getStatus() != Flight.Status.NOTASIGNED){
                throw new FlightAlreadyAssigned();
            }
            // Ya se asignó y luego se liberó un rango de mostradores para al menos uno de los vuelos solicitados
            if(flightOptional.get().getRange() != null){
                throw new FlightAlreadyAssigned();
            }
            flightList.add(flightOptional.get());
        }

        Optional<Airline> airlineOptional = airlineRepository.getAirlineByName(airline);
        if(airlineOptional.isEmpty()){
            throw new FlightAssignedToOtherAirlineException();
        }
        Range range = sectorOptional.get().book(count, flightList, airlineOptional.get());
    }

    @Override
    public void freeCounters(String sector, int counterFrom, String airline) {

    }

    @Override
    public Pair<List<Passenger>, List<Counter>> checkInCounters(String sector, int counterFrom, String airline) {
        return null;
    }

    @Override
    public List<RequestRange> listPendingAssignments(String sector) {
        return null;
    }

    @Override
    public Flight fetchCounter(String booking) {
        return null;
    }

    @Override
    public Range addPassengerToQueue(String booking, String sector, int startCounter) {
        return null;
    }

    @Override
    public Passenger checkPassengerStatus(String booking) {
        return null;
    }

    @Override
    public void register(String airline) {

    }

    @Override
    public void unregister(String airline) {

    }

    @Override
    public List<Range> checkCountersStatus(Optional<String> sector) {
        return null;
    }

    @Override
    public List<Passenger> queryCheckInHistory(Optional<String> sector, Optional<String> airline) {
        return null;
    }
}
