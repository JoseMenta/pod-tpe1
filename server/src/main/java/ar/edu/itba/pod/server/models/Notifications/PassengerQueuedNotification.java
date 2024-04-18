package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Passenger;

public class PassengerQueuedNotification implements Notification {

    private final Passenger passenger;

    private final int waitingCount;
    public PassengerQueuedNotification(final Passenger passenger, final int waitingCount) {
        this.passenger = passenger;
        this.waitingCount = waitingCount;
    }

    @Override
    public SubscriptionResponse createNotification() {
        return SubscriptionResponse.newBuilder()
                .setPassengerQueued(
                        ar.edu.itba.pod.grpc.notification.PassengerQueuedNotification.newBuilder()
                                .setBooking(passenger.getBooking())
                                .setAirline(passenger.getAirline().getName())
                                .setFlight(passenger.getFlight().getCode())
                                .setRange(
                                        ar.edu.itba.pod.grpc.commons.RangeMessage.newBuilder()
                                                .setStart(passenger.getFlight().getRange().getStart())
                                                .setEnd(passenger.getFlight().getRange().getEnd())
                                                .build())
                                .setSector(passenger.getFlight().getRange().getSector().getName())
                                .setWaitingCount(waitingCount)
                                .build())
                .build();
    }
}
