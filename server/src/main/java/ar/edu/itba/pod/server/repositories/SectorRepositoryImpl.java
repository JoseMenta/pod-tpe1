package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.exceptions.PassengerAlreadyExistsException;
import ar.edu.itba.pod.server.exceptions.SectorAlreadyExistsException;
import ar.edu.itba.pod.server.interfaces.repositories.SectorRepository;
import ar.edu.itba.pod.server.models.Passenger;
import ar.edu.itba.pod.server.models.Sector;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SectorRepositoryImpl implements SectorRepository {

    private final ConcurrentHashMap<String,Sector> sectors = new ConcurrentHashMap<>();
    @Override
    public Optional<Sector> getSectorById(String sectorId) {
        return Optional.ofNullable(sectors.get(sectorId));
    }

    @Override
    public Sector createSector(String sectorId) {
        Sector sector = new Sector();
        Sector possibleSector =  sectors.putIfAbsent(sectorId,sector);
        if (possibleSector != null)
            throw new SectorAlreadyExistsException();
        return sector;
    }

    @Override
    public Sector createSectorIfAbsent(String sectorId) {
        Sector sector = new Sector();
        return Optional.ofNullable(sectors.putIfAbsent(sectorId,sector)).orElse(sector);
    }
}
