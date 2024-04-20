package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CheckInCountersRequest;
import ar.edu.itba.pod.grpc.counter.CheckInCountersResponse;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CheckInAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTER_FROM = "counterFrom";
    public static final String AIRLINE = "airline";
    public CheckInAction() {
        super(List.of(SECTOR,COUNTER_FROM,AIRLINE), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        CounterServiceGrpc.CounterServiceBlockingStub stub = CounterServiceGrpc.newBlockingStub(channel);
        try{
            CheckInCountersResponse response = stub.checkInCounters(CheckInCountersRequest.newBuilder()
                    .setSector(arguments.get(SECTOR))
                    .setCountFrom(Integer.parseInt(arguments.get(COUNTER_FROM)))
                    .setAirline(arguments.get(AIRLINE))
                    .build());
            response.getCheckInCountersList().forEach(c -> System.out.printf("Check-in successful of %s for flight %s at counter %d\n",c.getBooking(),c.getFlight(),c.getCounter()));
            response.getEmptyCountersList().forEach(c -> System.out.printf("Counter %d is idle\n",c));
        }catch (StatusRuntimeException e){
            switch (getError(e)){
                case SECTOR_NOT_FOUND -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                case INVALID_RANGE -> System.out.printf("Range starting at %s was not found in the sector %s\n",arguments.get(COUNTER_FROM),arguments.get(SECTOR));
                case RANGE_FROM_OTHER_AIRLINE -> System.out.printf("Range starting at %s is not from airline %s\n",arguments.get(COUNTER_FROM),arguments.get(AIRLINE));
                default -> System.out.println("An unknown error occurred while checking-in in the counter");
            }
        }
    }
}
