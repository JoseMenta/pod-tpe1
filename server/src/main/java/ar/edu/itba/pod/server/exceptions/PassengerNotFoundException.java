package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassengerNotFoundException extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerNotFoundException.class);

    public PassengerNotFoundException() {
        super("12");
        LOGGER.error("PassengerNotFoundException", this);
    }
}
