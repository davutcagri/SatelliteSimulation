package com.davutcagri.satsim.link;

public record AutonomyStatus(
        SatelliteMode mode,
        boolean payloadEnabled,
        boolean solarArrayFault
) {
}
