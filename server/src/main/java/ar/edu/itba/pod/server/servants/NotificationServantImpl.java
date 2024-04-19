package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.notification.NotificationServiceGrpc;
import ar.edu.itba.pod.grpc.notification.SubscriptionRequest;
import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.grpc.notification.UnRegisterRequest;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.BlockingQueue;

public class NotificationServantImpl extends NotificationServiceGrpc.NotificationServiceImplBase{

    private final AirportService airportService;

    public NotificationServantImpl(final AirportService airportService) {
        this.airportService = airportService;
    }





    /**
     * <pre>
     *6, 17
     * </pre>
     *
     */
    @Override
    public void subscribeAirline(SubscriptionRequest request,
                                 StreamObserver<SubscriptionResponse> responseObserver) throws InterruptedException {
        BlockingQueue<Notification> notifications = airportService.register(request.getAirline());
        Notification notification = notifications.take();
        while (notification.createNotification() != null) {
            responseObserver.onNext(notification.createNotification());
            notification = notifications.take();
        }
        responseObserver.onCompleted();
    }
    /**
     * <pre>
     * 16
     * </pre>
     */
    @Override
    public void unsubscribeAirline(UnRegisterRequest request,
                                   StreamObserver<Empty> responseObserver) {
        try{
            airportService.unregister(request.getAirline());

        }catch (InterruptedException e) {
            responseObserver.onError(e);
        }
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
