package com.davutcagri.satsim.power;

import com.davutcagri.satsim.link.SatelliteMode;

public class LoadProfile {

    private final LoadProperties properties;

    public LoadProfile(LoadProperties properties) {
        this.properties = properties;
    }

    public double totalWatts(SatelliteMode mode, boolean payloadEnabled) {
        double payloadWatts = payloadEnabled ? properties.payloadWatts() : 0;
        return busWatts(mode) + payloadWatts;
    }

    private double busWatts(SatelliteMode mode) {
        return switch (mode) {
            case NOMINAL -> properties.nominalBusWatts();
            case POWER_SAVING -> properties.powerSavingBusWatts();
            case SAFE -> properties.safeBusWatts();
        };
    }
}
