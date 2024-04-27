package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.SectorAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.SectorRepository;
import ar.edu.itba.pod.server.models.HistoryCheckIn;
import ar.edu.itba.pod.server.models.Sector;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SectorRepositoryImpl implements SectorRepository {

    private final ConcurrentHashMap<String,Sector> sectors = new ConcurrentHashMap<>();

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SectorRepositoryImpl.class);
    @Override
    public Optional<Sector> getSectorById(String sectorId) {
        LOGGER.info("Get sector with id {}",sectorId);
        return Optional.ofNullable(sectors.get(sectorId));
    }

    @Override
    public Sector createSector(String sectorId, HistoryCheckIn historyCheckIn) {
        LOGGER.debug("Creating sector with id {}", sectorId);
        Sector sector = new Sector(sectorId, historyCheckIn);
        Sector possibleSector = sectors.putIfAbsent(sectorId,sector);
        if (possibleSector != null) {
            throw new SectorAlreadyExistsException();
        }
        LOGGER.info("Created sector with id {}",sectorId);
        return sector;
    }

    @Override
    public Sector createSectorIfAbsent(String sectorId, HistoryCheckIn historyCheckIn) {
        LOGGER.info("Create sector ifAbsent with id {}",sectorId);
        Sector sector = new Sector(sectorId, historyCheckIn);
        return Optional.ofNullable(sectors.putIfAbsent(sectorId,sector)).orElse(sector);
    }

    public List<Sector> getSectors() {
        return new ArrayList<>(sectors.values());
    }
}
