package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class FlightAssignedToOtherAirlineException extends AirlineException {
    public FlightAssignedToOtherAirlineException(){
        super(Error.FLIGHT_ALREADY_USED);
    }
}
