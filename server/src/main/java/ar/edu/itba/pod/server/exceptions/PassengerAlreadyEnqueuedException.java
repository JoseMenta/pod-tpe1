package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class PassengerAlreadyEnqueuedException extends AirlineException {


    public PassengerAlreadyEnqueuedException() {
        super(Error.PASSENGERS_WAITING);
    }
}
