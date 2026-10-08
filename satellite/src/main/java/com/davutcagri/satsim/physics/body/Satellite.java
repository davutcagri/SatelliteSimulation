package com.davutcagri.satsim.physics.body;

import com.davutcagri.satsim.link.Vector3D;

public class Satellite extends SpaceObject {

    public static final double RADIUS_METERS = 55;
    public static final double MASS_KILOGRAMS = 420_000;
    public static final double ORBIT_ALTITUDE_METERS = 427_000;
    public static final double ORBITAL_SPEED_METERS_PER_SECOND = 7_660;

    public Satellite() {
        super(
                "Satellite",
                RADIUS_METERS,
                MASS_KILOGRAMS,
                new Vector3D(0, 0, Earth.RADIUS_METERS + ORBIT_ALTITUDE_METERS),
                new Vector3D(ORBITAL_SPEED_METERS_PER_SECOND, 0, 0)
        );
    }
}
