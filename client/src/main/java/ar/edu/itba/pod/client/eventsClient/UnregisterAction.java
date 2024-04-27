package ar.edu.itba.pod.client.eventsClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.notification.NotificationServiceGrpc;
import ar.edu.itba.pod.grpc.notification.UnRegisterRequest;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;

public class UnregisterAction extends Action {
    public static final String AIRLINE = "airline";
    public UnregisterAction() {
        super(List.of(AIRLINE), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) {

        NotificationServiceGrpc.NotificationServiceBlockingStub stub =
                NotificationServiceGrpc.newBlockingStub(channel);
        try {
            stub.unsubscribeAirline(UnRegisterRequest.newBuilder().setAirline(arguments.get(AIRLINE)).build());
            System.out.printf("%s unregistered successfully for events\n", arguments.get(AIRLINE));
        }catch (StatusRuntimeException e){
            switch (getError(e)){
                case AIRLINE_NOT_REGISTERED, AIRLINE_NOT_FOUND -> System.out.printf("Airline %s was not registered\n", arguments.get(AIRLINE));
                default -> System.out.println("An unknown error occurred while getting the counters");
            }
        }
    }
}
