package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.server.models.Range;

public class FreeNonBookedRangeException extends RuntimeException {
    public FreeNonBookedRangeException(Range range) {
        super("Range " + range.toString() + " cannot be freed because it is not booked");
    }
}
