package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.server.models.Range;

public class RangeHasPassengersException extends RuntimeException {

    public RangeHasPassengersException(Range range) {
        super("The range " + range.toString() + " still has passengers");
    }
}
