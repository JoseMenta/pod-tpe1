package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Todo: Refactor name to add Exception suffix
public class FlightAlreadyAssigned extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightAlreadyAssigned.class);

    public FlightAlreadyAssigned(){
        super("7");
        LOGGER.error("FlightAlreadyAssigned", this);
    }
}
