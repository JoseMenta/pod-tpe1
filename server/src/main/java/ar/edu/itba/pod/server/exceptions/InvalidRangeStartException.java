package ar.edu.itba.pod.server.exceptions;

public class InvalidRangeStartException extends RuntimeException {

    public InvalidRangeStartException(final int start) {
        super("There is no range starting at " + start);
    }
}
