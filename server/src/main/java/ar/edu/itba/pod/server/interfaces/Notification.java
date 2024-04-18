package ar.edu.itba.pod.server.interfaces;

import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;

public interface Notification {

    SubscriptionResponse createNotification();
}
