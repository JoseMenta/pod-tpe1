package ar.edu.itba.pod.server.models;

import ar.edu.itba.pod.server.exceptions.EmptyHistoryCheckInException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryCheckIn {

    List<Passenger> historyCheckIn;

    public HistoryCheckIn() {
        this.historyCheckIn = new ArrayList<>();
    }

    public synchronized void logHistory(Passenger passenger) {
        historyCheckIn.add(passenger);
    }

    public synchronized List<Passenger> getHistoryCheckIn(final Optional<String> sector, final Optional<String> airline) {
        if (historyCheckIn.isEmpty())
        {
            throw new EmptyHistoryCheckInException();
        }
        if(sector.isEmpty() && airline.isEmpty()){
            return historyCheckIn.stream().toList();
        }
        if (sector.isEmpty()){
            return historyCheckIn.stream().filter(p -> p.getAirline().getName().equals(airline.get())).toList();
        }
        if (airline.isEmpty()){
            return historyCheckIn.stream().filter(p -> p.getFlight().getRange().getSector().getName().equals(sector.get())).toList();
        }
        return historyCheckIn.stream().filter(p -> p.getAirline().getName().equals(airline.get()) && p.getFlight().getRange().getSector().getName().equals(sector.get())).toList();
    }

}
