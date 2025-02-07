package ar.edu.itba.pod.server.models;

import java.util.List;

public record RequestRange(int length, List<Flight> flightList, Airline airline) {

    public RequestRange {
        if (length <= 0 || flightList == null || airline == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
    }

}
