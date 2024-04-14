package ar.edu.itba.pod.server.models;

public class Flight {
    private final String code;
    private Range range;
    private static final int CODE_LENGTH = 5;

    public Flight(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException("Length of code must be " + CODE_LENGTH);
        }
        this.code =code;
        this.range= null;
    }

    public synchronized void setRange(Range range) {
        this.range = range;
    }

    public synchronized Range getRange() {
        return this.range;
    }

    public String getCode() {
        return code;
    }
}
