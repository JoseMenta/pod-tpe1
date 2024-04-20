package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.query.CheckInStatusRequest;
import ar.edu.itba.pod.grpc.query.CheckInStatusResponse;
import ar.edu.itba.pod.grpc.query.QueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class CountersAction extends Action {
    public static final String OUTPATH = "outPath";
    public static final String SECTOR = "sector";

    public CountersAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        Map<String, String> arguments = parseArguments();

        final CountDownLatch finishLatch = new CountDownLatch(1);
        QueryServiceGrpc.QueryServiceStub stub = QueryServiceGrpc.newStub(channel);

        final StreamObserver<CheckInStatusResponse> observer = new StreamObserver<CheckInStatusResponse>() {

            BufferedWriter fileOutput;
            @Override
            public void onNext(final CheckInStatusResponse checkIn) {
                try {
                    if(fileOutput == null){
                        BufferedWriter fileOutput = Files.newBufferedWriter(
                                Paths.get(arguments.get(OUTPATH)),
                                StandardOpenOption.APPEND,
                                StandardOpenOption.CREATE
                        );

                        fileOutput.write(String.format("%-8s %-10s %-17s %-20s %-8s\n", "Sector", "Counters", "Airline", "Flights", "People"));
                        fileOutput.write("###############################################################");
                }
                String flights = String.join("|", String.join("|", checkIn.getFlightList()));
                String rango = String.format("(%s-%s)", checkIn.getRange().getStart(), checkIn.getRange().getEnd() );
                fileOutput.write(String.format("%-7s %-9s %-16s %-19s %-7s\n",
                        checkIn.getSector(), rango,
                        checkIn.getAirline(), flights, checkIn.getWaiting()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(final Throwable t) {
                switch (t.getMessage()){
                    case "15" -> System.out.printf("Range %s was not assigned in sector \n", arguments.get(SECTOR));
                    default -> System.out.println("An unknown error occurred while getting the counters");
                }
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };

        stub.checkInStatus(CheckInStatusRequest.newBuilder().setSector(arguments.get(SECTOR)).build(), observer);
        finishLatch.await();
    }
}
