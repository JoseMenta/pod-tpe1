package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.query.*;
import io.grpc.stub.StreamObserver;

public class QueryServantImpl extends QueryServiceGrpc.QueryServiceImplBase {

    @Override
    public void checkInHistory(CheckInHistoryRequest request, StreamObserver<CheckInHistoryResponse> responseObserver) {
        super.checkInHistory(request, responseObserver);
    }

    @Override
    public void checkInStatus(CheckInStatusRequest request, StreamObserver<CheckInStatusResponse> responseObserver) {
        super.checkInStatus(request, responseObserver);
    }
}
