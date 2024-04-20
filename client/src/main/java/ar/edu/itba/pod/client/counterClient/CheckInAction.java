package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CheckInCountersRequest;
import ar.edu.itba.pod.grpc.counter.CheckInCountersResponse;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.Map;

public class CheckInAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTER_FROM = "counterFrom";
    public static final String AIRLINE = "airline";
    public CheckInAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        Map<String, String> arguments = parseArguments();
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
            switch (e.getMessage()){
                case "2" -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                case "3" -> System.out.printf("Range starting at %s was not found in the sector %s\n",arguments.get(COUNTER_FROM),arguments.get(SECTOR));
                case "10" -> System.out.printf("Range starting at %s is not from airline %s\n",arguments.get(COUNTER_FROM),arguments.get(AIRLINE));
                default -> System.out.println("An unknown error occurred while checking-in in the counter");
            }
        }
    }
}
