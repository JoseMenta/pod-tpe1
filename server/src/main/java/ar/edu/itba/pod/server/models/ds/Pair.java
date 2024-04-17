package ar.edu.itba.pod.server.models.ds;

import lombok.Getter;

@Getter
public record Pair<K, T>(K first, T second) {

}
