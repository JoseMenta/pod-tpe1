package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RangeNotAssignedException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(RangeNotAssignedException.class);

    public RangeNotAssignedException() {
        super("15");
        LOGGER.error("RangeNotAssignedException", this);
    }
}
