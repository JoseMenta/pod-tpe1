package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;

public class AddCounterAction extends Action {


    public AddCounterAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) {
        // TODO
    }
}
