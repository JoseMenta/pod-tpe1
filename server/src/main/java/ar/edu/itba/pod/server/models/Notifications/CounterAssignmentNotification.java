package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.commons.RangeMessage;
import ar.edu.itba.pod.grpc.notification.CounterAssignedNotification;
import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;

import java.util.List;

public class CounterAssignmentNotification implements Notification {

    private final Range range;


    public CounterAssignmentNotification(Range range) {
        this.range = range;
    }

    @Override
    public SubscriptionResponse createNotification() {
        return SubscriptionResponse.newBuilder()
                .setCounterAssigned(
                        CounterAssignedNotification.newBuilder()
                                //TODO: revisar get()
                                .setAirline(range.getAirline().get().getName())
                                .addAllFlights(range.getFlights().stream().map(Flight::getCode).toList())
                                .setRange(
                                       RangeMessage.newBuilder()
                                               .setStart(range.getStart())
                                               .setEnd(range.getEnd())
                                               .build())
                                .setSector(range.getSector().getName())
                                .build())
                .build();
    }
}
