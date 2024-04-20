package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.server.models.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreeNonBookedRangeException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeNonBookedRangeException.class);

    public FreeNonBookedRangeException() {
        super("10");
        LOGGER.error("FreeNonBookedRangeException",this);
    }
}
