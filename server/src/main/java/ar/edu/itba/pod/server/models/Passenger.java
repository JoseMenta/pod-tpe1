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
    private PassengerStatus passengerStatus;
    private static final int BOOKING_LENGTH = 6;

    public Passenger(Airline airline, String booking, Flight flight) {
        if(airline == null || booking == null || flight == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }
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
        this.passengerStatus = PassengerStatus.NONE;
    }

    public synchronized void checkIn(Counter counter) {
        this.counter = counter;
        this.passengerStatus = PassengerStatus.CHECKED;
    }

    public synchronized void enqueue() {
        this.passengerStatus = PassengerStatus.WAITING;
    }

    public synchronized PassengerStatus getPassengerStatus() {
        return this.passengerStatus;
    }

    public synchronized Counter getCounter() {
        return this.counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(booking, passenger.booking);
    }

}
