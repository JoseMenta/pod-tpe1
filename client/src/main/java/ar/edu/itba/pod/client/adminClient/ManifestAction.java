package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.admin.AdminServiceGrpc;
import ar.edu.itba.pod.grpc.admin.FlightMessage;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ManifestAction extends Action {
//    -DserverAddress=localhost:50051 -Daction=manifest -DinPath=client/src/main/resources/bookings.csv
    public static final String IN_PATH = "inPath";
    private static final int THREAD_COUNT = 10;
    private static final int LINE_COUNT = 10;

    public ManifestAction() {
        super(List.of(IN_PATH), Collections.emptyList());
    }

    @Override
    public void run(ManagedChannel channel) throws InterruptedException {
        final Path filePath = Path.of(arguments.get(IN_PATH));
        try(final BufferedReader reader = Files.newBufferedReader(filePath);
            final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
            ){
            reader.readLine();
            final List<Future<?>> futures = new ArrayList<>(THREAD_COUNT);
            for(int i = 0; i<THREAD_COUNT; i++){
                futures.add(executorService.submit(new AddFlightsRunnable(channel,reader)));
            }
            for(Future<?> f : futures){
                f.get();
            }
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("Could not add bookings");
        }
    }

    private static class AddFlightsRunnable implements Runnable{

        final private AdminServiceGrpc.AdminServiceBlockingStub blockingStub;
        final private BufferedReader reader;
        public AddFlightsRunnable(
                final ManagedChannel managedChannel,
                final BufferedReader reader
        ){
            this.blockingStub = AdminServiceGrpc.newBlockingStub(managedChannel);
            this.reader = reader;

        }

        private void getNextNLines(final List<String> list,final int n, final BufferedReader reader) throws IOException {
            list.clear();
            for(int i = 0; i<n; i++){
                String line = reader.readLine();
                if(line==null){
                    break;
                }
                list.add(line);
            }
        }

        @Override
        public void run() {
            final List<String> lines = new ArrayList<>(LINE_COUNT);
            do {
                //get next lines
                synchronized (reader){
                    try {
                        getNextNLines(lines,LINE_COUNT,reader);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (String line : lines){
                    //Assume csv is correct
                    String[] values = line.split(",");
                    try{
                        blockingStub.addFlight(
                                FlightMessage.newBuilder()
                                        .setBooking(values[0])
                                        .setFlight(values[1])
                                        .setAirline(values[2])
                                        .build()
                        );
                        System.out.printf("Booking %s for %s %s added successfully\n",values[0],values[2],values[1]);
                    }catch (StatusRuntimeException e){
                        switch (e.getStatus().getDescription()){
                            case "1" -> System.out.printf("Booking %s has already been added\n",values[0]);
                            case "4" -> System.out.printf("Flight %s has already been registered for another airline\n",values[1]);
                            default -> System.out.printf("An unknown error occurred while adding booking %s for %s %s\n",values[0],values[2],values[1]);
                        }

                    }
                }
            }while (!lines.isEmpty());
        }
    }
}
