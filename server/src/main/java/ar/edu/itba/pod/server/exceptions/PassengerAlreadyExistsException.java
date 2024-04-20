package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassengerAlreadyExistsException extends RuntimeException{
    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerAlreadyExistsException.class);

    public PassengerAlreadyExistsException(){
        super("1");
        LOGGER.error("PassengerAlreadyExistsException",this);
    }
}
