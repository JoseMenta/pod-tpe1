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
//    -DserverAddress=localhost:50051 -Daction=listSectors

    public ListSectorsAction() {
        super(Collections.emptyList(),Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        //TODO: check if a BlockingStub should be used
        CounterServiceGrpc.CounterServiceStub stub =
                CounterServiceGrpc.newStub(channel);
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<SectorResponse> observer = new StreamObserver<SectorResponse>() {
            boolean first = true;
            @Override
            public void onNext(SectorResponse value) {
                if (first){
                    System.out.printf("%-9s %-9s\n","Sectors","Counters");
                    System.out.printf("%s\n","#".repeat(19));
                    first = false;
                }
                System.out.printf("%-9s %s\n",value.getName(),value.getRangesCount()==0?"-":value.getRangesList()
                                                        .stream().map(r->String.format("(%d-%d)",r.getStart(),r.getEnd()))
                                                        .collect(Collectors.joining()));
            }
            @Override
            public void onError(Throwable t) {
                switch (getError(t)){
                    case EMPTY_AIRPORT -> System.out.println("There are no sectors in the airport");
                    default -> System.out.println("An unknown error occurred while getting the sectors");
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
