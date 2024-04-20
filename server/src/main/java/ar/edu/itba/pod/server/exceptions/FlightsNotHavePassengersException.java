package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightsNotHavePassengersException extends RuntimeException{
    private static final Logger LOGGER = LoggerFactory.getLogger(FlightsNotHavePassengersException.class);

    public FlightsNotHavePassengersException(){
        super("6");
        LOGGER.error("FlightsNotHavePassengersException",this);
    }
}
