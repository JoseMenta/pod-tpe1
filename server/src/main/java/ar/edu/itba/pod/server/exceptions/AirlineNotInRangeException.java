package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirlineNotInRangeException extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(AirlineNotInRangeException.class);

    public AirlineNotInRangeException(){
        super("10");
        LOGGER.error("AirlineNotInRangeException", this);
    }
}
