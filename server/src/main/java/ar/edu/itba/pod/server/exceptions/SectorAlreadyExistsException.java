package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectorAlreadyExistsException extends RuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(SectorAlreadyExistsException.class);

    public SectorAlreadyExistsException(){
        super("1");
        LOGGER.error("SectorAlreadyExistsException", this);
    }
}
