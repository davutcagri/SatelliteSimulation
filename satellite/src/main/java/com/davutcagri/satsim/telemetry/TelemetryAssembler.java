package com.davutcagri.satsim.telemetry;

import com.davutcagri.satsim.autonomy.AutonomySystem;
import com.davutcagri.satsim.engine.SimulationClock;
import com.davutcagri.satsim.environment.SpaceEnvironment;
import com.davutcagri.satsim.link.AutonomyStatus;
import com.davutcagri.satsim.link.ClockStatus;
import com.davutcagri.satsim.link.OrbitStatus;
import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.physics.OrbitalSystem;
import com.davutcagri.satsim.physics.body.Earth;
import com.davutcagri.satsim.physics.body.Satellite;
import com.davutcagri.satsim.power.PowerSystem;
import com.davutcagri.satsim.power.SolarArray;

public class TelemetryAssembler {

    private final SimulationClock clock;
    private final OrbitalSystem orbitalSystem;
    private final SpaceEnvironment environment;
    private final PowerSystem powerSystem;
    private final SolarArray solarArray;
    private final AutonomySystem autonomySystem;

    public TelemetryAssembler(SimulationClock clock,
                              OrbitalSystem orbitalSystem,
                              SpaceEnvironment environment,
                              PowerSystem powerSystem,
                              SolarArray solarArray,
                              AutonomySystem autonomySystem) {
        this.clock = clock;
        this.orbitalSystem = orbitalSystem;
        this.environment = environment;
        this.powerSystem = powerSystem;
        this.solarArray = solarArray;
        this.autonomySystem = autonomySystem;
    }

    public TelemetryPacket assemble() {
        return new TelemetryPacket(clockStatus(), orbitStatus(), powerSystem.status(), autonomyStatus());
    }

    private ClockStatus clockStatus() {
        return new ClockStatus(clock.elapsedSeconds(), clock.getSpeedMultiplier(), clock.isPaused());
    }

    private OrbitStatus orbitStatus() {
        Earth earth = orbitalSystem.getEarth();
        Satellite satellite = orbitalSystem.getSatellite();
        return new OrbitStatus(
                earth.getPosition(),
                satellite.getPosition(),
                satellite.getVelocity(),
                environment.sunDirection(),
                satellite.getPosition().magnitude() - earth.getRadiusMeters(),
                satellite.getVelocity().magnitude(),
                environment.isSunlit()
        );
    }

    private AutonomyStatus autonomyStatus() {
        return new AutonomyStatus(autonomySystem.mode(), autonomySystem.isPayloadEnabled(), solarArray.isFaulty());
    }
}
