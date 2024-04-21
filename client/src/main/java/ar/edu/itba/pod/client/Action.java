package ar.edu.itba.pod.client;

import ar.edu.itba.pod.grpc.commons.Error;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    protected Error getError(final StatusRuntimeException e){
        return Optional.ofNullable(e.getStatus().getDescription())
                .map(s -> {
                    try {
                        return Integer.parseInt(s);
                    }catch (Exception ex){
                        return  Error.UNSPECIFIED.getNumber();
                    }
                })
                .map(Error::forNumber)
                .orElse(Error.UNSPECIFIED);
    }

    protected Error getError(final Throwable t){
        if(t instanceof StatusRuntimeException e){
            return getError(e);
        }
        return Error.UNSPECIFIED;
    }
    public abstract void run(ManagedChannel channel) throws InterruptedException;
}