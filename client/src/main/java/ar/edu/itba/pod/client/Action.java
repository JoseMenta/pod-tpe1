package ar.edu.itba.pod.client;

import io.grpc.ManagedChannel;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Action {

    private final List<String> expectedArguments;

    public Action(List<String> expectedArguments) {
        this.expectedArguments = expectedArguments;
    }

    public Map<String, String> parseArguments(){
        Map<String, String> arguments = new HashMap<>();
        for (String arg : expectedArguments) {
            String aux = System.getProperty(arg);
            if (aux == null) {
                throw new IllegalArgumentException("Arguments not valid");
            }else{
                arguments.put(arg, aux);
            }
        }
        return arguments;
    }

    public abstract void run(ManagedChannel channel) throws InterruptedException;
}