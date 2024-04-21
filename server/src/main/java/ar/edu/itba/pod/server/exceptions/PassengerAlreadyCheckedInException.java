package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class PassengerAlreadyCheckedInException extends AirlineException{
    public PassengerAlreadyCheckedInException() {
        super(Error.PASSENGER_ALREADY_CHECKED_IN);
    }
}
