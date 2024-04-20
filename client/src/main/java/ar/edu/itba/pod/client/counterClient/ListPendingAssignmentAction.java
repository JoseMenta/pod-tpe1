package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.ListPendingRequest;
import ar.edu.itba.pod.grpc.counter.PendingRangeInfo;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class ListPendingAssignmentAction extends Action {
    public static final String SECTOR = "sector";
    public ListPendingAssignmentAction() {
        super(List.of(SECTOR), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        CounterServiceGrpc.CounterServiceStub stub =
                CounterServiceGrpc.newStub(channel);
        System.out.printf("%-9s %-16s %-31s\n","Counters","Airline","Flights");
        System.out.printf("%s\n","#".repeat(58));
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<PendingRangeInfo> observer = new StreamObserver<PendingRangeInfo>() {
            @Override
            public void onNext(PendingRangeInfo value) {
                System.out.printf("%-9d %-16s %-31s\n",value.getSize(),value.getAirline(),String.join("|",value.getFlightsList()));
            }

            @Override
            public void onError(Throwable t) {
                if(t instanceof StatusRuntimeException e) {
                    switch (e.getStatus().getDescription()){
                        case "2" -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                        default -> System.out.println("An unknown error occurred while listing pending assignments");
                    }
                }else{
                    System.out.println("An unknown error occurred while listing pending assignments");
                }
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };
        stub.listPendingAssignments(ListPendingRequest.newBuilder()
                .setSector(arguments.get(SECTOR))
                .build(),observer);
        finishLatch.await();
    }
}
