package ar.edu.itba.pod.server.exceptions;


import ar.edu.itba.pod.grpc.commons.Error;

public class FlightNotInRangeException extends AirlineException {


    public FlightNotInRangeException(){
        super(Error.FLIGHT_NOT_IN_RANGE);
    }
}
