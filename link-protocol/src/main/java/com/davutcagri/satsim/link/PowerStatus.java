package com.davutcagri.satsim.link;

public record PowerStatus(
        double solarGenerationWatts,
        double loadWatts,
        double batteryChargePercent
) {
}
