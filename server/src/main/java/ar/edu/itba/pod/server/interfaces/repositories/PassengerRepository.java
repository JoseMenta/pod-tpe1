package ar.edu.itba.pod.server.interfaces.repositories;

import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Passenger;

import java.util.Optional;

public interface PassengerRepository {
    Optional<Passenger> getPassengerByBookingId(final String bookingId);

    Passenger createPassenger(final String bookingId, final Airline airline,final Flight flight);

}
