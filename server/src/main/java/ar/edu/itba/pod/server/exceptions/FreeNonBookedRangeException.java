package ar.edu.itba.pod.server.exceptions;


import ar.edu.itba.pod.grpc.commons.Error;

public class FreeNonBookedRangeException extends AirlineException {

    public FreeNonBookedRangeException() {
        super(Error.RANGE_FROM_OTHER_AIRLINE);
    }
}
