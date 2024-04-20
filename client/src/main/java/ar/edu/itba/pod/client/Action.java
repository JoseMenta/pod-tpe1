package ar.edu.itba.pod.client;

import io.grpc.ManagedChannel;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Action {

    protected final Map<String, String> arguments;

    public Action(List<String> expectedArguments, List<String> optionalArguments) {
        arguments = new HashMap<>();
        for (String arg : expectedArguments) {
            String aux = System.getProperty(arg);
            if (aux == null) {
                throw new IllegalArgumentException("Expected "+ arg + " argument");
            }else{
                arguments.put(arg, aux);
            }
        }
        for(String optArg : optionalArguments){
            String aux = System.getProperty(optArg);
            if(aux!=null){
                arguments.put(optArg,aux);
            }
        }
    }

    public abstract void run(ManagedChannel channel) throws InterruptedException;
}