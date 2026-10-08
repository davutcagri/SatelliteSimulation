package com.davutcagri.satsim.link;

public record TelemetryPacket(
        ClockStatus clock,
        OrbitStatus orbit,
        PowerStatus power,
        AutonomyStatus autonomy
) {
}
