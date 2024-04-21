package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class InvalidRangeStartException extends AirlineException {

    public InvalidRangeStartException() {
        super(Error.INVALID_RANGE);
    }
}
