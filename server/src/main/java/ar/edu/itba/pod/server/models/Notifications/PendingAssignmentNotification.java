package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.Sector;

import java.util.List;

public class PendingAssignmentNotification implements Notification {

    private final List<Flight> flightList;
    private final Sector sector;

    private final int counterAmount;
    private final int pendingAhead;

    public PendingAssignmentNotification(final List<Flight> flightList,final Sector sector,final int counterAmount, final int pendingAhead) {
        this.flightList = flightList;
        this.sector = sector;
        this.counterAmount = counterAmount;
        this.pendingAhead = pendingAhead;
    }
    @Override
    public SubscriptionResponse createNotification() {
        return SubscriptionResponse.newBuilder().setPendingAssignment(
                ar.edu.itba.pod.grpc.notification.PendingAssignmentNotification.newBuilder()
                        .setSector(sector.getName())
                        .addAllFlights(flightList.stream().map(Flight::getCode).toList())
                        .setCounterAmount(counterAmount)
                        .setPendingAhead(pendingAhead)
                        .build()
        ).build();
    }
}
