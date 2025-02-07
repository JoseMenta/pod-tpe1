package ar.edu.itba.pod.server.models;

import lombok.Getter;

import java.util.Objects;

public class Flight {
    @Getter
    private final String code;
    private Range range;
    @Getter
    private Status status;
    public enum Status {NOT_ASSIGNED, WAITING, ASSIGNED,CHECKED_IN}
    @Getter
    private final Airline airline;

    public Flight(final String code,final Airline airline) {
        if(airline == null)
            throw new IllegalArgumentException("Airline must not be null");
        this.code =code;
        this.airline = airline;
        this.range= null;
        this.status= Status.NOT_ASSIGNED;
    }

    public synchronized void assignRange(Range range) {
        this.status = Status.ASSIGNED;
        this.range = range;
    }

    public synchronized void endCheckIn(){
        this.status = Status.CHECKED_IN;
    }

    public synchronized void waitingRange(){
        this.status = Status.WAITING;
    }

    public synchronized Range getRange() {
        return this.range;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Flight other){
            return other.code.equals(code);
        }
        return false;
    }
}
