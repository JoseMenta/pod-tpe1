package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.grpc.admin.RangeRequest;
import ar.edu.itba.pod.server.exceptions.InvalidRangeException;
import ar.edu.itba.pod.server.exceptions.InvalidSectorException;
import ar.edu.itba.pod.server.interfaces.repositories.*;
import ar.edu.itba.pod.server.interfaces.services.AirportService;
import ar.edu.itba.pod.server.models.*;
import ar.edu.itba.pod.server.models.ds.Pair;
import ar.edu.itba.pod.server.repositories.*;

import java.util.List;
import java.util.Optional;

public class AirportServiceImpl implements AirportService {

    final AirlineRepository airlineRepository;
    final FlightRepository flightRepository;
    final PassengerRepository passengerRepository;
    final RangeRepository rangeRepository;
    final SectorRepository sectorRepository;

    public AirportServiceImpl(){
        this.airlineRepository = new AirlineRepositoryImpl();
        this.flightRepository = new FlightRepositoryImpl();
        this.passengerRepository = new PassengerRepositoryImpl();
        this.rangeRepository = new RangeRepositoryImpl();
        this.sectorRepository = new SectorRepositoryImpl();
    }
    @Override
    public void addSector(String sector) {

    }

    @Override
    public Range addCountersToSector(String sector, int amount) {
        Optional<Sector> sectorData = sectorRepository.getSectorById(sector);
        if(sectorData.isEmpty()){
            throw new InvalidSectorException();
        }
        if(amount <= 0){
            throw new InvalidRangeException();
        }
        return rangeRepository.createRange(amount, sectorData.get());
    }

    @Override
    public void addBooking(String booking, String flight, String airline) {

    }

    @Override
    public List<Sector> listSectors() {
        return null;
    }

    @Override
    public List<Range> listCounters(String sector, int start, int end) {
        return null;
    }

    @Override
    public Pair<Range, Integer> assignRange(String sector, String airline, List<String> flights, int count) {
        return null;
    }

    @Override
    public void freeCounters(String sector, int counterFrom, String airline) {

    }

    @Override
    public Pair<List<Passenger>, List<Counter>> checkInCounters(String sector, int counterFrom, String airline) {
        return null;
    }

    @Override
    public List<RangeRequest> listPendingAssignments(String sector) {
        return null;
    }

    @Override
    public Flight fetchCounter(String booking) {
        return null;
    }

    @Override
    public Range addPassengerToQueue(String booking, String sector, int startCounter) {
        return null;
    }

    @Override
    public Passenger checkPassengerStatus(String booking) {
        return null;
    }

    @Override
    public void register(String airline) {

    }

    @Override
    public void unregister(String airline) {

    }

    @Override
    public List<Range> checkCountersStatus(Optional<String> sector) {
        return null;
    }

    @Override
    public List<Passenger> queryCheckInHistory(Optional<String> sector, Optional<String> airline) {
        return null;
    }
}
