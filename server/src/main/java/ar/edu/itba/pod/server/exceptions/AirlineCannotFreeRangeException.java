package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class AirlineCannotFreeRangeException extends AirlineException {

    public AirlineCannotFreeRangeException() {
        super(Error.RANGE_FROM_OTHER_AIRLINE);
    }
}
