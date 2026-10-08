package com.davutcagri.satsim.link;

public record OrbitStatus(
        Vector3D earthPositionMeters,
        Vector3D satellitePositionMeters,
        Vector3D satelliteVelocityMetersPerSecond,
        Vector3D sunDirection,
        double altitudeMeters,
        double speedMetersPerSecond,
        boolean sunlit
) {
}
