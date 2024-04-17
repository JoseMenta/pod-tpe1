package ar.edu.itba.pod.server.models;

import lombok.Getter;

import java.util.Objects;

public class Flight {
    @Getter
    private final String code;
    private Range range;

    @Getter
    private final Airline airline;
    private static final int CODE_LENGTH = 5;

    public Flight(final String code,final Airline airline) {
        if (code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException("Length of code must be " + CODE_LENGTH);
        }
        if(airline == null)
            throw new IllegalArgumentException("Airline must not be null");
        this.code =code;
        this.airline = airline;
        this.range= null;
    }

    public synchronized void setRange(Range range) {
        this.range = range;
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
