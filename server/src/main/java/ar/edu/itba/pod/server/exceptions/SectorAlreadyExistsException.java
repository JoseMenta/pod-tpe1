package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class SectorAlreadyExistsException extends AirlineException {


    public SectorAlreadyExistsException(){
        super(Error.ALREADY_EXISTS);
    }
}
