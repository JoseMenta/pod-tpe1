package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import io.grpc.ManagedChannel;

import java.util.List;

public class AssignCounterAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTERCOUNT = "counterCount";
    public static final String AIRLINE  = "airline";
    public static final String FLIGHTS = "flights";

    public AssignCounterAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {

    }
}
