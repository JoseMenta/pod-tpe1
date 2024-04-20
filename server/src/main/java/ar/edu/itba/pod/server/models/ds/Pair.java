package ar.edu.itba.pod.server.models.ds;

import lombok.Getter;

public record Pair<K, T>(K first, T second) {

    public Pair(K first) {
        this(first, null);
    }

    public boolean hasFirst(){
        return first!=null;
    }

    public boolean hasSecond(){
        return second!=null;
    }

}
