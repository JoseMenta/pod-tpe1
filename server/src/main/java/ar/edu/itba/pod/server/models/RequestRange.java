package ar.edu.itba.pod.server.models;

import lombok.Getter;

import java.util.List;

@Getter
public record RequestRange(int length, List<Flight> flightList, Airline airline) {

    public RequestRange {
        if (length > 0 || flightList == null || airline == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
    }

}
