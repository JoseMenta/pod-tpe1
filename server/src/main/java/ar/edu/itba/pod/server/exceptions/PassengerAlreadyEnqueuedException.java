package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassengerAlreadyEnqueuedException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerAlreadyEnqueuedException.class);

    public PassengerAlreadyEnqueuedException() {
        super("11");
        LOGGER.error("Passenger already enqueued");
    }
}
