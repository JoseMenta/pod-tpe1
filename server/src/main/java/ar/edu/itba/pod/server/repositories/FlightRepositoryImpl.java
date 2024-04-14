package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.FlightAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.FlightRepository;
import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FlightRepositoryImpl implements FlightRepository {

    private final ConcurrentHashMap<String, Flight> flights = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(FlightRepositoryImpl.class);


    @Override
    public Optional<Flight> getFlightByFlightNumber(String flightNumber) {
        LOGGER.info("Get flight with flight number {}",flightNumber);
        return Optional.ofNullable(flights.get(flightNumber));
    }

    @Override
    public Flight createFlight(final String flightNumber,final Airline airline) {
        Flight flight = new Flight(flightNumber,airline);
        Flight possibleFlight =  flights.putIfAbsent(flightNumber,flight);
        if (possibleFlight != null) {
            LOGGER.error("Flight with flight number {} already exists",flightNumber);
            throw new FlightAlreadyExistsException();
        }
        LOGGER.info("Create flight with flight number {}",flightNumber);
        return flight;
    }

    @Override
    public Flight createFlightIfAbsent(final String flightNumber,final Airline airline) {
        LOGGER.info("Create flight ifAbsent with flight number {}",flightNumber);
        Flight flight = new Flight(flightNumber,airline);
        return Optional.ofNullable(flights.putIfAbsent(flightNumber,flight)).orElse(flight);
    }
}
