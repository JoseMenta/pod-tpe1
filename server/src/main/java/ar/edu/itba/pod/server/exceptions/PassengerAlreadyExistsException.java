package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class PassengerAlreadyExistsException extends AirlineException {

    public PassengerAlreadyExistsException(){
        super(Error.ALREADY_EXISTS);
    }
}
