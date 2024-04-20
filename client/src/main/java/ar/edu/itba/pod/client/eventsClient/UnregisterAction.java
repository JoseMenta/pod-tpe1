package ar.edu.itba.pod.client.eventsClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.notification.NotificationServiceGrpc;
import ar.edu.itba.pod.grpc.notification.SubscriptionRequest;
import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.grpc.notification.UnRegisterRequest;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
            if (e.getStatus().getDescription().equals("16")) {
                System.out.printf("Airline %s was not registered\n", arguments.get(AIRLINE));
            } else {
                System.out.println("An unknown error occurred while getting the counters");
            }
        }
    }
}
