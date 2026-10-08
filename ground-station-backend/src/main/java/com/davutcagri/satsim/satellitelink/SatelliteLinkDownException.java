package com.davutcagri.satsim.satellitelink;

public class SatelliteLinkDownException extends RuntimeException {

    public SatelliteLinkDownException(String message) {
        super(message);
    }

    public SatelliteLinkDownException(String message, Throwable cause) {
        super(message, cause);
    }
}
