package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.checkin.CheckInServiceGrpc;
import ar.edu.itba.pod.grpc.checkin.PassengerRequest;
import ar.edu.itba.pod.grpc.checkin.PassengerResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;

public class PassengerStatusAction extends Action {
    public static final String BOOKING = "booking";
    public PassengerStatusAction() {
        super(List.of(BOOKING), Collections.emptyList());
    }

    private void printResponse(final String booking, final PassengerResponse response) {
        final String flight = response.getFlight();
        final String airline = response.getAirline();
        final String sector = response.getSector();
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("Booking %s for flight %s from %s ", booking, flight, airline));

        switch(response.getCounterInfoCase().getNumber()) {
            case PassengerResponse.COUNTER_FIELD_NUMBER -> {
                final int counter = response.getCounter();
                sb.append(String.format("checked in at counter %d in Sector %s", counter, sector));
            }
            case PassengerResponse.RANGE_FIELD_NUMBER -> {
                final int start = response.getRange().getStart();
                final int end = response.getRange().getEnd();
                sb.append(String.format("can check-in on counters (%d-%d) in Sector %s", start, end, sector));
            }
            case PassengerResponse.WAITING_FIELD_NUMBER -> {
                final int start = response.getWaiting().getRange().getStart();
                final int end = response.getWaiting().getRange().getEnd();
                final int peopleInLine = response.getWaiting().getWaitingPassenger();
                sb.append(String.format("is now waiting to check-in on counters (%d-%d) in Sector %s with %d people in line",
                        start, end, sector, peopleInLine));
            }
        }
        System.out.println(sb);
    }

    @Override
    public void run(final ManagedChannel channel) throws InterruptedException {
        final String booking = arguments.get(BOOKING);
        final CheckInServiceGrpc.CheckInServiceBlockingStub stub =
                CheckInServiceGrpc.newBlockingStub(channel);
        try {
            final PassengerRequest request = PassengerRequest.newBuilder()
                    .setBooking(booking)
                    .build();
            final PassengerResponse response = stub.passengerStatus(request);
            printResponse(booking, response);
        } catch (StatusRuntimeException e) {
            switch (getError(e)) {
                case PASSENGER_NOT_FOUND -> System.out.printf("There is no passenger with booking %s\n", booking);
                case RANGE_NOT_ASSIGNED -> System.out.printf("The flight for booking %s has not been assigned a range of counters yet\n", booking);
                default -> System.out.printf("An unknown error occurred while fetching the status for booking %s\n", booking);
            }
        }
    }
}
