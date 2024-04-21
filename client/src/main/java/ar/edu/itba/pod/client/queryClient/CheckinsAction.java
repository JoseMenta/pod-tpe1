package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.query.CheckInHistoryRequest;
import ar.edu.itba.pod.grpc.query.CheckInHistoryResponse;
import ar.edu.itba.pod.grpc.query.QueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class CheckinsAction extends Action {
    public static final String OUT_PATH = "outPath";

    public static final String SECTOR = "sector";
    public static final String AIRLINE = "airline";

    public CheckinsAction() {
        super(List.of(OUT_PATH), List.of(SECTOR, AIRLINE));
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        QueryServiceGrpc.QueryServiceStub stub = QueryServiceGrpc.newStub(channel);
        final StreamObserver<CheckInHistoryResponse> observer = new StreamObserver<CheckInHistoryResponse>() {
            BufferedWriter writer;
            @Override
            public void onNext(final CheckInHistoryResponse value) {
                try {
                    if(writer == null){
                        writer = Files.newBufferedWriter(
                                Paths.get(arguments.get(OUT_PATH)),
                                StandardOpenOption.APPEND,
                                StandardOpenOption.CREATE
                        );
                        writer.write(String.format("%-7s %-9s %-17s %-10s %-16s\n","Sector","Counter","Airline","Flight","Booking"));
                        writer.write(String.format("%s\n","#".repeat(63)));
                    }
                    writer.write(String.format("%-7s %-9s %-17s %-10s %-16s\n",
                            value.getSector(),
                            value.getCounter(),
                            value.getAirline(),
                            value.getFlight(),
                            value.getBooking()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override

            public void onError(final Throwable t) {
                switch (getError(t)){
                    case EMPTY_AIRPORT -> System.out.println("There are no check-in's done for now");
                    default -> System.out.println("An unknown error occurred while listing check-in history");
                }
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };
        CheckInHistoryRequest.Builder requestBuilder = CheckInHistoryRequest.newBuilder();
        Optional.ofNullable(arguments.get(SECTOR)).ifPresent(requestBuilder::setSector);
        Optional.ofNullable(arguments.get(AIRLINE)).ifPresent(requestBuilder::setAirline);
        stub.checkInHistory(requestBuilder.build(),observer);
        finishLatch.await();
    }
}
