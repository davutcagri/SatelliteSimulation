package com.davutcagri.satsim.engine;

import com.davutcagri.satsim.autonomy.AutonomySystem;
import com.davutcagri.satsim.environment.SpaceEnvironment;
import com.davutcagri.satsim.physics.OrbitalSystem;
import com.davutcagri.satsim.power.PowerSystem;

public class SatelliteSimulationStep implements SimulationStep {

    private final OrbitalSystem orbitalSystem;
    private final SpaceEnvironment environment;
    private final PowerSystem powerSystem;
    private final AutonomySystem autonomySystem;

    public SatelliteSimulationStep(OrbitalSystem orbitalSystem,
                                   SpaceEnvironment environment,
                                   PowerSystem powerSystem,
                                   AutonomySystem autonomySystem) {
        this.orbitalSystem = orbitalSystem;
        this.environment = environment;
        this.powerSystem = powerSystem;
        this.autonomySystem = autonomySystem;
    }

    @Override
    public void advance(double deltaTimeSeconds) {
        orbitalSystem.advance(deltaTimeSeconds);
        environment.update();
        powerSystem.advance(
                environment.isSunlit(),
                autonomySystem.mode(),
                autonomySystem.isPayloadEnabled(),
                deltaTimeSeconds
        );
        autonomySystem.evaluate(powerSystem.batteryChargePercent());
    }
}
