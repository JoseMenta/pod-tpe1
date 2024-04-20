package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AirlineException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirlineException.class);

    public AirlineException(Error error) {
        super(String.valueOf(error.getNumber()));
        LOGGER.error(this.getClass().getSimpleName(), this);
    }
}
