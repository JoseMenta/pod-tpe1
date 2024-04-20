package ar.edu.itba.pod.client.eventsClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.counter.CounterServiceGrpc;
import ar.edu.itba.pod.grpc.counter.CountersResponse;
import ar.edu.itba.pod.grpc.counter.ListPendingRequest;
import ar.edu.itba.pod.grpc.notification.NotificationServiceGrpc;
import ar.edu.itba.pod.grpc.notification.SubscriptionRequest;
import ar.edu.itba.pod.grpc.notification.SubscriptionResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class RegisterAction extends Action {
    public static final String AIRLINE = "airline";

    public RegisterAction() {
        super(List.of(AIRLINE), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {

        final CountDownLatch finishLatch = new CountDownLatch(1);

        NotificationServiceGrpc.NotificationServiceStub stub =
                NotificationServiceGrpc.newStub(channel);
        StreamObserver<SubscriptionResponse> observer = new StreamObserver<>() {
            @Override
            public void onNext(SubscriptionResponse value) {

                switch (value.getNotificationCase().getNumber()){
                    case SubscriptionResponse.SUBSCRIPTION_FIELD_NUMBER -> System.out.printf("%s registered successfully for events\n",value.getSubscription().getAirline());
                    case SubscriptionResponse.COUNTERASSIGNED_FIELD_NUMBER -> System.out.printf("%d counters (%d-%d) in Sector %s are now checking in passengers from %s %s flights\n",value.getCounterAssigned().getRange().getEnd() - value.getCounterAssigned().getRange().getStart() + 1,value.getCounterAssigned().getRange().getStart(),value.getCounterAssigned().getRange().getEnd(),value.getCounterAssigned().getSector(),value.getCounterAssigned().getAirline(),String.join("|", value.getCounterAssigned().getFlightsList()));
                    case SubscriptionResponse.PASSENGERQUEUED_FIELD_NUMBER -> System.out.printf("Booking %s for flight %s from %s is now waiting to check-in on counters (%d-%d) in Sector %s with %d people in line\n",value.getPassengerQueued().getBooking(),value.getPassengerQueued().getFlight(),value.getPassengerQueued().getAirline(),value.getPassengerQueued().getRange().getStart(),value.getPassengerQueued().getRange().getEnd(),value.getPassengerQueued().getSector(),value.getPassengerQueued().getWaitingCount());
                    case SubscriptionResponse.COUNTERCHECKIN_FIELD_NUMBER -> System.out.printf("Check-in successful of %s for flight %s at counter %d in Sector %s\n",value.getCounterCheckIn().getBooking(),value.getCounterCheckIn().getFlight(),value.getCounterCheckIn().getCounter(),value.getCounterCheckIn().getSector());
                    case SubscriptionResponse.CHECKINENDED_FIELD_NUMBER -> System.out.printf("Ended check-in for flights %s on counters (%d-%d) from Sector %s\n",String.join("|",value.getCheckInEnded().getFlightsList()),value.getCheckInEnded().getRange().getStart(),value.getCheckInEnded().getRange().getEnd(),value.getCheckInEnded().getSector());
                    case SubscriptionResponse.PENDINGASSIGNMENT_FIELD_NUMBER -> System.out.printf("%d counters in Sector %s for flights %s is pending with %d other pendings ahead\n",value.getPendingAssignment().getCounterAmount(),value.getPendingAssignment().getSector(),String.join("|", value.getPendingAssignment().getFlightsList()),value.getPendingAssignment().getPendingAhead());
                }

            }
            @Override
            public void onError(Throwable t) {
                if(t instanceof StatusRuntimeException e){
                    switch (e.getStatus().getDescription()){
                        case "6" -> System.out.printf("Passenger not exists for airline %s\n",arguments.get(AIRLINE));
                        case "17" -> System.out.printf("Airline %s already subscribed\n",arguments.get(AIRLINE));
                        default -> System.out.println("An unknown error occurred while getting the counters");
                    }
                }else{
                    System.out.println("An unknown error occurred while getting the counters");
                }
            }
            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };
        stub.subscribeAirline(SubscriptionRequest.newBuilder().setAirline(arguments.get(AIRLINE)).build(),observer);
        finishLatch.await();
    }
}
