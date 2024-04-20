package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.FlightAssignedToOtherAirlineException;
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
    public synchronized Flight createFlightIfAbsent(final String flightNumber,final Airline airline) {
        LOGGER.info("Create flight ifAbsent with flight number {}",flightNumber);
        final Optional<Flight> flightOptional = Optional.ofNullable(flights.get(flightNumber));
        if (flightOptional.isPresent() && !airline.equals(flightOptional.get().getAirline())){
            throw new FlightAssignedToOtherAirlineException();
        }
        Flight flight = new Flight(flightNumber,airline);
        return Optional.ofNullable(flights.putIfAbsent(flightNumber,flight)).orElse(flight);
    }
}
