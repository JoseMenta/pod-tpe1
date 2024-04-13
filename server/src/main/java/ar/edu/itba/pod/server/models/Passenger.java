package ar.edu.itba.pod.server.models;

import lombok.Getter;

import java.util.Objects;

public class Passenger {
    @Getter
    private final Airline airline;
    @Getter
    private final String booking;
    @Getter
    private final Flight flight;
    private Counter counter;
    private Status status;
    private static final int BOOKING_LENGTH = 6;

    public Passenger(Airline airline, String booking, Flight flight) {
        if (booking.length() != BOOKING_LENGTH) {
            throw new IllegalArgumentException("Length of code must be " + BOOKING_LENGTH);
        }
        if (!booking.matches("^[a-zA-Z0-9]*$")) {
            throw new IllegalArgumentException("Booking must be alphanumeric");
        }
        this.airline = airline;
        this.booking = booking;
        this.flight = flight;
        this.counter = null;
        this.status = Status.NONE;
    }

    public synchronized void setCounter(Counter counter) {
        this.counter = counter;
    }

    public synchronized void getCounter() {
        this.counter = null;
    }

    public synchronized void setStatus(Status status) {
        this.status = status;
    }

    public synchronized Status getStatus() {
        return this.status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(booking, passenger.booking);
    }

}
