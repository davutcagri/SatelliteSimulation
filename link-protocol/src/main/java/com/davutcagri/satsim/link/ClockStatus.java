package com.davutcagri.satsim.link;

public record ClockStatus(
        double simulationTimeSeconds,
        double speedMultiplier,
        boolean paused
) {
}
