package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Range;

public class AirlineCannotFreeRangeException extends RuntimeException {

    public AirlineCannotFreeRangeException() {
        super("10");
    }
}
