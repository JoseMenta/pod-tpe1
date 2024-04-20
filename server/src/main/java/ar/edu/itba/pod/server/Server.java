package ar.edu.itba.pod.server;

import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.servants.AdminServantImpl;
import ar.edu.itba.pod.server.servants.CheckInServantImpl;
import ar.edu.itba.pod.server.services.AirportServiceImpl;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info(" Server Starting ...");
        final AirportService airportService = new AirportServiceImpl();
        int port = 50051;
        io.grpc.Server server = ServerBuilder.forPort(port)
                .addService(new AdminServantImpl(airportService))
                .addService(new CheckInServantImpl(airportService))
                .build();
        server.start();
        logger.info("Server started, listening on " + port);
        server.awaitTermination();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server since JVM is shutting down");
            server.shutdown();
            logger.info("Server shut down");
        }));
    }}
