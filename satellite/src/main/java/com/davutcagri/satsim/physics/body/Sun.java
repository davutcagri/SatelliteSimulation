package com.davutcagri.satsim.physics.body;

import com.davutcagri.satsim.link.Vector3D;

public class Sun extends SpaceObject {

    public static final double RADIUS_METERS = 696_340_000.0;
    public static final double MASS_KILOGRAMS = 1.989e30;

    public Sun() {
        super("Sun", RADIUS_METERS, MASS_KILOGRAMS, Vector3D.ZERO, Vector3D.ZERO);
    }
}
