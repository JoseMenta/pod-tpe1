package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.query.CheckInStatusRequest;
import ar.edu.itba.pod.grpc.query.CheckInStatusResponse;
import ar.edu.itba.pod.grpc.query.QueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

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
            @Override
            public void onNext(final CheckInStatusResponse checkIn) {
                //TODO
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
}
