package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.query.QueryCountersRequest;
import ar.edu.itba.pod.grpc.query.QueryCountersResponse;
import ar.edu.itba.pod.grpc.query.QueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class CountersAction extends Action {
    public static final String OUT_PATH = "outPath";
    public static final String SECTOR = "sector";

    public CountersAction() {
        super(List.of(OUT_PATH), List.of(SECTOR));
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        QueryServiceGrpc.QueryServiceStub stub = QueryServiceGrpc.newStub(channel);

        final StreamObserver<QueryCountersResponse> observer = new StreamObserver<QueryCountersResponse>() {

            private void openWriterAndWriteHeader() {
                try {
                    writer = Files.newBufferedWriter(
                            Paths.get(arguments.get(OUT_PATH)),
                            StandardOpenOption.WRITE,
                            StandardOpenOption.CREATE
                    );

                    writer.write(String.format("%-7s %-9s %-16s %-19s %-8s\n", "Sector", "Counters", "Airline", "Flights", "People"));
                    writer.write(String.format("%s\n", "#".repeat(63)));
                }catch (Exception e){
                    //
                }
            }
            BufferedWriter writer;
            @Override
            public void onNext(final QueryCountersResponse checkIn) {
                try {
                    if(writer == null) {
                        openWriterAndWriteHeader();
                    }
                    String rango = String.format("(%s-%s)", checkIn.getRange().getStart(), checkIn.getRange().getEnd() );
                    writer.write(String.format("%s\t%s\t%s\t%s\t%s\n",
                            checkIn.getSector(), rango,
                            checkIn.hasAirline()?checkIn.getAirline():"-",
                            checkIn.getFlightCount()!=0?String.join("|", String.join("|", checkIn.getFlightList())):"-",
                            checkIn.hasWaiting()?checkIn.getWaiting():"-"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(final Throwable t) {
                switch (getError(t)){
                    case RANGE_NOT_ASSIGNED -> System.out.println("No counters were assigned in the airport");
                    default -> System.out.println("An unknown error occurred while getting the counters");
                }
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                if(writer == null){
                    openWriterAndWriteHeader();
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finishLatch.countDown();
            }
        };
        QueryCountersRequest.Builder builder = QueryCountersRequest.newBuilder();
        Optional.ofNullable(arguments.get(SECTOR)).ifPresent(builder::setSector);
        stub.queryCounters(builder.build(), observer);
        finishLatch.await();
    }
}
