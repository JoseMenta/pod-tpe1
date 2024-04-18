package ar.edu.itba.pod.server.exceptions;

public class InvalidAirlineException extends RuntimeException {

    public InvalidAirlineException(final String airlineName) {
        super("The airline " + airlineName + " does not exist");
    }
}
