package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;

public class ManifestAction extends Action {
    public static final String INPATH = "inPath";
    public ManifestAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) {
        Map<String, String> arguments = parseArguments();
    }
}
