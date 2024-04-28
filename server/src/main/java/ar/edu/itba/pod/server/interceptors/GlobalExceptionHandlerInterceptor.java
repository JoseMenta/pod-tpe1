package ar.edu.itba.pod.server.interceptors;

import ar.edu.itba.pod.server.exceptions.*;
import com.google.rpc.Code;
import io.grpc.*;
import io.grpc.protobuf.StatusProto;

import java.util.HashMap;
import java.util.Map;


public class GlobalExceptionHandlerInterceptor implements ServerInterceptor {

    private final Map<Class<? extends Throwable>, Code> errorCodesByException;

    public GlobalExceptionHandlerInterceptor() {
        errorCodesByException = new HashMap<>();
        errorCodesByException.put(SectorAlreadyExistsException.class, Code.ALREADY_EXISTS);
        errorCodesByException.put(InvalidRangeException.class, Code.INVALID_ARGUMENT);
        errorCodesByException.put(SectorNotFoundException.class,Code.NOT_FOUND);
        errorCodesByException.put(PassengerAlreadyExistsException.class, Code.ALREADY_EXISTS);
        errorCodesByException.put(FlightAssignedToOtherAirlineException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(PassengerNotFoundException.class, Code.NOT_FOUND);
        errorCodesByException.put(AirlineNotFoundException.class, Code.NOT_FOUND);
        errorCodesByException.put(PassengerAlreadyEnqueuedException.class, Code.ALREADY_EXISTS);
        errorCodesByException.put(FlightNotInRangeException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(AirlineNotInRangeException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(RangeNotAssignedException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(AirlineCannotFreeRangeException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(AirlineMultiSubscriptionException.class, Code.ALREADY_EXISTS);
        errorCodesByException.put(AirlineNullSubscriptionException.class, Code.NOT_FOUND);
        errorCodesByException.put(EmptyHistoryCheckInException.class, Code.NOT_FOUND);
        errorCodesByException.put(FlightAlreadyAssignedException.class, Code.ALREADY_EXISTS);
        errorCodesByException.put(FlightInPendingQueueException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(FlightsNotHavePassengersException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(FreeNonBookedRangeException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(InvalidRangeStartException.class, Code.INVALID_ARGUMENT);
        errorCodesByException.put(RangeHasPassengersException.class, Code.FAILED_PRECONDITION);
        errorCodesByException.put(FlightNotFoundException.class, Code.NOT_FOUND);
        errorCodesByException.put(NoSectorsInAirportException.class, Code.NOT_FOUND);
        errorCodesByException.put(PassengerAlreadyCheckedInException.class, Code.ALREADY_EXISTS);
        errorCodesByException.put(FlightAlreadyCheckedInException.class,Code.ALREADY_EXISTS);
    }

    @Override
    public <T, R> ServerCall.Listener<T> interceptCall(
            ServerCall<T, R> serverCall, Metadata headers, ServerCallHandler<T, R> serverCallHandler) {
        ServerCall.Listener<T> delegate = serverCallHandler.startCall(serverCall, headers);
        return new ExceptionHandler<>(delegate, serverCall, headers);
    }

    private class ExceptionHandler<T, R> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<T> {

        private final ServerCall<T, R> delegate;
        private final Metadata headers;

        ExceptionHandler(ServerCall.Listener<T> listener, ServerCall<T, R> serverCall, Metadata headers) {
            super(listener);
            this.delegate = serverCall;
            this.headers = headers;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (RuntimeException ex) {
                handleException(ex, delegate, headers);
            }
        }



        private void handleException(RuntimeException exception, ServerCall<T, R> serverCall, Metadata headers) {
            Throwable error = exception;
            if (!errorCodesByException.containsKey(error.getClass())) {
                // Si la excepción vino "wrappeada" entonces necesitamos preguntar por la causa.
                error = error.getCause();
                if (error == null || !errorCodesByException.containsKey(error.getClass())) {
                    // Una excepción NO esperada.
                    serverCall.close(Status.UNKNOWN, headers);
                    return;
                }
            }
            // Una excepción esperada.
            com.google.rpc.Status rpcStatus = com.google.rpc.Status.newBuilder()
                    .setCode(errorCodesByException.get(error.getClass()).getNumber())
                    .setMessage(error.getMessage())

                    .build();
            StatusRuntimeException statusRuntimeException = StatusProto.toStatusRuntimeException(rpcStatus);
            Status newStatus = Status.fromThrowable(statusRuntimeException);
            serverCall.close(newStatus, headers);
        }
    }

}