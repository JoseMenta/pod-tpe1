package ar.edu.itba.pod;

import ar.edu.itba.pod.server.models.*;
import ar.edu.itba.pod.server.models.ds.RangeList;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.SequencedCollection;


public class RangeListTest {

    //TODO: revisar static
    private static final Counter COUNTER = new Counter(1);
    private static final SequencedCollection<Counter> COUNTERS = List.of(COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER,COUNTER);
    private static final Sector SECTOR = new Sector("A",new HistoryCheckIn());//TODO: change after merge
    private static final SequencedCollection<Flight> FLIGHTS = List.of(new Flight("AAAAA",new Airline("A")));
    private static final Airline AIRLINE = new Airline("air");
    private static final Range RANGE11 = new Range(1,1,SECTOR,COUNTERS);
    private static final Range RANGE12 = new Range(1,2,SECTOR,COUNTERS);
    private static final Range RANGE13 = new Range(1,3,SECTOR,COUNTERS);
    private static final Range RANGE14 = new Range(1,4,SECTOR,COUNTERS);
    private static final Range RANGE23 = new Range(2,3,SECTOR,COUNTERS);
    private static final Range RANGE34 = new Range(3,4,SECTOR,COUNTERS);
    private static final Range RANGE45 = new Range(4,5,SECTOR,COUNTERS);
    private static final Range RANGE56 = new Range(5,6,SECTOR,COUNTERS);
    private static final Range RANGE66 = new Range(6,6,SECTOR,COUNTERS);
    private static final Range RANGE36 = new Range(3,6,SECTOR,COUNTERS);
    private static final Range RANGE16 = new Range(1,6,SECTOR,COUNTERS);
    private static final Range RANGE130 = new Range(1,30,SECTOR,COUNTERS);
    private static final Range RANGE1030 = new Range(10,30,SECTOR,COUNTERS);

    private final LinkedList<Range> list = new LinkedList<>(); //to access internal state without other methods (for unit tests)
    private final RangeList rangeList = new RangeList(list);

    @Before
    public void setUp(){
        list.clear();
    }

    @Test
    public void testAddForEmpty(){
        rangeList.addRange(RANGE11);

        Assertions.assertIterableEquals(List.of(RANGE11),list);
    }

    @Test
    public void testAddFirsMergeNext(){
        list.add(RANGE23);

        rangeList.addRange(RANGE11);

        Assertions.assertIterableEquals(List.of(RANGE13),list);
    }

    @Test
    public void testAddFirstWithoutMergeNext(){
        list.add(RANGE45);

        rangeList.addRange(RANGE11);

        Assertions.assertIterableEquals(List.of(RANGE11,RANGE45),list);
    }

    @Test
    public void testAddLastMergePrev(){
        list.add(RANGE12);

        rangeList.addRange(RANGE34);

        Assertions.assertIterableEquals(List.of(RANGE14),list);
    }


    @Test
    public void testAddLastWithoutMergePrev(){
        list.add(RANGE11);

        rangeList.addRange(RANGE34);

        Assertions.assertIterableEquals(List.of(RANGE11,RANGE34),list);
    }

    @Test
    public void testAddMiddleWithoutMerge(){
        list.add(RANGE11);
        list.add(RANGE66);

        rangeList.addRange(RANGE34);

        Assertions.assertIterableEquals(List.of(RANGE11,RANGE34,RANGE66),list);
    }

    @Test
    public void testAddMiddleMergePrev(){
        list.add(RANGE11);
        list.add(RANGE56);

        rangeList.addRange(RANGE23);

        Assertions.assertIterableEquals(List.of(RANGE13,RANGE56),list);
    }

    @Test
    public void testAddMiddleMergeNext(){
        list.add(RANGE11);
        list.add(RANGE56);

        rangeList.addRange(RANGE34);

        Assertions.assertIterableEquals(List.of(RANGE11,RANGE36),list);
    }

    @Test
    public void testAddMiddleMergePrevAndNext(){
        list.add(RANGE12);
        list.add(RANGE56);

        rangeList.addRange(RANGE34);

        Assertions.assertIterableEquals(List.of(RANGE16),list);
    }

    @Test
    public void testBookEmpty(){

        Optional<Range> ans = rangeList.bookRange(1,FLIGHTS, AIRLINE);

        Assertions.assertTrue(ans.isEmpty());
    }

    @Test
    public void testBookNotEnough(){
        list.add(RANGE130);

        Optional<Range> ans = rangeList.bookRange(40,FLIGHTS, AIRLINE);

        Assertions.assertTrue(ans.isEmpty());
    }

    @Test
    public void testBookAllRange(){
        list.add(RANGE16);

        Optional<Range> ans = rangeList.bookRange(6,FLIGHTS, AIRLINE);

        Assertions.assertTrue(ans.isPresent());
        Assertions.assertEquals(RANGE16,ans.get());
        Assertions.assertIterableEquals(List.of(RANGE16),list);
    }

