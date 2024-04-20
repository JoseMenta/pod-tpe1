package ar.edu.itba.pod.server.models;

import ar.edu.itba.pod.server.exceptions.AirlineMultiSubscriptionException;
import ar.edu.itba.pod.server.exceptions.AirlineNullSubscriptionException;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.Notifications.NotificationPill;
import ar.edu.itba.pod.server.models.ds.LogMessage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Airline {

    private static final Logger LOGGER = LoggerFactory.getLogger(Airline.class);
    private static final int MESSAGE_QUEUE_SIZE = 10;

    @Getter
    private final String name;
    private BlockingQueue<Notification> messageQueue;

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o instanceof Airline other){
            return other.name.equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Airline(String name) {
        this.name = name;
        messageQueue = null;
    }

    /**
     * Register the subscription to the airline
     *
     * @return the message queue to be used by the subscriber
     * @throws AirlineMultiSubscriptionException if the airline has already a subscriptor
     */
    public synchronized BlockingQueue<Notification> subscribe() {
        if (messageQueue != null) {
            throw new AirlineMultiSubscriptionException();
        }
        LOGGER.info("Airline {} subscription added", name);
        messageQueue = new LinkedBlockingQueue<>(MESSAGE_QUEUE_SIZE);
        return messageQueue;
    }

    /**
     * Unregister the subscription to the airline
     *
     * @throws AirlineNullSubscriptionException if the airline has no subscriptor
     */
    public synchronized void unsubscribe() throws InterruptedException {
        if (messageQueue == null) {
            throw new AirlineNullSubscriptionException();
        }
        LOGGER.info("Airline {} subscription removed", name);
        messageQueue.put(new NotificationPill());
        messageQueue = null;
    }

    /**
     * Log a message to the airline message queue
     *
     * @param message the message to log
     *
     * @return true if the message was logged, false otherwise
     */
    public synchronized boolean log(Notification message) {
        if (messageQueue == null){
            return false;
        }
        boolean logged = messageQueue.offer(message);
        if (!logged) {
            LOGGER.warn("Airline {} message queue is full, dropping message", name);
        }
        return logged;
    }
}
