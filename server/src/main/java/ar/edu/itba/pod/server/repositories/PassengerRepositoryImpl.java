package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.PassengerAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.PassengerRepository;
import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PassengerRepositoryImpl implements PassengerRepository {

    private final ConcurrentHashMap<String,Passenger> passengers = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRepositoryImpl.class);

    @Override
    public Optional<Passenger> getPassengerByBookingId(String bookingId) {
        LOGGER.info("Get passenger with booking id {}",bookingId);
        return Optional.ofNullable(passengers.get(bookingId));
    }

    @Override
    public Passenger createPassenger(String bookingId, Airline airline, Flight flight) {
        Passenger passenger = new Passenger(airline,bookingId,flight);
        Passenger possiblePassenger =  passengers.putIfAbsent(bookingId,passenger);
        if (possiblePassenger != null) {
            passengers.put(bookingId,possiblePassenger);//return to previous state
            LOGGER.error("Passenger with booking id {} already exists",bookingId);
            throw new PassengerAlreadyExistsException();
        }
        LOGGER.info("Create passenger with booking id {}",bookingId);
        return passenger;
    }

    @Override
    public Passenger createPassengerIfAbsent(String bookingId, Airline airline, Flight flight) {
        LOGGER.info("Create passenger ifAbsent with booking id {}",bookingId);
        Passenger passenger = new Passenger(airline,bookingId,flight);
        return Optional.ofNullable(passengers.putIfAbsent(bookingId,passenger)).orElse(passenger);
    }

}
