package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.checkin.*;
import ar.edu.itba.pod.grpc.commons.RangeMessage;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.mappers.RangeMapper;
import ar.edu.itba.pod.server.models.*;
import ar.edu.itba.pod.server.models.ds.Pair;
import io.grpc.stub.StreamObserver;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CheckInServantImpl extends CheckInServiceGrpc.CheckInServiceImplBase {

    private final AirportService airportService;
    private final static Function<Flight, CheckInRangeResponse> CHECK_IN_RANGE_RESPONSE_MAPPER = (flight) -> {
        CheckInRangeResponse.Builder response = CheckInRangeResponse.newBuilder()
                .setFlight(flight.getCode())
                .setAirline(flight.getAirline().getName());
        if (flight.getRange() != null) {
            Range range = flight.getRange();
            AssignedRange assignedRange = AssignedRange.newBuilder()
                    .setSector(range.getSector().getName())
                    .setWaitingCount(range.getWaitingCount())
                    .setRange(RangeMapper.mapToRangeMessage(range))
                    .build();
            response.setAssignedRange(assignedRange);
        }
        return response.build();
    };

    private final static BiFunction<Passenger, Integer, PassengerCheckInResponse> PASSENGER_CHECK_IN_RESPONSE_MAPPER = (passenger, waitingAhead) -> {
        Airline airline = passenger.getAirline();
        Flight flight = passenger.getFlight();
        Range range = flight.getRange();
        return PassengerCheckInResponse.newBuilder()
                .setAirline(airline.getName())
                .setFlight(flight.getCode())
                .setPeopleInLine(waitingAhead)
                .setRange(RangeMapper.mapToRangeMessage(range))
                .build();
    };

    private final static Function<Passenger, PassengerResponse> PASSENGER_RESPONSE_MAPPER = (passenger) -> {
        Airline airline = passenger.getAirline();
        Flight flight = passenger.getFlight();
        Range range = flight.getRange();
        Sector sector = range.getSector();
        PassengerStatus passengerStatus = passenger.getPassengerStatus();
        PassengerResponse.Builder builder = PassengerResponse.newBuilder()
                .setAirline(airline.getName())
                .setFlight(flight.getCode())
                .setSector(sector.getName())
                .setRange(RangeMapper.mapToRangeMessage(range));
        if (passengerStatus == PassengerStatus.CHECKED) {
            Counter counter = passenger.getCounter();
            builder.setCounter(counter.getId());
        } else {
            RangeMessage rangeMessage = RangeMapper.mapToRangeMessage(range);
            if (passengerStatus == PassengerStatus.WAITING) {
                RangeWaitingInfo rangeWaitingInfo = RangeWaitingInfo.newBuilder()
                        .setWaitingPassenger(range.getWaitingCount(passenger))
                        .setRange(rangeMessage)
                        .build();
                builder.setWaiting(rangeWaitingInfo);
            } else {
                builder.setRange(rangeMessage);
            }
        }
        return builder.build();
    };

    public CheckInServantImpl(final AirportService airportService) {
        this.airportService = airportService;
    }

    @Override
    public void checkInRangeQuery(CheckInRangeRequest request, StreamObserver<CheckInRangeResponse> responseObserver) {
        String booking = request.getBooking();
        Flight flight = airportService.fetchCounter(booking);
        responseObserver.onNext(CHECK_IN_RANGE_RESPONSE_MAPPER.apply(flight));
        responseObserver.onCompleted();
    }

    @Override
    public void passengerCheckIn(PassengerCheckInRequest request, StreamObserver<PassengerCheckInResponse> responseObserver) {
        String booking = request.getBooking();
        String sectorName = request.getSector();
        int counterFrom = request.getCounterFrom();
        Pair<Passenger, Integer> pair = airportService.addPassengerToQueue(booking, sectorName, counterFrom);
        Passenger passenger = pair.getFirst();
        int waitingAhead = pair.getSecond();
        responseObserver.onNext(PASSENGER_CHECK_IN_RESPONSE_MAPPER.apply(passenger, waitingAhead));
        responseObserver.onCompleted();
    }

    @Override
    public void passengerStatus(PassengerRequest request, StreamObserver<PassengerResponse> responseObserver) {
        String booking = request.getBooking();
        Passenger passenger = airportService.checkPassengerStatus(booking);
        responseObserver.onNext(PASSENGER_RESPONSE_MAPPER.apply(passenger));
        responseObserver.onCompleted();
    }
}
