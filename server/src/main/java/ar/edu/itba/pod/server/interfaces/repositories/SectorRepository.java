package ar.edu.itba.pod.server.interfaces.repositories;

import ar.edu.itba.pod.server.models.HistoryCheckIn;
import ar.edu.itba.pod.server.models.Sector;

import java.util.List;
import java.util.Optional;

public interface SectorRepository {
    Optional<Sector> getSectorById(final String sectorId);

    Sector createSector(final String sectorId, final HistoryCheckIn historyCheckIn);

    List<Sector> getSectors();

}
