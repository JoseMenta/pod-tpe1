package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Range;

public class AirlineCannotFreeRangeException extends RuntimeException {

    public AirlineCannotFreeRangeException(final Range range, final Airline airline) {
        super("The airline " + airline.getName() + " cannot free the range " + range.toString());
    }
}
