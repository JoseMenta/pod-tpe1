package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.FlightAlreadyExistsException;
import ar.edu.itba.pod.server.exceptions.PassengerAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.PassengerRepository;
import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Passenger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PassengerRepositoryImpl implements PassengerRepository {

    private final ConcurrentHashMap<String,Passenger> passengers = new ConcurrentHashMap<>();


    @Override
    public Optional<Passenger> getPassengerByBookingId(String bookingId) {
        return Optional.ofNullable(passengers.get(bookingId));
    }

    @Override
    public Passenger createPassenger(String bookingId, Airline airline, Flight flight) {
        Passenger passenger = new Passenger();
        Passenger possiblePassenger =  passengers.putIfAbsent(bookingId,passenger);
        if (possiblePassenger != null)
            throw new PassengerAlreadyExistsException();
        return passenger;
    }

    @Override
    public Passenger createPassengerIfAbsent(String bookingId, Airline airline, Flight flight) {
        Passenger passenger = new Passenger();
        return Optional.ofNullable(passengers.putIfAbsent(bookingId,passenger)).orElse(passenger);
    }
}
