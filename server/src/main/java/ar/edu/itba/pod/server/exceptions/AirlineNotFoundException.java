package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirlineNotFoundException extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(AirlineNotFoundException.class);

    public AirlineNotFoundException() {
        super("19");
        LOGGER.error("AirlineNotFoundException", this);
    }

}
