package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class AirlineNullSubscriptionException extends AirlineException {

    public AirlineNullSubscriptionException() {
        super(Error.AIRLINE_NOT_REGISTERED);
    }
}
