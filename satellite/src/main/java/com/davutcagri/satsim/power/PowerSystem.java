package com.davutcagri.satsim.power;

import com.davutcagri.satsim.link.PowerStatus;
import com.davutcagri.satsim.link.SatelliteMode;

public class PowerSystem {

    private final SolarArray solarArray;
    private final Battery battery;
    private final LoadProfile loadProfile;
    private double generationWatts;
    private double loadWatts;

    public PowerSystem(SolarArray solarArray, Battery battery, LoadProfile loadProfile) {
        this.solarArray = solarArray;
        this.battery = battery;
        this.loadProfile = loadProfile;
    }

    public void advance(boolean sunlit, SatelliteMode mode, boolean payloadEnabled, double deltaTimeSeconds) {
        generationWatts = solarArray.generationWatts(sunlit);
        loadWatts = loadProfile.totalWatts(mode, payloadEnabled);
        battery.exchange(generationWatts - loadWatts, deltaTimeSeconds);
    }

    public double batteryChargePercent() {
        return battery.chargePercent();
    }

    public PowerStatus status() {
        return new PowerStatus(generationWatts, loadWatts, battery.chargePercent());
    }
}
