package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class AirlineNotInRangeException extends AirlineException {

    public AirlineNotInRangeException(){
        super(Error.RANGE_FROM_OTHER_AIRLINE);
    }
}
