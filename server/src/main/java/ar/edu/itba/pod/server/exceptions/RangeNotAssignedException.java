package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class RangeNotAssignedException extends AirlineException {

    public RangeNotAssignedException() {
        super(Error.RANGE_NOT_ASSIGNED);
    }
}
