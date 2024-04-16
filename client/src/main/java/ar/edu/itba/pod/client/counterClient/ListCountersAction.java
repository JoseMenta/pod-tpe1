package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;

public class ListCountersAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTERFROM = "counterFrom";

    public static final String COUNTERTO = "counterTo";
    public ListCountersAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {

    }
}
