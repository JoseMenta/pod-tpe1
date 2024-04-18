package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Airline;

public class SubscriptionNotification implements Notification {

    private final Airline airline;

    public SubscriptionNotification(final Airline airline) {
        this.airline = airline;
    }
    @Override
    public SubscriptionResponse createNotification() {
        return SubscriptionResponse.newBuilder().setSubscription(
                ar.edu.itba.pod.grpc.notification.SubscriptionNotification.newBuilder()
                        .setAirline(this.airline.getName()).build()
        ).build();
    }
}
