package ar.edu.itba.pod.server.models;

import lombok.Getter;

import java.util.*;

public class Range implements Comparable<Range>{

    @Getter
    private final int start;

    @Getter
    private final int end;
    private final Sector sector;
    private final List<Counter> counters;
    private final List<Flight> flights;
    private final Queue<Passenger> passengerQueue;
    private final Airline airline;
    private static final Comparator<Range> comparator = Comparator.comparing(Range::getStart).thenComparing(Range::getEnd);

    public Range(int start, int end, Sector sector, List<Counter> counters) {
        if(start>end || counters.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
        this.sector = sector;
        this.counters = new ArrayList<>(counters);
        this.flights = Collections.emptyList();
        this.passengerQueue = new ArrayDeque<>(); //TODO: ver si hay algo parecido a Collections.emptyList()
        this.airline = null;
    }

    private Range(int start, int end,Sector sector, List<Counter> counters, List<Flight> flights, Airline airline) {
        //Ojo, aca se asignan directamente las colecciones
        //Cuando se usa este metodo, se deben crear antes
        this.start = start;
        this.end = end;
        this.sector = sector;
        this.counters = counters;
        this.flights = flights;
        this.passengerQueue = new ArrayDeque<>(); //TODO: ver si hay algo parecido a Collections.emptyList()
        this.airline = airline;
    }

    public boolean canMerge(final Range next){
        if(next == null){
            return false;
        }
        return this.end+1 == next.start && !this.isOccupied() && !next.isOccupied();
    }

    public synchronized boolean isOccupied(){
        return !this.flights.isEmpty();
    }

    /**
     * Merges two contiguous ranges
     * @param next: the contiguous range to merge after this
     * @return the range that merges the two ranges
     * @throws IllegalArgumentException if the ranges are not contiguous
     */

    public synchronized Range merge(final Range next){
        if(!canMerge(next)){
            throw new IllegalArgumentException();
        }
        List<Counter> newCounters = new ArrayList<>(counters);
        newCounters.addAll(next.counters);
        return new Range(this.start, next.end,this.sector,newCounters,Collections.emptyList(), null);
    }

    public int size(){
        return this.end - this.start + 1;
    }

    public boolean canBook(final int length){
        return length<=size() && !isOccupied();
    }

    //synchronized para acceder a la lista de counters
    /**
     * Books a sub-range of the sector returning two or one ranges, the first one of length counters and the second one of size() - length elements if size() - length >0
     * @param length: the length of the first Range
     * @param flights: the list of flights
     * @param airline: the airline of the flights
     * @return a list of the resulting ranges, with two non-empty ranges or one empty range
     * @throws IllegalArgumentException if length is negative or zero
     */
    public synchronized List<Range> book(final int length, final List<Flight> flights, final Airline airline) {
        if(length<=0){
            throw new IllegalArgumentException();
        }
        if(length == size()){
            final Range ans = new Range(this.start, this.end, this.sector, new ArrayList<>(counters),new ArrayList<>(flights), airline);
            return List.of(ans);
        }
        //return the two sub ranges
        final Range first = new Range(this.start, this.start + length-1, this.sector,new ArrayList<>(this.counters.subList(0,length)),new ArrayList<>(flights), airline);
        final Range last = new Range(this.start + length, this.end, this.sector, this.counters.subList(length,counters.size()));//use safe constructor
        return List.of(first,last);
    }

    @Override
    public int compareTo(Range o) {
         return comparator.compare(this,o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start,end,sector);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Range other){
            return this.start == other.start && this.end == other.end && this.sector.equals(other.sector);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Range: (%d,%d)",start,end);
    }

    public Range free(){
        return new Range(this.start,this.end,this.sector,this.counters);
    }

    public synchronized void addPassengerToQueue(final Passenger passenger){
        this.passengerQueue.add(passenger);
    }

    public synchronized void checkIn(){
        //TODO: implementar
    }
}
