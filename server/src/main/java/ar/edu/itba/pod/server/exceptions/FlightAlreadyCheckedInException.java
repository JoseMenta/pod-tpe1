package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class FlightAlreadyCheckedInException extends AirlineException {
    public FlightAlreadyCheckedInException() {
        super(Error.FLIGHT_ALREADY_CHECKED_IN);
    }
}
