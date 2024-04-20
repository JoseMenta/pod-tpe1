package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.server.models.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RangeHasPassengersException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(RangeHasPassengersException.class);

    public RangeHasPassengersException() {
        super("11");
        LOGGER.error("RangeHasPassengersException",this);
    }
}
