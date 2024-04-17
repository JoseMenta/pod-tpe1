package ar.edu.itba.pod.server.models;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter implements Comparable<Counter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Counter.class);

    @Getter
    private final int id;
    // private final CheckInHistory checkInHistory;

    public Counter(int id) {
        this.id = id;
        // this.checkInHistory = checkInHistory;
    }

//    public CheckInHistory getCheckInHistory() {
//        return checkInHistory.copy();
//    }

    @Override
    public int compareTo(Counter o) {
        return Integer.compare(this.id, o.id);
    }

    /**
     * Check in a passenger in this counter
     *
     * @param passenger the passenger to check in
     * @param sector the sector where the passenger is checking in
     */
    public void checkIn(Passenger passenger, Sector sector) {
        passenger.checkIn(this);
        // checkInHistory.add(passenger, sector, this);
        // LOGGER.info("Passenger {} checked in at counter {} in sector {}", passenger.getBooking(), id, sector.getName());
    }
}
