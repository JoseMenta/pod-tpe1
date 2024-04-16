package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;

public class PassengerStatusAction extends Action {
    public static final String BOOKING = "booking";
    public PassengerStatusAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        Map<String, String> arguments = parseArguments();
    }
}
