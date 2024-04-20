package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class AirlineNotFoundException extends AirlineException {

    public AirlineNotFoundException() {
        super(Error.AIRLINE_NOT_FOUND);
    }

}
