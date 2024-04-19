package ar.edu.itba.pod.client.eventsClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.notification.NotificationServiceGrpc;
import ar.edu.itba.pod.grpc.notification.SubscriptionRequest;
import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.grpc.notification.UnRegisterRequest;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class UnregisterAction extends Action {
    public static final String AIRLINE = "airline";
    public UnregisterAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) {
        Map<String, String> arguments = parseArguments();

        NotificationServiceGrpc.NotificationServiceBlockingStub stub =
                NotificationServiceGrpc.newBlockingStub(channel);
        try {
            stub.unsubscribeAirline(UnRegisterRequest.newBuilder().setAirline(arguments.get(AIRLINE)).build());
            System.out.printf("%s unregistered successfully for events\n", arguments.get(AIRLINE));
        }catch (Exception e){
            if (e.getMessage().equals("16")) {
                System.out.printf("Airline %s was not registered\n", arguments.get(AIRLINE));
            } else {
                System.out.println("An unknown error occurred while getting the counters");
            }
        }
    }
}
