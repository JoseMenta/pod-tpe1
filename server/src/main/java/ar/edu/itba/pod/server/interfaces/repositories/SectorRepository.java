package ar.edu.itba.pod.server.interfaces.repositories;

import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Sector;

import java.util.Optional;

public interface SectorRepository {
    Optional<Sector> getSectorById(final String sectorId);

    Sector createSector(final String sectorId);
    Sector createSectorIfAbsent(final String sectorId);

}
