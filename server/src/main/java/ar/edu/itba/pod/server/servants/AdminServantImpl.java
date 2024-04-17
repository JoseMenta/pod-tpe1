package ar.edu.itba.pod.server.servants;

import ar.edu.itba.pod.grpc.admin.AdminServiceGrpc;
import ar.edu.itba.pod.grpc.admin.SectorRequest;
import ar.edu.itba.pod.grpc.admin.SectorResponse;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class AdminServantImpl extends AdminServiceGrpc.AdminServiceImplBase {

    @Override
    public void addSector(SectorRequest request,
                          StreamObserver<Empty> responseObserver) {

        if(request.getSector().equals("laucha")){
//            responseObserver.onNext(SectorResponse.newBuilder().setReady(false).build());
        }else {
//            responseObserver.onNext(SectorResponse.newBuilder().setReady(true).build());
        }
       responseObserver.onCompleted();
    }

}
