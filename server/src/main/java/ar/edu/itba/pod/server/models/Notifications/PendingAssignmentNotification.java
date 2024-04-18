package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.Sector;

public class PendingAssignmentNotification implements Notification {

    private final Range range;
    private final int pendingAhead;

    public PendingAssignmentNotification(final Range range, final int pendingAhead) {
        this.range = range;
        this.pendingAhead = pendingAhead;
    }
    @Override
    public SubscriptionResponse createNotification() {
        return SubscriptionResponse.newBuilder().setPendingAssignment(
                ar.edu.itba.pod.grpc.notification.PendingAssignmentNotification.newBuilder()
                        .setSector(range.getSector().getName())
                        .addAllFlights(range.getFlights().stream().map(Flight::getCode).toList())
                        .setCounterAmount(range.size())
                        .setPendingAhead(pendingAhead)
                        .build()
        ).build();
    }
}
