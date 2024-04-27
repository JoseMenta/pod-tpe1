package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.commons.RangeMessage;
import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;

public class CheckInEndedNotification implements Notification {

    private final Range range;

    public CheckInEndedNotification(final Range range) {
        this.range = range;
    }
    @Override
    public SubscriptionResponse createNotification() {
        return SubscriptionResponse.newBuilder().setCheckInEnded(
                ar.edu.itba.pod.grpc.notification.CheckInEndedNotification.newBuilder()
                        .addAllFlights(range.getFlights().stream().map(Flight::getCode).toList())
                        .setSector(range.getSector().getName())
                        .setRange(
                                RangeMessage.newBuilder()
                                        .setStart(range.getStart())
                                        .setEnd(range.getEnd())
                                        .build()
                        )
                        .build()
        ).build(
        );
    }
}
