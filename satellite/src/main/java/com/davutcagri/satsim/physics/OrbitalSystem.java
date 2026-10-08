package com.davutcagri.satsim.physics;

import com.davutcagri.satsim.physics.body.Earth;
import com.davutcagri.satsim.physics.body.Satellite;
import com.davutcagri.satsim.physics.body.Sun;
import lombok.Getter;

@Getter
public class OrbitalSystem {

    private final Sun sun;
    private final Earth earth;
    private final Satellite satellite;
    private final Rk4Propagator propagator;

    public OrbitalSystem(Sun sun, Earth earth, Satellite satellite, Rk4Propagator propagator) {
        this.sun = sun;
        this.earth = earth;
        this.satellite = satellite;
        this.propagator = propagator;
    }

    public void advance(double deltaTimeSeconds) {
        propagator.propagate(earth, sun, deltaTimeSeconds);
        propagator.propagate(satellite, earth, deltaTimeSeconds);
    }
}
