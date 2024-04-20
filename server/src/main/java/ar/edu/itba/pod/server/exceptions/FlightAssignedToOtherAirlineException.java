package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightAssignedToOtherAirlineException extends RuntimeException{
    private static final Logger LOGGER = LoggerFactory.getLogger(FlightAssignedToOtherAirlineException.class);

    public FlightAssignedToOtherAirlineException(){
        super("4");
        LOGGER.error("FlightAssignedToOtherAirlineException", this);
    }
}
