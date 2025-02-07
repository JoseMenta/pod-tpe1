package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.commons.RangeMessage;
import ar.edu.itba.pod.grpc.counter.CounterRequest;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.CountersResponse;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ListCountersAction extends Action {

    public static final String SECTOR = "sector";
    public static final String COUNTER_FROM = "counterFrom";
    public static final String COUNTER_TO = "counterTo";
    public ListCountersAction() {
        super(List.of(SECTOR,COUNTER_FROM,COUNTER_TO), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        CounterServiceGrpc.CounterServiceStub stub =
                CounterServiceGrpc.newStub(channel);
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<CountersResponse> observer = new StreamObserver<CountersResponse>() {
            boolean headerPrinted = false;
            @Override
            public void onNext(CountersResponse value) {
                if(!headerPrinted){
                    System.out.printf("%-9s %-16s %-19s %-11s\n","Counters","Airline","Flights","People");
                    System.out.printf("%s\n","#".repeat(58));
                    headerPrinted = true;
                }
                System.out.printf("%s\t%s\t%s\t%s\n",
                        value.hasRange()?String.format("(%d-%d)",value.getRange().getStart(),value.getRange().getEnd()):"-",
                        value.hasAirline()?value.getAirline():"-",
                        value.getFlightsCount()!=0?String.join("|", value.getFlightsList()):"-",
                        value.hasPeopleInLine()?value.getPeopleInLine():"-");
            }
            @Override
            public void onError(Throwable t) {
                switch (getError(t)){
                    case SECTOR_NOT_FOUND -> System.out.printf("Sector %s was not found\n",arguments.get(SECTOR));
                    case INVALID_RANGE -> System.out.printf("From val %s and to val %s are not a range of one or more counters\n",arguments.get(COUNTER_FROM),arguments.get(COUNTER_TO));
                    default -> System.out.println("An unknown error occurred while getting the counters");
                }
                finishLatch.countDown();
            }
            @Override
            public void onCompleted() {
                if(!headerPrinted){
                    System.out.printf("%-9s %-16s %-19s %-11s\n","Counters","Airline","Flights","People");
                    System.out.printf("%s\n","#".repeat(58));
                    headerPrinted = true;
                }
                finishLatch.countDown();
            }
        };
        stub.listCounters(CounterRequest.newBuilder()
                .setSector(arguments.get(SECTOR))
                .setRange(RangeMessage.newBuilder()
                        .setStart(Integer.parseInt(arguments.get(COUNTER_FROM)))
                        .setEnd(Integer.parseInt(arguments.get(COUNTER_TO))))
                .build(),observer);
        finishLatch.await();
    }
}
