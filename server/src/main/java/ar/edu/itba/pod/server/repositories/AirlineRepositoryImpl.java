package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.AirlineAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.AirlineRepository;
import ar.edu.itba.pod.server.models.Airline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AirlineRepositoryImpl implements AirlineRepository {

    private final ConcurrentHashMap<String, Airline> airlines = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(AirlineRepositoryImpl.class);

    @Override
    public Optional<Airline> getAirlineByName(String airlineName) {
        LOGGER.info("Get Airline with name {}",airlineName);
        return Optional.ofNullable(airlines.get(airlineName));
    }

    @Override
    public Airline createAirline(String name) {
        Airline airline = new Airline(name);
        Airline possibleAirline =  airlines.putIfAbsent(name,airline);
        if (possibleAirline != null) {
            LOGGER.error("Airline with name {} already exists",name);
            throw new AirlineAlreadyExistsException();
        }
        LOGGER.info("Create Airline with name {}",name);
        return airline;
    }

    @Override
    public Airline createAirlineIfAbsent(String name) {
        LOGGER.info("Create airline ifAbsent with name {}",name);
        Airline airline = new Airline(name);
        return Optional.ofNullable(airlines.putIfAbsent(name,airline)).orElse(airline);
    }
}
