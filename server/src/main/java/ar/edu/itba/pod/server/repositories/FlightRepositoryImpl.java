package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.AirlineAlreadyExistsException;
import ar.edu.itba.pod.server.exceptions.FlightAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.FlightRepository;
import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Flight;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FlightRepositoryImpl implements FlightRepository {

    private final ConcurrentHashMap<String, Flight> flights = new ConcurrentHashMap<>();


    @Override
    public Optional<Flight> getFlightByFlightNumber(String flightNumber) {
        return Optional.ofNullable(flights.get(flightNumber));
    }

    @Override
    public Flight createFlight(final String flightNumber,final Airline airline) {
        Flight flight = new Flight(flightNumber,airline);
        Flight possibleFlight =  flights.putIfAbsent(flightNumber,flight);
        if (possibleFlight != null)
            throw new FlightAlreadyExistsException();
        return flight;
    }

    @Override
    public Flight createFlightIfAbsent(final String flightNumber,final Airline airline) {
        Flight flight = new Flight(flightNumber,airline);
        return Optional.ofNullable(flights.putIfAbsent(flightNumber,flight)).orElse(flight);
    }
}
