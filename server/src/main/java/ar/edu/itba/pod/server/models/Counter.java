package ar.edu.itba.pod.server.models;

import lombok.Getter;

@Getter
public class Counter implements Comparable<Counter> {

    private final int id;
    // private final CheckInHistory checkInHistory;

    public Counter(int id) {
        this.id = id;
        // this.checkInHistory = checkInHistory;
    }

    @Override
    public int compareTo(Counter o) {
        return Integer.compare(this.id, o.id);
    }

    public void checkIn(Passenger passenger, Sector sector) {
        // passenger.setCounter(this);
        // checkInHistory.add(passenger, sector, this);
    }
}
