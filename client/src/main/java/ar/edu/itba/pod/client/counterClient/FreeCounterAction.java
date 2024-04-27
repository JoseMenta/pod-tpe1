package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.FreeRangeRequest;
import ar.edu.itba.pod.grpc.counter.FreeRangeResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;

public class FreeCounterAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTER_FROM = "counterFrom";
    public static final String AIRLINE = "airline";


    public FreeCounterAction(){
        super(List.of(SECTOR,COUNTER_FROM,AIRLINE), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        CounterServiceGrpc.CounterServiceBlockingStub stub = CounterServiceGrpc.newBlockingStub(channel);
        try {
            FreeRangeResponse response = stub.freeCounterRange(FreeRangeRequest.newBuilder()
                    .setAirline(arguments.get(AIRLINE))
                    .setStart(Integer.parseInt(arguments.get(COUNTER_FROM)))
                    .setSector(arguments.get(SECTOR))
                    .build());
            System.out.printf("Ended check-in for flights %s on %d counters (%d-%d) in Sector %s\n",
                    String.join("|", response.getFlightsList()),
                    response.getRange().getEnd() - response.getRange().getStart()+1,
                    response.getRange().getStart(),response.getRange().getEnd(),
                    arguments.get(SECTOR));
        }catch (StatusRuntimeException e){
            switch (getError(e)){
                case SECTOR_NOT_FOUND -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                case INVALID_RANGE -> System.out.printf("Range starting at %s was not found in the sector %s\n",arguments.get(COUNTER_FROM),arguments.get(SECTOR));
                case RANGE_FROM_OTHER_AIRLINE -> System.out.printf("Range starting at %s is not from airline %s\n",arguments.get(COUNTER_FROM),arguments.get(AIRLINE));
                case PASSENGERS_WAITING -> System.out.println("There are passengers waiting in the range");
                default -> System.out.println("An unknown error occurred while freeing the counter");
            }
        }
    }
}
