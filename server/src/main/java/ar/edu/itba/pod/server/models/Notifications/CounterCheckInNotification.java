package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Passenger;

public class CounterCheckInNotification implements Notification {


    private final Passenger passenger;

    public CounterCheckInNotification(final Passenger passenger) {
        this.passenger = passenger;
    }


    @Override
    public SubscriptionResponse createNotification() {
        return SubscriptionResponse.newBuilder()
                .setCounterCheckIn(
                        ar.edu.itba.pod.grpc.notification.CounterCheckInNotification.newBuilder()
                                .setBooking(passenger.getBooking())
                                .setCounter(passenger.getCounter().getId())
                                .setFlight(passenger.getFlight().getCode())
                                .setSector(passenger.getFlight().getRange().getSector().getName())
                                .build())
                .build();
    }
}
