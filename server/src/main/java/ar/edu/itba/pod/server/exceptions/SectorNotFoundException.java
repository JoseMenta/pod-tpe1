package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class SectorNotFoundException extends AirlineException {


    public SectorNotFoundException(){
        super(Error.SECTOR_NOT_FOUND);
    }
}
