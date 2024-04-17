package ar.edu.itba.pod.server.models;

import lombok.Getter;

import java.util.List;

@Getter
public class RequestRange {
    private final int start;
    private final int end;
    private final List<Flight> flightList;
    private final Airline airline;

    public RequestRange(int start, int end, List<Flight> flightList, Airline airline) {
        if(start < 0 || end < 0 || start > end || flightList == null || airline == null){
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.start = start;
        this.end = end;
        this.flightList = flightList;
        this.airline = airline;
    }

}
