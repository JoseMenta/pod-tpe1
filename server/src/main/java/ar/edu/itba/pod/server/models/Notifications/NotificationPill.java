package ar.edu.itba.pod.server.models.Notifications;

import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.server.interfaces.Notification;

public class NotificationPill implements Notification {
    @Override
    public SubscriptionResponse createNotification() {
        return null;
    }
}
