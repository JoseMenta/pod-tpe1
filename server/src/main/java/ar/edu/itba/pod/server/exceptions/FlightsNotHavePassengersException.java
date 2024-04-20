package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class FlightsNotHavePassengersException extends AirlineException {

    public FlightsNotHavePassengersException(){
        super(Error.EMPTY_PASSENGERS);
    }
}
