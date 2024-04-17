package ar.edu.itba.pod.server.interfaces.repositories;

import ar.edu.itba.pod.server.models.HistoryCheckIn;
import ar.edu.itba.pod.server.models.Sector;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface SectorRepository {
    Optional<Sector> getSectorById(final String sectorId);

    Sector createSector(final String sectorId, final HistoryCheckIn historyCheckIn);

    Sector createSectorIfAbsent(final String sectorId, final HistoryCheckIn historyCheckIn);

    ConcurrentHashMap<String, Sector> getSectors();

}
