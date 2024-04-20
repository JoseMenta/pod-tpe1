package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.RangeBookingRequest;
import ar.edu.itba.pod.grpc.counter.RangeInfo;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AssignCounterAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTER_COUNT = "counterCount";
    public static final String AIRLINE  = "airline";
    public static final String FLIGHTS = "flights";

    public AssignCounterAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        Map<String, String> arguments = parseArguments();
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
            switch (e.getMessage()){
                case "2" -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                case "4" -> System.out.printf("There were passengers added for one of flights %s but for other airline",arguments.get(FLIGHTS));
                case "6" -> System.out.printf("There are no passengers for flights %s\n",arguments.get(FLIGHTS));
                case "7" -> System.out.printf("There is already a counter assignment for one of the flights %s\n",arguments.get(FLIGHTS));
                case "8" -> System.out.printf("There is a pending assignment for one of the flights %s\n",arguments.get(FLIGHTS));
                case "9" -> System.out.printf("One of the flights %s has already ended the check in\n",arguments.get(FLIGHTS));
                default -> System.out.println("An unknown error occurred while assigning the counter");
            }
        }
    }
}
