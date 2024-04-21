package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class FlightNotFoundException extends AirlineException {

    public FlightNotFoundException() {
        super(Error.FLIGHT_NOT_FOUND);
    }
}
