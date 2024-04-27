package ar.edu.itba.pod.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

@Getter
public class Client implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private final ManagedChannel channel;
    private final Action action;


    public Client(final String host,Action action) {
        if (host == null || action == null) {
            throw new IllegalArgumentException("Host and action must not be null");
        }

        this.channel = ManagedChannelBuilder.forTarget(host)
                .usePlaintext()
                .build();
        this.action = action;
    }

    public void run() throws InterruptedException {
        action.run(this.channel);
    }

    @Override
    public void close()  {
        try {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
