package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.admin.AdminServiceGrpc;
import ar.edu.itba.pod.grpc.admin.SectorRequest;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddSectorAction extends Action {
    //    -DserverAddress=localhost:50051 -Daction=addSector -Dsector=C
    public static final String SECTOR = "sector";
    public AddSectorAction() {
        super(List.of(SECTOR), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        AdminServiceGrpc.AdminServiceBlockingStub stub =
                AdminServiceGrpc.newBlockingStub(channel);
        final String sector = this.arguments.get(SECTOR);
        try{
            SectorRequest request = SectorRequest.newBuilder().setSector(sector).build();
            stub.addSector(request);
            System.out.printf("Sector %s added successfully\n",sector);
        }catch (StatusRuntimeException e){
            switch (getError(e)){
                case ALREADY_EXISTS -> System.out.printf("Sector %s has already been added\n",sector);
                default -> System.out.printf("An unknown error occurred while adding sector %s\n",sector);
            }
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }

    }
}
