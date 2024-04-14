package ar.edu.itba.pod.server.interfaces.repositories;

import ar.edu.itba.pod.server.models.Flight;

import java.util.Optional;

public interface FlightRepository {

    Optional<Flight> getFlightByFlightNumber(final String flightNumber);

    Flight createFlight(final String flightNumber);

    Flight createFlightIfAbsent(final String flightNumber);
}
