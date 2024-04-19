package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidRangeException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidRangeException.class);

    public InvalidRangeException(){
        super("3");
        LOGGER.error("InvalidRangeException", this);
    }
}
