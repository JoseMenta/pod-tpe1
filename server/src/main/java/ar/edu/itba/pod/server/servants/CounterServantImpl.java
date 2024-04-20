package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.counter.*;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.mappers.RangeMapper;
import ar.edu.itba.pod.server.models.Counter;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Passenger;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.ds.Pair;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Optional;

public class CounterServantImpl extends CounterServiceGrpc.CounterServiceImplBase{


    private final AirportService airportService;

    public CounterServantImpl(final AirportService airportService) {
        this.airportService = airportService;
    }
    @Override
    public void listSectors(Empty request,
                           StreamObserver<SectorResponse> responseObserver) {
        airportService.listSectors().forEach(sector -> {
            responseObserver.onNext(
                            SectorResponse.newBuilder()
                                    .setName(sector.getName())
                                    .addAllRanges(sector.getRanges().stream().map(RangeMapper::mapToRangeMessage).toList()
                                    )
                            .build()
            );

        });
        responseObserver.onCompleted();

    }

    /**
     * <pre>
     *2,3
     * </pre>
     */
    @Override
    public void listCounters(CounterRequest request,
                             StreamObserver<CountersResponse> responseObserver) {
        airportService.listCounters(request.getSector(),request.getRange().getStart(),request.getRange().getEnd()).forEach(counters -> {
            if(counters.getAirline().isPresent()) {
                responseObserver.onNext(CountersResponse.newBuilder()
                        .setAirline(counters.getAirline().get().getName())
                        .setPeopleInLine(counters.size())
                        .addAllFlights(counters.getFlights().stream().map(Flight::getCode).toList())
                        .setRange(RangeMapper.mapToRangeMessage(counters))
                        .build());
            }else{
                responseObserver.onNext(CountersResponse.newBuilder()
                        .setRange(RangeMapper.mapToRangeMessage(counters))
                        .build());
            }
        });
        responseObserver.onCompleted();
    }

    /**
     * <pre>
     *2,4,6,7,8,9
     * </pre>
     */
    @Override
    public void bookRange(RangeBookingRequest request,
                          StreamObserver<RangeInfo> responseObserver) {
        Pair<Optional<Range>,Integer> assignment = airportService.assignRange(request.getSector(),request.getAirline(),request.getFlightsList(),request.getCounterCount());
        if(assignment.first().isEmpty()) {
            responseObserver.onNext(RangeInfo.newBuilder()
                            .setPendingCount(assignment.second())
                    .build());
        }else {
            responseObserver.onNext(RangeInfo.newBuilder()
                            .setAssignedRange(RangeMapper.mapToRangeMessage(assignment.first().get()))
                    .build());
        }
        responseObserver.onCompleted();
    }


    /**
     * <pre>
     *2,3,10,11
     * </pre>
     */
    @Override
    public void freeCounterRange(FreeRangeRequest request,
                                 StreamObserver<FreeRangeResponse> responseObserver) {
       Range range = airportService.freeCounters(request.getSector(),request.getStart(),request.getAirline());

        responseObserver.onNext(FreeRangeResponse.newBuilder()
                .setRange(RangeMapper.mapToRangeMessage(range))
                .addAllFlights(range.getFlights().stream().map(Flight::getCode).toList())
                .build());

        responseObserver.onCompleted();
    }

    /**
     * <pre>
     * 2,3,10
     * </pre>
     */
    @Override
    public void checkInCounters(CheckInCountersRequest request,
                                StreamObserver<CheckInCountersResponse> responseObserver) {
        Pair<List<Passenger>, List<Counter>> counters = airportService.checkInCounters(request.getSector(),request.getCountFrom(),request.getAirline());
         responseObserver.onNext(CheckInCountersResponse.newBuilder()
                .addAllCheckInCounters(
                        counters.first().stream().map(p ->
                                CheckInCounters.newBuilder()
                                        .setCounter(p.getCounter().getId())
                                        .setBooking(p.getBooking())
                                        .setFlight(p.getFlight().getCode())
                                        .build()

                                ).toList()
                )
                .addAllEmptyCounters(
                        counters.second().stream().map(Counter::getId).toList()
                )
                .build());
         responseObserver.onCompleted();
    }

    /**
     * <pre>
     * 2
     * </pre>
     */
    @Override
    public void listPendingAssignments(ListPendingRequest request,
                                       StreamObserver<PendingRangeInfo> responseObserver) {

        airportService.listPendingAssignments(request.getSector()).forEach(
                r ->
                        responseObserver.onNext(
                                PendingRangeInfo.newBuilder()
                                        .setAirline(r.airline().getName())
                                        .addAllFlights(r.flightList().stream().map(Flight::getCode).toList())
                                        .setSize(r.length())
                                        .build()
                        )
        );
        responseObserver.onCompleted();

    }


}
