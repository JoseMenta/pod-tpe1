package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.checkin.CheckInRangeRequest;
import ar.edu.itba.pod.grpc.checkin.CheckInRangeResponse;
import ar.edu.itba.pod.grpc.checkin.CheckInServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;

public class FetchCounterAction extends Action {

    public static final String BOOKING = "booking";
    public FetchCounterAction() {
        super(List.of(BOOKING), Collections.emptyList());
    }

    private void printResponse(final CheckInRangeResponse response) {
        final String flight = response.getFlight();
        final String airline = response.getAirline();
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("Flight %s from %s ", flight, airline));
        if (response.hasAssignedRange()) {
            final int start = response.getAssignedRange().getRange().getStart();
            final int end = response.getAssignedRange().getRange().getEnd();
            final String sector = response.getAssignedRange().getSector();
            final int waitingAhead = response.getAssignedRange().getWaitingCount();
            sb.append(String.format("is now checking in at counters (%d-%d) in Sector %s with %d people in line",
                    start, end, sector, waitingAhead));
        } else {
            sb.append("has no counters assigned yet");
        }
        System.out.println(sb);
    }

    @Override
    public void run(final ManagedChannel channel) throws InterruptedException {
        final String booking = arguments.get(BOOKING);
        final CheckInServiceGrpc.CheckInServiceBlockingStub stub = CheckInServiceGrpc.newBlockingStub(channel);
        try {
            final CheckInRangeRequest request = CheckInRangeRequest.newBuilder().
                    setBooking(booking).build();
            final CheckInRangeResponse response = stub.checkInRangeQuery(request);
            printResponse(response);
        } catch (StatusRuntimeException e) {
            switch(getError(e)) {
                case PASSENGER_NOT_FOUND -> System.out.printf("There is no passenger with booking %s\n", booking);
                default -> System.out.printf("An unknown error occurred while fetching the check-in range for booking %s\n", booking);
            }
        }
    }
}
