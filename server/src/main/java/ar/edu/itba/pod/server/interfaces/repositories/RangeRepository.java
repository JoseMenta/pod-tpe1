package ar.edu.itba.pod.server.interfaces.repositories;

import ar.edu.itba.pod.server.models.Flight;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.Sector;

import java.util.Optional;

public interface RangeRepository {

    Range createRange(final int quantity, final Sector sector);
}
