package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirlineNullSubscriptionException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirlineNullSubscriptionException.class);

    public AirlineNullSubscriptionException() {
        super("16");
        LOGGER.error("AirlineNullSubscriptionException", this);
    }
}
