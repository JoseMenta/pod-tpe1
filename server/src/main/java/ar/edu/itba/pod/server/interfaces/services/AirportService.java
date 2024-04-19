package ar.edu.itba.pod.server.interfaces.services;

import ar.edu.itba.pod.grpc.admin.RangeRequest;
import ar.edu.itba.pod.server.interfaces.Notification;
import ar.edu.itba.pod.server.models.*;
import ar.edu.itba.pod.server.models.ds.Pair;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;


public interface AirportService {

    //1: Admin Service
    //1.1
    void addSector(final String sector);

    //1.2
    Range addCountersToSector(final String sector, final int amount);

    //1.3
    void addBooking(final String booking, final String flight, final String airline);

    //2: Counter Service
    //2.1
    List<Sector> listSectors();

    //2.2
    List<Range> listCounters(final String sector, final int start, final int end);

    //2.3
    Pair<Optional<Range>, Integer> assignRange(final String sector, final String airline, final List<String> flights, final int count);

    //2.4
    Range freeCounters(final String sector,final int counterFrom,final String airline);

    //2.5
    Pair<List<Passenger>,List<Counter>> checkInCounters(final String sector, final int counterFrom, final String airline);

    //2.6
    List<RequestRange> listPendingAssignments(final String sector);


    //3 CheckIn service
    //3.1
    Flight fetchCounter(final String booking);

    //3.2
    Range addPassengerToQueue(final String booking, final String sector, final int startCounter);

    //3.3
    Passenger checkPassengerStatus(final String booking);

    //4 Notificaciones de Aerolínea
    //4.1
    BlockingQueue<Notification> register(final String airline);

    // 4.2
    void  unregister(final String airline) throws InterruptedException;

    //5 Query service
    //5.1
    List<Range> checkCountersStatus(Optional<String> sector);

    // 5.2
    List<Passenger> queryCheckInHistory(final String sector,final String airline);
}

