package ar.edu.itba.pod.server.exceptions;

public class AirlineNotFoundException extends RuntimeException{
    public AirlineNotFoundException(String airlineName) {
        super("Airline " + airlineName + " not found");
    }

}
