package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;

public class FetchCounterAction extends Action {

    public static final String BOOKING= "booking";
    public FetchCounterAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        Map<String, String> arguments = parseArguments();
    }
}
