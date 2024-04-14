package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.interfaces.repositories.RangeRepository;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.Sector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class RangeRepositoryImpl implements RangeRepository {
    private final AtomicInteger lastId = new AtomicInteger();
    private static final Logger LOGGER = LoggerFactory.getLogger(RangeRepositoryImpl.class);


    // TODO wait the implementation of range
    @Override
    public Range createRange(final int quantity, final Sector sector) {
        LOGGER.info("Create range with quantity {} for the sector {}",quantity,sector);
        lastId.addAndGet(quantity);
        return new Range();
    }
}
