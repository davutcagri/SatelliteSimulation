package com.davutcagri.satsim.power;

public class Battery {

    private static final double SECONDS_PER_HOUR = 3600.0;
    private static final double PERCENT = 100.0;

    private final double capacityWattHours;
    private double storedWattHours;

    public Battery(PowerProperties properties) {
        this.capacityWattHours = properties.batteryCapacityWattHours();
        this.storedWattHours = capacityWattHours * properties.initialBatteryChargePercent() / PERCENT;
    }

    public void exchange(double netWatts, double deltaTimeSeconds) {
        double changedWattHours = storedWattHours + netWatts * deltaTimeSeconds / SECONDS_PER_HOUR;
        storedWattHours = Math.max(0, Math.min(capacityWattHours, changedWattHours));
    }

    public double chargePercent() {
        return storedWattHours / capacityWattHours * PERCENT;
    }
}
