package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.server.models.Range;

public class FreeNonBookedRangeException extends RuntimeException {
    public FreeNonBookedRangeException() {
        super("10");
    }
}
