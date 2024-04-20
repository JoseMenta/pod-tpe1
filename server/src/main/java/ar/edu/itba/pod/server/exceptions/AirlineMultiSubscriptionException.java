package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class AirlineMultiSubscriptionException extends AirlineException {

    public AirlineMultiSubscriptionException() {
        super(Error.AIRLINE_ALREADY_SUBSCRIBED);
    }
}
