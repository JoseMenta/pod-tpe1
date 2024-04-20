package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.admin.AdminServiceGrpc;
import ar.edu.itba.pod.grpc.admin.RangeRequest;
import ar.edu.itba.pod.grpc.admin.RangeResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AddCounterAction extends Action {
//-DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=3
    public static final String SECTOR = "sector";
    public static final String COUNTERS = "counters";

    public AddCounterAction() {
        super(List.of(SECTOR, COUNTERS), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) {
        AdminServiceGrpc.AdminServiceBlockingStub stub =
                AdminServiceGrpc.newBlockingStub(channel);
        final int counterCount = Integer.parseInt(this.arguments.get(COUNTERS));
        final String sector = this.arguments.get(SECTOR);
        try{
            RangeResponse response = stub.addRange(RangeRequest.newBuilder()
                    .setSector(sector)
                    .setCount(counterCount)
                    .build());
            System.out.printf("%d new counters (%d,%d) in Sector %s added successfully\n",counterCount,response.getRange().getStart(),response.getRange().getEnd(),sector);
        }catch (StatusRuntimeException e){
            switch (getError(e)){
                case SECTOR_NOT_FOUND -> System.out.printf("Sector %s was not found\n",sector);
                case INVALID_RANGE -> System.out.println("Counter count must be positive");
                default -> System.out.println("An unknown error occurred while adding counter");
            }
        }
    }
}
