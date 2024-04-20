package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.RangeBookingRequest;
import ar.edu.itba.pod.grpc.counter.RangeInfo;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AssignCounterAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTER_COUNT = "counterCount";
    public static final String AIRLINE  = "airline";
    public static final String FLIGHTS = "flights";

    public AssignCounterAction() {
        super(List.of(SECTOR,COUNTER_COUNT,AIRLINE,FLIGHTS), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        CounterServiceGrpc.CounterServiceBlockingStub stub = CounterServiceGrpc.newBlockingStub(channel);
        try {
            RangeInfo rangeInfo = stub.bookRange(RangeBookingRequest.newBuilder()
                            .setSector(arguments.get(SECTOR))
                            .setAirline(arguments.get(AIRLINE))
                            .setCounterCount(Integer.parseInt(arguments.get(COUNTER_COUNT)))
                            .addAllFlights(Arrays.stream(arguments.get(FLIGHTS).split("\\|")).toList())
                            .build());
            if(rangeInfo.hasAssignedRange()){
                System.out.printf("%s counters (%d-%d) in Sector %s are now checking in passengers from %s %s flights\n",
                        arguments.get(COUNTER_COUNT),
                        rangeInfo.getAssignedRange().getStart(),rangeInfo.getAssignedRange().getEnd(),
                        arguments.get(SECTOR),
                        arguments.get(AIRLINE),arguments.get(FLIGHTS));
            }else{
                System.out.printf("%s counters in Sector %s is pending with %d other pending ahead\n",
                        arguments.get(COUNTER_COUNT),
                        arguments.get(SECTOR),
                        rangeInfo.getPendingCount());
            }
        }catch (StatusRuntimeException e){
            switch (getError(e)){
                case SECTOR_NOT_FOUND -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                case FLIGHT_ALREADY_USED -> System.out.printf("There were passengers added for one of flights %s but for other airline",arguments.get(FLIGHTS));
                case EMPTY_PASSENGERS -> System.out.printf("There are no passengers for flights %s\n",arguments.get(FLIGHTS));
                case FLIGHT_ALREADY_ASSIGNED -> System.out.printf("There is already a counter assignment for one of the flights %s\n",arguments.get(FLIGHTS));
                case PENDING_REQUEST_FOR_FLIGHT -> System.out.printf("There is a pending assignment for one of the flights %s\n",arguments.get(FLIGHTS));
                case FLIGHT_ALREADY_CHECKED_IN -> System.out.printf("One of the flights %s has already ended the check in\n",arguments.get(FLIGHTS));
                default -> System.out.println("An unknown error occurred while assigning the counter");
            }
        }
    }
}
