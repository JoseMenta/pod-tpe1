package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;

public class AddCounterAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTERS = "counters";

    public AddCounterAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) {
        Map<String, String> arguments = parseArguments();
    }
}
