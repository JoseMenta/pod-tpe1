package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class FlightInPendingQueueException extends AirlineException {

    public FlightInPendingQueueException(){
        super(Error.PENDING_REQUEST_FOR_FLIGHT);
    }
}
