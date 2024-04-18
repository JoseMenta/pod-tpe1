package ar.edu.itba.pod.server.models;

import ar.edu.itba.pod.server.exceptions.EmptyHistoryCheckInException;

import java.util.ArrayList;
import java.util.List;

public class HistoryCheckIn {

    List<Passenger> historyCheckIn;

    public HistoryCheckIn() {
        this.historyCheckIn = new ArrayList<>();
    }

    public synchronized void logHistory(Passenger passenger) {
        historyCheckIn.add(passenger);
    }

    public synchronized List<Passenger> getHistoryCheckIn(final String sector,final String airline) {
        if (historyCheckIn.isEmpty())
        {
            throw new EmptyHistoryCheckInException();
        }
        if (sector == null){
            return historyCheckIn.stream().filter(p -> p.getAirline().getName().equals(airline)).toList();
        }
        if (airline == null){
            return historyCheckIn.stream().filter(p -> p.getFlight().getRange().getSector().getName().equals(sector)).toList();
        }
        return historyCheckIn.stream().filter(p -> p.getAirline().getName().equals(airline) && p.getFlight().getRange().getSector().getName().equals(sector)).toList();
    }

}
