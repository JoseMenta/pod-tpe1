package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.admin.*;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.mappers.RangeMapper;
import ar.edu.itba.pod.server.models.Range;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class AdminServantImpl extends AdminServiceGrpc.AdminServiceImplBase {

    private final AirportService airportService;

    public AdminServantImpl(final AirportService airportService) {
        this.airportService = airportService;
    }

    @Override
    public void addSector(SectorRequest request,
                          StreamObserver<Empty> responseObserver) {
    airportService.addSector(request.getSector());
    responseObserver.onNext(Empty.newBuilder().build());
    responseObserver.onCompleted();
    }
    @Override
    public void addRange(RangeRequest request,
                         StreamObserver<RangeResponse> responseObserver) {
    Range range = airportService.addCountersToSector(request.getSector(),request.getCount());
    responseObserver.onNext(
            RangeResponse.newBuilder()
                    .setRange(
                    RangeMapper.mapToRangeMessage(range)
                    )
                    .setSector(range.getSector().getName())
                    .build()
    );
    responseObserver.onCompleted();
    }

    @Override
    public void addFlight(FlightMessage request,
                          StreamObserver<Empty> responseObserver){
    airportService.addBooking(request.getBooking(), request.getFlight(),request.getAirline());
    responseObserver.onNext(Empty.newBuilder().build());
    responseObserver.onCompleted();
    }



}
