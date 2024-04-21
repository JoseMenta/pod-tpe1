package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.grpc.commons.Error;

public class EmptyHistoryCheckInException extends AirlineException {

    public EmptyHistoryCheckInException() {
        super(Error.HISTORY_CHECK_IN_NOT_FOUND);
    }
}
