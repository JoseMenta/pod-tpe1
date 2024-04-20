package ar.edu.itba.pod.server.interfaces.repositories;

import ar.edu.itba.pod.server.models.Airline;

import java.util.Optional;

public interface AirlineRepository {

    Optional<Airline> getAirlineByName(final String airlineName);

    Airline createAirlineIfAbsent(final String name);
}
