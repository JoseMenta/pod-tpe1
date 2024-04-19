package ar.edu.itba.pod.server.exceptions;

import org.slf4j.Logger;

public class SectorNotFoundException extends RuntimeException {

    private final static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SectorNotFoundException.class);

    public SectorNotFoundException(){
        super("2");
        LOGGER.error("SectotNotFoundException", this);
    }
}
