package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.interfaces.repositories.RangeRepository;
import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.Sector;

import java.util.concurrent.atomic.AtomicInteger;

public class RangeRepositoryImpl implements RangeRepository {
    private final AtomicInteger lastId = new AtomicInteger();



    // TODO wait the implementation of range
    @Override
    public Range createRange(final int quantity, final Sector sector) {
        lastId.addAndGet(quantity);
        return new Range();
    }
}
