package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.admin.AdminServiceGrpc;
import ar.edu.itba.pod.grpc.admin.SectorRequest;
//import ar.edu.itba.pod.grpc.admin.SectorResponse;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddSectorAction extends Action {
    public static final String SECTOR = "sector";
    public AddSectorAction(List<String> expectedArguments) {
        super(expectedArguments);
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        Map<String, String> arguments = parseArguments();

        try{
            AdminServiceGrpc.AdminServiceBlockingStub stub =
                    AdminServiceGrpc.newBlockingStub(channel);

            SectorRequest request = SectorRequest.newBuilder().setSector(arguments.get(SECTOR)).build();
            /*
            SectorResponse response = stub.addSector(request);

            if(response.getReady()){
                System.out.println("se creo bien");
            }else{
                System.out.println("se creo mal");
            }
            */
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }

    }
}
