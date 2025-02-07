package ar.edu.itba.pod.server.models;

import ar.edu.itba.pod.server.exceptions.*;
import ar.edu.itba.pod.server.models.Notifications.CheckInEndedNotification;
import ar.edu.itba.pod.server.models.Notifications.CounterAssignmentNotification;
import ar.edu.itba.pod.server.models.Notifications.PassengerQueuedNotification;
import ar.edu.itba.pod.server.models.Notifications.PendingAssignmentNotification;
import ar.edu.itba.pod.server.models.ds.Pair;
import ar.edu.itba.pod.server.models.ds.RangeList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Sector {
    @Getter
    private final String name;
    private final List<RequestRange> pendingRequests;
    private final HistoryCheckIn historyCheckIn;
    private final RangeList rangeList;

    public Sector(String name, HistoryCheckIn historyCheckIn) {
        if(historyCheckIn == null || name == null){
            throw new IllegalArgumentException("HistoryCheckIn can't be null");
        }
        if(name.contains(" ")){
            throw new IllegalArgumentException("Name can't contain spaces");
        }
        this.name = name;
        this.pendingRequests = new ArrayList<>();
        this.historyCheckIn = historyCheckIn;
        this.rangeList = new RangeList();
    }

    public synchronized void addRange(Range range){
        this.rangeList.addRange(range);
        bookPendingRequestsIfPossible();
    }

    public synchronized Pair<Optional<Range>, Integer> book(int length, List<Flight> flightList, Airline airline){
        if(length <= 0 || flightList == null || airline == null){
            throw new IllegalArgumentException("Invalid arguments");
        }
        Optional<Range> range = this.rangeList.bookRange(length, flightList, airline);
        if (range.isPresent()) {
            airline.log(new CounterAssignmentNotification(range.get()));
            return new Pair<>(range, null);
        }
        flightList.forEach(Flight::waitingRange);
        final int pendingRequestsAhead = this.pendingRequests.size();
        this.pendingRequests.add(new RequestRange(length, flightList, airline));
        airline.log(new PendingAssignmentNotification(flightList,this,length, pendingRequestsAhead));
        return new Pair<>(Optional.empty(), pendingRequestsAhead);
    }

    //Lo hago aca para evitar el siguiente caso que se daba cuando se devolvía el rango, y se hacía desde afuera:
    //T1 obtiene el rango
    //T2 libera el rango en el sector
    //T1 mete al pasajero en el rango liberado
    //Esto se daba porque no se bloqueaban llamadas a liberar un rango en el sector cuando se estaba agregando un pasajero
    public synchronized int addPassengerToQueue(final Passenger passenger,final int start){
        if(passenger.getPassengerStatus().equals(PassengerStatus.WAITING)){
            throw new PassengerAlreadyEnqueuedException();
        }
        if (passenger.getPassengerStatus().equals(PassengerStatus.CHECKED)){
            throw new PassengerAlreadyCheckedInException();
        }
        final Range range = rangeList.getRangeByStart(start).orElseThrow(InvalidRangeException::new);
        if(!range.hasFlight(passenger.getFlight())){
            throw new FlightNotInRangeException();
        }
        passenger.enqueue();
        int waitingAhead = range.addPassengerToQueue(passenger);
        passenger.getAirline().log(new PassengerQueuedNotification(passenger,waitingAhead));
        return waitingAhead;
    }

    //Va porque si se esta liberando uno, tiene que ser consistente lo que se muestra en el listado con lo que pasa (no se puede mostrar como ocupado y liberar o viceversa)
    public synchronized List<Range> getRangesInInterval(final int from, final int to){
        return rangeList.getRangesInInterval(from,to);
    }

    public synchronized List<Range> getRanges(){
        return rangeList.getElements();
    }

    public synchronized Pair<List<Passenger>,List<Counter>> checkInCounters(final int from, final Airline airline){
        Range range = rangeList.getRangeByStart(from).orElseThrow(InvalidRangeException::new);

        if (!range.getAirline().orElseThrow(AirlineNotInRangeException::new).equals(airline)) {
            throw new AirlineNotInRangeException();
        }
        return range.checkIn(this.historyCheckIn);
    }

    private synchronized void bookPendingRequestsIfPossible(){
        ListIterator<RequestRange> iterator = pendingRequests.listIterator();
        while (iterator.hasNext()){
            RequestRange requestRange = iterator.next();
            Optional<Range> range = this.rangeList.bookRange(requestRange.length(), requestRange.flightList(), requestRange.airline());
            if (range.isPresent()) {
                iterator.remove();
                AtomicInteger index = new AtomicInteger(0);
                pendingRequests.forEach(r -> r.airline().log(new PendingAssignmentNotification(r.flightList(), this,r.length(), index.getAndIncrement())));
            }
        }
    }


    public synchronized Range free(int start, final Airline airline) {
        if (start < 0) {
            throw new InvalidRangeStartException();
        }
        Range rangeToFree = rangeList.getRangeByStart(start).orElseThrow(InvalidRangeException::new);
        if(!this.rangeList.freeRange(start, airline)) {
            throw new InvalidRangeException();
        }
        airline.log(new CheckInEndedNotification(rangeToFree));
        rangeToFree.getFlights().forEach(Flight::endCheckIn);
        bookPendingRequestsIfPossible();
        return rangeToFree;
    }

    public synchronized List<RequestRange> getPendingRequests() {
        return List.copyOf(this.pendingRequests);
    }
}
