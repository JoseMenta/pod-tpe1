package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.commons.RangeMessage;
import ar.edu.itba.pod.grpc.query.*;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.Sector;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class QueryServantImpl extends QueryServiceGrpc.QueryServiceImplBase {

    private final AirportService airportService;

    public QueryServantImpl(final AirportService airportService) {
        this.airportService = airportService;
    }

    @Override
    public void checkInHistory(CheckInHistoryRequest request, StreamObserver<CheckInHistoryResponse> responseObserver) {
        Optional<String> sector = request.hasSector() ? Optional.of(request.getSector()) : Optional.empty();
        Optional<String> airline = request.hasAirline() ? Optional.of(request.getAirline())  : Optional.empty();

        airportService.queryCheckInHistory(sector, airline).forEach(h ->
            responseObserver.onNext(
                CheckInHistoryResponse.newBuilder()
                    .setSector(h.getFlight().getRange().getSector().getName())
                    .setAirline(h.getAirline().getName())
                    .setFlight(h.getFlight().getCode())
                    .setBooking(h.getBooking()).build())
        );

    }

    @Override
    public void checkInStatus(CheckInStatusRequest request, StreamObserver<CheckInStatusResponse> responseObserver) {
        Optional<String> name_sector = request.hasSector() ? Optional.of(request.getSector()) : Optional.empty();

        airportService.checkCountersStatus(name_sector).forEach(r ->{
                    CheckInStatusResponse.Builder checkInStatusBuilder = CheckInStatusResponse.newBuilder()
                            .setSector(r.getSector().getName())
                            .setRange(
                                    RangeMessage.newBuilder()
                                            .setStart(r.getStart()).setEnd(r.getEnd()).build()
                            )
                            .addAllFlight(r.getFlights().stream().map(Flight::getCode).toList());
                    if(r.getAirline().isPresent()){
                        checkInStatusBuilder.setAirline(r.getAirline().get().getName());
                    }
                    if(!r.getPassangerQueue().isEmpty()){
                        checkInStatusBuilder.setWaiting(r.getPassangerQueue().size());
                    }
                    responseObserver.onNext(checkInStatusBuilder.build());
                }
            );
        responseObserver.onCompleted();
    }
}
