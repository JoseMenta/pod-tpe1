package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class PassengerNotFoundException extends AirlineException {


    public PassengerNotFoundException() {
        super(Error.PASSENGER_NOT_FOUND);
    }
}
