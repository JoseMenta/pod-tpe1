package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class FlightAlreadyAssignedException extends AirlineException {

    public FlightAlreadyAssignedException(){
        super(Error.FLIGHT_ALREADY_ASSIGNED);
    }
}
