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

        try (
                BufferedWriter fileOutput = Files.newBufferedWriter(
                        Paths.get(arguments.get(OUTPATH)),
                        StandardOpenOption.APPEND,
                        StandardOpenOption.CREATE
                );
        ) {
            //fileOutput.write(String.format("%f %s %f %f %f %f", elapsedTime, particle.getIdentifier(), particle.getX(), particle.getY(), particle.getVelocity(), particle.getAngle()));

            final StreamObserver<CheckInStatusResponse> observer = new StreamObserver<CheckInStatusResponse>() {
                @Override
                public void onNext(final CheckInStatusResponse checkIn) {
                    try {
                        fileOutput.write(String.format("%s\t(%d-%d)\t%s\t%s\t%s",
                                checkIn.getSector(), checkIn.getRange().getStart(), checkIn.getRange().getEnd(),
                                checkIn.getAirline(), "airlines", checkIn.getWaiting()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(final Throwable throwable) {
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
         catch (IOException e) {
            throw new RuntimeException("Could not write files.");
        }
    }
}
