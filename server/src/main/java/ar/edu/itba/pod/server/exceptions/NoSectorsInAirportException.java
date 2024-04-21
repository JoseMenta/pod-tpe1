package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class NoSectorsInAirportException extends AirlineException {
    public NoSectorsInAirportException() {
        super(Error.EMPTY_AIRPORT);
    }
}
