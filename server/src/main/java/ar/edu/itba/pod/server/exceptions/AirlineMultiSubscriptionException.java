package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirlineMultiSubscriptionException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirlineMultiSubscriptionException.class);

    public AirlineMultiSubscriptionException() {
        super("17");
        LOGGER.error("AirlineMultiSubscriptionException", this);
    }
}
