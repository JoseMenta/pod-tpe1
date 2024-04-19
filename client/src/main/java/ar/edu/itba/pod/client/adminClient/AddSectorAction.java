package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.admin.AdminServiceGrpc;
import ar.edu.itba.pod.grpc.admin.SectorRequest;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

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
        AdminServiceGrpc.AdminServiceBlockingStub stub =
                AdminServiceGrpc.newBlockingStub(channel);
        final String sector = arguments.get(SECTOR);
        try{
            SectorRequest request = SectorRequest.newBuilder().setSector(sector).build();
            stub.addSector(request);
        }catch (StatusRuntimeException e){
            switch (e.getMessage()){
                case "1" -> System.out.printf("Sector %s has already been added\n",sector);
                default -> System.out.printf("An unknown error occurred while adding sector %s\n",sector);
            }
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }

    }
}
