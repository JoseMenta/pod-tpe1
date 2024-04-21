package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class InvalidRangeException extends AirlineException {


    public InvalidRangeException(){
        super(Error.INVALID_RANGE);
    }
}
