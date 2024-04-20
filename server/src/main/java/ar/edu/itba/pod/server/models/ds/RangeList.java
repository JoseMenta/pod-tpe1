package ar.edu.itba.pod.server.models.ds;

import ar.edu.itba.pod.server.models.Airline;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;

import java.util.*;

public class RangeList {

    private final List<Range> ranges;

    public RangeList(){
        this.ranges = new LinkedList<>();
    }

    public RangeList(final LinkedList<Range> list){
        this.ranges = list;
    }


    /**
     * Adds a range to the list, merging the added range with existing ranges if possible
     * @param range: the range to be added
     * @throws NullPointerException if range is null
     */
    public synchronized void addRange(Range range){
        Objects.requireNonNull(range);
        if(ranges.isEmpty()){
            ranges.add(range);
            return;
        }
        ListIterator<Range> listIterator = ranges.listIterator();
        Range curr = listIterator.next();
        //Ver que pasa con hasNext()
        while (curr.compareTo(range)<0 && listIterator.hasNext()){//go to the next element (order of condition is important)
            curr = listIterator.next();
        }
        if(curr.compareTo(range)>=0){
            //try to merge with curr
            if(range.canMerge(curr)){
                //lo saco para reemplazarlo despues
                listIterator.remove();
                range = range.merge(curr);
            }else {
                listIterator.previous();
            }
        }
        if(listIterator.hasPrevious()){
            final Range prev = listIterator.previous();//voy al anterior
            if(prev.canMerge(range)){ //puedo mergearlo con range
                listIterator.set(prev.merge(range));
            }else {
                listIterator.next();//element is inserted before the one returned by next
                listIterator.add(range);
            }
        }else {
            listIterator.add(range);
        }
    }

    /**
     * Tries to book a range of length counters for flights, returning the booked length if possible (but not removing the range)
     * @param length: the length of the wanted range
     * @param flights: the flights booked for the range
     * @param airline: the airline of the flights
     * @return an optional with the booked range if one was found
     * @throws IllegalArgumentException if length <= 0 or flights is null or empty
     */
    public synchronized Optional<Range> bookRange(final int length, final List<Flight> flights, final Airline airline) {
        if(length<=0 || flights == null || flights.isEmpty()){
            throw new IllegalArgumentException();
        }
        if(ranges.isEmpty()){
            return Optional.empty();
        }
        ListIterator<Range> iterator = ranges.listIterator();
        Range curr = iterator.next();
        while (!curr.canBook(length) && iterator.hasNext()){//order of condition is important
            curr = iterator.next();
        }
        if(!curr.canBook(length)){
            return Optional.empty();
        }
        //curr can Book length
        Pair<Range,Range> splitRanges = curr.book(length,flights, airline);
        if(!splitRanges.hasSecond()){
            //All the range was booked
            iterator.set(splitRanges.first());
        }else{
            iterator.set(splitRanges.first());
            iterator.add(splitRanges.second());
        }
        return Optional.of(splitRanges.first());
    }

    /**
     *
     * @param start: the start of the range
     * @return true if the range was found, false otherwise
     */
    public synchronized boolean freeRange(final int start, final Airline airline){
        if(ranges.isEmpty()){
            return false;
        }
        //find the wanted range
        ListIterator<Range> iterator = ranges.listIterator();
        Range curr = iterator.next();
        while (iterator.hasNext() && curr.getStart()!=start){
            curr = iterator.next();
        }
        if(curr.getStart()!=start){
            //finished because there are not more elements
            return false;
        }
        //curr is the wanted range
        Range newRange = curr.free(airline);
        iterator.remove();//remove the used range from the list
        if(iterator.hasNext()){
            //we try to merge with next Range
            Range next = iterator.next();
            if(newRange.canMerge(next)){
                //merge the ranges
                newRange = newRange.merge(next);
                iterator.remove();
            }else {
                iterator.previous(); //for next/previous calls
            }
        }
        if(iterator.hasPrevious()){
            Range prev = iterator.previous();
            if(prev.canMerge(newRange)){
                iterator.set(prev.merge(newRange));
            }else {
                iterator.next();
                iterator.add(newRange);
            }
        }else {
            iterator.add(newRange);
        }
        return true;
    }


    /**
     *
     * @return an unmodifiable list with the ranges
     */
    public synchronized List<Range> getElements(){
        return Collections.unmodifiableList(ranges);
    }

    public synchronized List<Range> getRangesInInterval(final int from, final int to){
        return ranges.stream()
                .filter(r -> r.isInInterval(from,to))
                .toList();
    }

    public synchronized Optional<Range> getRangeByStart(final int start){
        return ranges.stream()
                .filter(r -> r.getStart() == start)
                .findFirst();
    }
    /**
     *
     * @param from: the start of the range
     * @return an optional with the range if it was found, empty otherwise
     */
    public synchronized Optional<Range> getRange(final int from){
        if(ranges.isEmpty()){
            return Optional.empty();
        }
        ListIterator<Range> iterator = ranges.listIterator();
        Range curr = iterator.next();
        while (iterator.hasNext() && curr.getStart()!=from){
            curr = iterator.next();
        }
        if(curr.getStart() != from){
            return Optional.empty();
        }
        return Optional.of(curr);
    }

}
