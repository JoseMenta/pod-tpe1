package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;

public class CountersAction extends Action {
    public static final String OUTPATH = "outPath";
    public static final String SECTOR = "sector";

    public CountersAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) {
        Map<String, String> arguments = parseArguments();
    }
}
