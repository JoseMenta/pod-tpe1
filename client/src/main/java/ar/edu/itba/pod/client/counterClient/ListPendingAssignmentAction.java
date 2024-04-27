package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.ListPendingRequest;
import ar.edu.itba.pod.grpc.counter.PendingRangeInfo;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ListPendingAssignmentAction extends Action {
    public static final String SECTOR = "sector";
    public ListPendingAssignmentAction() {
        super(List.of(SECTOR), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        CounterServiceGrpc.CounterServiceStub stub =
                CounterServiceGrpc.newStub(channel);
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<PendingRangeInfo> observer = new StreamObserver<PendingRangeInfo>() {
            boolean headerPrinted = false;
            @Override
            public void onNext(PendingRangeInfo value) {
                if(!headerPrinted){
                    System.out.printf("%-9s %-16s %-31s\n","Counters","Airline","Flights");
                    System.out.printf("%s\n","#".repeat(58));
                    headerPrinted = true;
                }
                System.out.printf("%d\t%s\t%s\n",value.getSize(),value.getAirline(),String.join("|",value.getFlightsList()));
            }

            @Override
            public void onError(Throwable t) {
                switch (getError(t)){
                    case SECTOR_NOT_FOUND -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                    default -> System.out.println("An unknown error occurred while listing pending assignments");
                }
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                if(!headerPrinted){
                    System.out.printf("%-9s %-16s %-31s\n","Counters","Airline","Flights");
                    System.out.printf("%s\n","#".repeat(58));
                    headerPrinted = true;
                }
                finishLatch.countDown();
            }
        };
        stub.listPendingAssignments(ListPendingRequest.newBuilder()
                .setSector(arguments.get(SECTOR))
                .build(),observer);
        finishLatch.await();
    }
}
