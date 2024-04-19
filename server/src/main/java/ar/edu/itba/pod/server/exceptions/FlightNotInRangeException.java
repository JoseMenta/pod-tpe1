package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightNotInRangeException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightNotInRangeException.class);

    public FlightNotInRangeException(){
        super("18");
        LOGGER.error("FlightNotInRangeException", this);
    }
}
