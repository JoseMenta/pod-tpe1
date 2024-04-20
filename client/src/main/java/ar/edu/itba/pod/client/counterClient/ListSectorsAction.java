package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.admin.AdminServiceGrpc;
import ar.edu.itba.pod.grpc.checkin.PassengerResponse;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.SectorResponse;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ListSectorsAction extends Action {

    public ListSectorsAction() {
        super(Collections.emptyList(),Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        //TODO: check if a BlockingStub should be used
        CounterServiceGrpc.CounterServiceStub stub =
                CounterServiceGrpc.newStub(channel);
        System.out.printf("%-9s %-9s\n","Sectors","Counters");
        System.out.printf("%s\n","#".repeat(19));
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<SectorResponse> observer = new StreamObserver<SectorResponse>() {
            @Override
            public void onNext(SectorResponse value) {
                System.out.printf("%-9s %s",value.getName(),value.getRangesList()
                                                        .stream().map(r->String.format("(%d-%d)",r.getStart(),r.getEnd()))
                                                        .collect(Collectors.joining()));
            }
            @Override
            public void onError(Throwable t) {
                if(t instanceof StatusRuntimeException e){
                    switch (e.getStatus().getDescription()){
                        case "5" -> System.out.println("There are no sectors in the airport");
                        default -> System.out.println("An unknown error occurred while getting the sectors");
                    }
                }else{
                    System.out.println("An unknown error occurred while getting the sectors");
                }
                finishLatch.countDown();

            }
            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };
        stub.listSectors(Empty.getDefaultInstance(),observer);
        finishLatch.await();
    }
}
