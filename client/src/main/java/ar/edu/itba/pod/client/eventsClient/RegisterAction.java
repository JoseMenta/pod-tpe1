package ar.edu.itba.pod.client.eventsClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;

public class RegisterAction extends Action {
    public static final String AIRLINE = "airline";

    public RegisterAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) {
        Map<String, String> arguments = parseArguments();
    }
}
