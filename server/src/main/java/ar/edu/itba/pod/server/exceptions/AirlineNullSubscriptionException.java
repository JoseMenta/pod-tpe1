package ar.edu.itba.pod.server.exceptions;

public class AirlineNullSubscriptionException extends RuntimeException {

    public AirlineNullSubscriptionException(String airlineName) {
        super("Airline " + airlineName + " has no subscriptor");
    }
}
