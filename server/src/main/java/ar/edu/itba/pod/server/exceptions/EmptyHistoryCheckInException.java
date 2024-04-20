package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyHistoryCheckInException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyHistoryCheckInException.class);

    public EmptyHistoryCheckInException() {
        super("20");
        LOGGER.error("EmptyHistoryCheckInException", this);
    }
}
