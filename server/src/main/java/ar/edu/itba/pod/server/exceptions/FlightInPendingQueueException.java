package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightInPendingQueueException extends RuntimeException{
    private static final Logger LOGGER = LoggerFactory.getLogger(FlightInPendingQueueException.class);

    public FlightInPendingQueueException(){
        super("8");
        LOGGER.error("FlightInPendingQueueException",this);
    }
}
