package ar.edu.itba.pod.server.exceptions;

public class AirlineMultiSubscriptionException extends RuntimeException {

    public AirlineMultiSubscriptionException(String airlineName) {
        super("Airline " + airlineName + " has already a subscriptor");
    }
}
