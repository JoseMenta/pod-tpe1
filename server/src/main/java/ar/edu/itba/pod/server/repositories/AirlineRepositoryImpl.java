package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.AirlineAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.AirlineRepository;
import ar.edu.itba.pod.server.models.Airline;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AirlineRepositoryImpl implements AirlineRepository {

    private final ConcurrentHashMap<String, Airline> airlines = new ConcurrentHashMap<>();


    @Override
    public Optional<Airline> getAirlineByName(String airlineName) {
        return Optional.ofNullable(airlines.get(airlineName));
    }

    @Override
    public Airline createAirline(String name) {
        Airline airline = new Airline();
        Airline possibleAirline =  airlines.putIfAbsent(name,airline);
        if (possibleAirline != null)
            throw new AirlineAlreadyExistsException();
        return airline;
    }

    @Override
    public Airline createAirlineIfAbsent(String name) {
        Airline airline = new Airline();
        return Optional.ofNullable(airlines.putIfAbsent(name,airline)).orElse(airline);
    }
}