    @Test
    public void testBookPartialRange(){
        list.add(RANGE130);

        Optional<Range> ans = rangeList.bookRange(9,FLIGHTS, AIRLINE);

        Range expected = new Range(1,9,SECTOR,COUNTERS);
        Assertions.assertTrue(ans.isPresent());
        Assertions.assertEquals(expected,ans.get());
        Assertions.assertIterableEquals(List.of(expected,RANGE1030),list);
    }

    @Test
    public void testBookPartialRangeAfterBooked(){
        list.add(RANGE16.book(6,FLIGHTS, AIRLINE).getFirst());
        list.add(new Range(7,20,SECTOR,COUNTERS));

        Optional<Range> ans = rangeList.bookRange(1,FLIGHTS, AIRLINE);

        Range expected = new Range(7,7,SECTOR,COUNTERS);
        Assertions.assertTrue(ans.isPresent());
        Assertions.assertEquals(expected,ans.get());
        Assertions.assertIterableEquals(List.of(RANGE16,expected,new Range(8,20,SECTOR,COUNTERS)),list);
    }

    @Test
    public void testFreeOnlyRange(){
        list.add(RANGE16.book(6,FLIGHTS, AIRLINE).getFirst());

        rangeList.freeRange(1);

        Assertions.assertIterableEquals(List.of(RANGE16),list);
    }


    @Test
    public void testFreeLastWithoutMerge(){
        list.add(RANGE12);
        list.add(RANGE45.book(2,FLIGHTS, AIRLINE).getFirst());

        rangeList.freeRange(4);

        Assertions.assertIterableEquals(List.of(RANGE12,RANGE45),list);
    }

    @Test
    public void testFreeLastPrevOccupied(){
        list.add(RANGE23.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE45.book(2,FLIGHTS, AIRLINE).getFirst());

        rangeList.freeRange(4);

        Assertions.assertIterableEquals(List.of(RANGE23,RANGE45),list);
    }

    @Test
    public void testFreeLastMerge(){
        list.add(RANGE23);
        list.add(RANGE45.book(2,FLIGHTS, AIRLINE).getFirst());

        rangeList.freeRange(4);

        Assertions.assertIterableEquals(List.of(new Range(2,5,SECTOR,COUNTERS)),list);
    }

    @Test
    public void testFreeFirstWithoutMerge(){
        list.add(RANGE11.book(1,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE45);

        rangeList.freeRange(1);

        Assertions.assertIterableEquals(List.of(RANGE11,RANGE45),list);
    }

    @Test
    public void testFreeFirstNextOccupied(){
        list.add(RANGE12.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());

        rangeList.freeRange(1);

        Assertions.assertIterableEquals(List.of(RANGE12,RANGE34),list);
    }

    @Test
    public void testFreeFirstMerge(){
        list.add(RANGE12.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE34);

        rangeList.freeRange(1);

        Assertions.assertIterableEquals(List.of(new Range(1,4,SECTOR,COUNTERS)),list);
    }

    @Test
    public void testFreeMiddleWithoutMerge(){
        list.add(RANGE11);
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE66);

        rangeList.freeRange(3);

        Assertions.assertIterableEquals(List.of(RANGE11,RANGE34,RANGE66),list);
    }

    @Test
    public void testFreeMiddleNextOccupied(){
        list.add(RANGE11);
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE56.book(2,FLIGHTS, AIRLINE).getFirst());

        rangeList.freeRange(3);

        Assertions.assertIterableEquals(List.of(RANGE11,RANGE34,RANGE56),list);
    }

    @Test
    public void testFreeMiddleMergeNext(){
        list.add(RANGE11);
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE56);

        rangeList.freeRange(3);

        Assertions.assertIterableEquals(List.of(RANGE11,new Range(3,6,SECTOR,COUNTERS)),list);
    }

    @Test
    public void testFreeMiddlePrevOccupied(){
        list.add(RANGE12.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE66);

        rangeList.freeRange(3);

        Assertions.assertIterableEquals(List.of(RANGE12,RANGE34,RANGE66),list);
    }
    @Test
    public void testFreeMiddleMergePrev(){
        list.add(RANGE12);
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE66);

        rangeList.freeRange(3);

        Assertions.assertIterableEquals(List.of(new Range(1,4,SECTOR,COUNTERS),RANGE66),list);
    }

    @Test
    public void testFreeMiddleMergePrevAndNext(){
        list.add(RANGE12);
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE56);

        rangeList.freeRange(3);

        Assertions.assertIterableEquals(List.of(RANGE16),list);
    }

    @Test
    public void testFreeMiddleOccupiedPrevAndNext(){
        list.add(RANGE12.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE34.book(2,FLIGHTS, AIRLINE).getFirst());
        list.add(RANGE56.book(2,FLIGHTS, AIRLINE).getFirst());

        rangeList.freeRange(3);

        Assertions.assertIterableEquals(List.of(RANGE12,RANGE34,RANGE56),list);
    }


}
