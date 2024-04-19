package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.checkin.CheckInServiceGrpc;
import ar.edu.itba.pod.grpc.checkin.PassengerCheckInRequest;
import ar.edu.itba.pod.grpc.checkin.PassengerCheckInResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PassengerCheckInAction extends Action {

    public static final String BOOKING = "booking";
    public static final String SECTOR = "sector";
    public static final String COUNTER = "counter";
    public PassengerCheckInAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    private void printResponse(final String booking, final String sector, final PassengerCheckInResponse response) {
        final String flight = response.getFlight();
        final String airline = response.getAirline();
        final int peopleInLine = response.getPeopleInLine();
        final int start = response.getRange().getStart();
        final int end = response.getRange().getEnd();
        System.out.printf("Booking %s for flight %s from %s is now waiting to check-in on counters (%d-%d) in Sector %s with %d people in line\n",
                booking,
                flight,
                airline,
                start,
                end,
                sector,
                peopleInLine);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        Map<String, String> arguments = parseArguments();
        final String booking = arguments.get(BOOKING);
        final String sector = arguments.get(SECTOR);
        final String counter = arguments.get(COUNTER);
        final CheckInServiceGrpc.CheckInServiceBlockingStub stub =
                CheckInServiceGrpc.newBlockingStub(channel);
        try {
            final PassengerCheckInRequest request = PassengerCheckInRequest.newBuilder()
                    .setBooking(booking)
                    .setSector(sector)
                    .setCounterFrom(counter)
                    .build();
            final PassengerCheckInResponse response = stub.passengerCheckIn(request);
            printResponse(booking, sector, response);
        } catch (StatusRuntimeException e) {
            switch (e.getMessage()) {
                case "2" -> System.out.printf("Sector %s does not exist\n", sector);
                case "11" -> System.out.printf("Passenger with booking %s has already started the check-in process\n", booking);
                case "12" -> System.out.printf("There is no passenger with booking %s\n", booking);
                case "18" -> System.out.printf("Flight %s is not checking in the range of counters starting at %s\n", booking, counter);
            }
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
