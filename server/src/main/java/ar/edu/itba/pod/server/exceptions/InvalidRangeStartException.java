package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidRangeStartException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidRangeStartException.class);

    public InvalidRangeStartException() {
        super("3");
        LOGGER.error("InvalidRangeStartException",this);
    }
}
