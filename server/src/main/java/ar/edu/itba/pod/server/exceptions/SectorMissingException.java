package ar.edu.itba.pod.server.exceptions;

public class SectorMissingException extends RuntimeException {

    public SectorMissingException(String sectorName) {
        super("Sector " + sectorName + " does not exist");
    }
}
