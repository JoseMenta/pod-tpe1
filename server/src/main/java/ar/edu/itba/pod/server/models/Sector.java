package ar.edu.itba.pod.server.models;

import ar.edu.itba.pod.server.models.ds.RangeList;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Sector {
    @Getter
    private final String name;
    private final ConcurrentLinkedQueue<RequestRange> pendingRequests;
    private HistoryCheckIn historyCheckIn;
    private RangeList rangeList;

    public Sector(String name, HistoryCheckIn historyCheckIn) {
        if(historyCheckIn == null || name == null){
            throw new IllegalArgumentException("HistoryCheckIn can't be null");
        }
        if(name.contains(" ")){
            throw new IllegalArgumentException("Name can't contain spaces");
        }
        this.name = name;
        this.pendingRequests = new ConcurrentLinkedQueue<>();
        this.historyCheckIn = historyCheckIn;
        this.rangeList = new RangeList();
    }

    public synchronized void addRange(Range range){
        this.rangeList.addRange(range);
    }

    // TODO: Agregar el historyCheckIn al book
    public synchronized void book(int length, List<Flight> flightList, Airline airline){
        if(length <= 0 || flightList == null || airline == null){
            throw new IllegalArgumentException("Invalid arguments");
        }
        Optional<Range> range = this.rangeList.bookRange(length, flightList, airline);
        if(range.isEmpty()){
            this.pendingRequests.add(new RequestRange(length, flightList, airline));
            return;
        }
//        Airline.log(range.get());
    }

    public synchronized void free(int start) {
        if (!this.rangeList.freeRange(start) || start < 0) {
            throw new IllegalArgumentException("Invalid start");
        }
        boolean flag = true;
        while (flag) {
            RequestRange requestRange = this.pendingRequests.peek();
            if (requestRange == null) {
                flag = false;
            } else {
                Optional<Range> range = this.rangeList.bookRange(requestRange.length(), requestRange.flightList(), requestRange.airline());
                if (range.isPresent()) {
                    this.pendingRequests.poll();
//                    Airline.log(range.get());
                }else{
                    flag = false;
                }
            }
        }
    }
}
