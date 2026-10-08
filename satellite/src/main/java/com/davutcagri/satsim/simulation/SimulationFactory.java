package com.davutcagri.satsim.simulation;

import com.davutcagri.satsim.autonomy.AutonomyProperties;
import com.davutcagri.satsim.autonomy.AutonomySystem;
import com.davutcagri.satsim.communication.UplinkCommandHandler;
import com.davutcagri.satsim.engine.SatelliteSimulationStep;
import com.davutcagri.satsim.engine.SimulationClock;
import com.davutcagri.satsim.engine.SimulationEngine;
import com.davutcagri.satsim.environment.CylindricalShadowModel;
import com.davutcagri.satsim.environment.SpaceEnvironment;
import com.davutcagri.satsim.physics.OrbitalSystem;
import com.davutcagri.satsim.physics.Rk4Propagator;
import com.davutcagri.satsim.physics.body.Earth;
import com.davutcagri.satsim.physics.body.Satellite;
import com.davutcagri.satsim.physics.body.Sun;
import com.davutcagri.satsim.power.Battery;
import com.davutcagri.satsim.power.LoadProfile;
import com.davutcagri.satsim.power.LoadProperties;
import com.davutcagri.satsim.power.PowerProperties;
import com.davutcagri.satsim.power.PowerSystem;
import com.davutcagri.satsim.power.SolarArray;
import com.davutcagri.satsim.telemetry.TelemetryAssembler;
import com.davutcagri.satsim.telemetry.TelemetryRecorder;

public class SimulationFactory {

    private final PowerProperties powerProperties;
    private final LoadProperties loadProperties;
    private final AutonomyProperties autonomyProperties;

    public SimulationFactory(PowerProperties powerProperties,
                             LoadProperties loadProperties,
                             AutonomyProperties autonomyProperties) {
        this.powerProperties = powerProperties;
        this.loadProperties = loadProperties;
        this.autonomyProperties = autonomyProperties;
    }

    public SimulationInstance create() {
        SimulationClock clock = new SimulationClock();
        OrbitalSystem orbitalSystem = new OrbitalSystem(new Sun(), new Earth(), new Satellite(), new Rk4Propagator());
        SpaceEnvironment environment = new SpaceEnvironment(orbitalSystem, new CylindricalShadowModel());
        SolarArray solarArray = new SolarArray(powerProperties);
        PowerSystem powerSystem = new PowerSystem(solarArray, new Battery(powerProperties), new LoadProfile(loadProperties));
        AutonomySystem autonomySystem = new AutonomySystem(autonomyProperties);

        TelemetryRecorder recorder = new TelemetryRecorder(new TelemetryAssembler(
                clock, orbitalSystem, environment, powerSystem, solarArray, autonomySystem));
        recorder.record();

        SimulationEngine engine = new SimulationEngine(clock, new SatelliteSimulationStep(
                orbitalSystem, environment, powerSystem, autonomySystem));
        UplinkCommandHandler commandHandler = new UplinkCommandHandler(clock, autonomySystem, solarArray);
        return new SimulationInstance(engine, recorder, commandHandler);
    }
}
