package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirlineCannotFreeRangeException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirlineCannotFreeRangeException.class);

    public AirlineCannotFreeRangeException() {
        super("10");
        LOGGER.error("AirlineCannotFreeRangeException", this);
    }
}
