package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.notification.NotificationServiceGrpc;
import ar.edu.itba.pod.grpc.notification.SubscriptionRequest;
import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import ar.edu.itba.pod.grpc.notification.UnRegisterRequest;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import com.google.protobuf.Empty;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NotificationServantImpl extends NotificationServiceGrpc.NotificationServiceImplBase{

    private final AirportService airportService;
    private final Logger LOGGER = LoggerFactory.getLogger(NotificationServantImpl.class);
    public NotificationServantImpl(final AirportService airportService) {
        this.airportService = airportService;
    }






    @Override
    public void subscribeAirline(SubscriptionRequest request,
                                 StreamObserver<SubscriptionResponse> responseObserver) {
        final ServerCallStreamObserver<SubscriptionResponse> rsob = (ServerCallStreamObserver<SubscriptionResponse>) responseObserver;
        try {

            BlockingQueue<Notification> notifications = airportService.register(request.getAirline());
            Notification notification = notifications.take();
            while (notification == null || notification.createNotification() != null) {

                if(notification != null && !rsob.isCancelled()){
                    rsob.onNext(notification.createNotification());
                }else {
                    if(rsob.isCancelled()){
                        LOGGER.info("Client cancelled the request");
                        airportService.unregister(request.getAirline());
                        return;
                    }
                }
                notification = notifications.poll(1000, TimeUnit.MILLISECONDS);
            }
        }catch (InterruptedException e){
            LOGGER.error("Error while getting notifications",e);
        }
        rsob.onCompleted();
    }

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
