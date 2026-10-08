package com.davutcagri.satsim.physics.body;

import com.davutcagri.satsim.link.Vector3D;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class SpaceObject {

    private final String name;
    private final double radiusMeters;
    private final double massKilograms;

    @Setter
    private Vector3D position;

    @Setter
    private Vector3D velocity;

    protected SpaceObject(String name,
                          double radiusMeters,
                          double massKilograms,
                          Vector3D position,
                          Vector3D velocity) {
        this.name = name;
        this.radiusMeters = radiusMeters;
        this.massKilograms = massKilograms;
        this.position = position;
        this.velocity = velocity;
    }
}
