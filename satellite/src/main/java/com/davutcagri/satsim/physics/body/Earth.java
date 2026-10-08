package com.davutcagri.satsim.physics.body;

import com.davutcagri.satsim.link.Vector3D;

public class Earth extends SpaceObject {

    public static final double RADIUS_METERS = 6_371_000;
    public static final double MASS_KILOGRAMS = 5.9719e24;
    public static final double DISTANCE_FROM_SUN_METERS = 1.495978707e11;
    public static final double ORBITAL_SPEED_METERS_PER_SECOND = 29_780;

    public Earth() {
        super(
                "Earth",
                RADIUS_METERS,
                MASS_KILOGRAMS,
                new Vector3D(DISTANCE_FROM_SUN_METERS, 0, 0),
                new Vector3D(0, ORBITAL_SPEED_METERS_PER_SECOND, 0)
        );
    }
}
