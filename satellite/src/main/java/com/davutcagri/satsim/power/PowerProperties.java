package com.davutcagri.satsim.power;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("satellite.power")
public record PowerProperties(
        double solarArrayPeakWatts,
        double faultOutputFraction,
        double batteryCapacityWattHours,
        double initialBatteryChargePercent
) {
}
