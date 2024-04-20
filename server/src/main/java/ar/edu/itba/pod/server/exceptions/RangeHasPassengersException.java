package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class RangeHasPassengersException extends AirlineException {

    public RangeHasPassengersException() {
        super(Error.PASSENGERS_WAITING);
    }
}
