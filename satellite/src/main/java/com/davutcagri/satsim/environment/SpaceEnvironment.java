package com.davutcagri.satsim.environment;

import com.davutcagri.satsim.link.Vector3D;
import com.davutcagri.satsim.physics.OrbitalSystem;

public class SpaceEnvironment {

    private final OrbitalSystem orbitalSystem;
    private final CylindricalShadowModel shadowModel;
    private Vector3D sunDirection;
    private boolean sunlit;

    public SpaceEnvironment(OrbitalSystem orbitalSystem, CylindricalShadowModel shadowModel) {
        this.orbitalSystem = orbitalSystem;
        this.shadowModel = shadowModel;
        update();
    }

    public void update() {
        Vector3D earthToSun = orbitalSystem.getSun().getPosition().minus(orbitalSystem.getEarth().getPosition());
        sunDirection = earthToSun.normalized();
        sunlit = !shadowModel.isInShadow(
                orbitalSystem.getSatellite().getPosition(),
                sunDirection,
                orbitalSystem.getEarth().getRadiusMeters()
        );
    }

    public Vector3D sunDirection() {
        return sunDirection;
    }

    public boolean isSunlit() {
        return sunlit;
    }
}
