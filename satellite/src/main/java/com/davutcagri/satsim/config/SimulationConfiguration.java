package com.davutcagri.satsim.config;

import com.davutcagri.satsim.autonomy.AutonomyProperties;
import com.davutcagri.satsim.autonomy.AutonomySystem;
import com.davutcagri.satsim.engine.SatelliteSimulationStep;
import com.davutcagri.satsim.engine.SimulationClock;
import com.davutcagri.satsim.engine.SimulationEngine;
import com.davutcagri.satsim.engine.SimulationRunner;
import com.davutcagri.satsim.environment.CylindricalShadowModel;
import com.davutcagri.satsim.environment.SpaceEnvironment;
import com.davutcagri.satsim.physics.OrbitalSystem;
import com.davutcagri.satsim.physics.Rk4Propagator;
import com.davutcagri.satsim.physics.body.Earth;
import com.davutcagri.satsim.physics.body.Satellite;
import com.davutcagri.satsim.physics.body.Sun;
import com.davutcagri.satsim.power.*;
import com.davutcagri.satsim.telemetry.TelemetryAssembler;
import com.davutcagri.satsim.telemetry.TelemetryRecorder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PowerProperties.class, LoadProperties.class, AutonomyProperties.class})
public class SimulationConfiguration {

    @Bean
    public SimulationClock simulationClock() {
        return new SimulationClock();
    }

    @Bean
    public OrbitalSystem orbitalSystem() {
        return new OrbitalSystem(new Sun(), new Earth(), new Satellite(), new Rk4Propagator());
    }

    @Bean
    public SpaceEnvironment spaceEnvironment(OrbitalSystem orbitalSystem) {
        return new SpaceEnvironment(orbitalSystem, new CylindricalShadowModel());
    }

    @Bean
    public SolarArray solarArray(PowerProperties properties) {
        return new SolarArray(properties);
    }

    @Bean
    public PowerSystem powerSystem(SolarArray solarArray, PowerProperties powerProperties, LoadProperties loadProperties) {
        return new PowerSystem(solarArray, new Battery(powerProperties), new LoadProfile(loadProperties));
    }

    @Bean
    public AutonomySystem autonomySystem(AutonomyProperties properties) {
        return new AutonomySystem(properties);
    }

    @Bean
    public TelemetryRecorder telemetryRecorder(SimulationClock clock,
                                               OrbitalSystem orbitalSystem,
                                               SpaceEnvironment environment,
                                               PowerSystem powerSystem,
                                               SolarArray solarArray,
                                               AutonomySystem autonomySystem) {
        TelemetryAssembler assembler = new TelemetryAssembler(
                clock, orbitalSystem, environment, powerSystem, solarArray, autonomySystem);
        TelemetryRecorder recorder = new TelemetryRecorder(assembler);
        recorder.record();
        return recorder;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SimulationRunner simulationRunner(SimulationClock clock,
                                             OrbitalSystem orbitalSystem,
                                             SpaceEnvironment environment,
                                             PowerSystem powerSystem,
                                             AutonomySystem autonomySystem,
                                             TelemetryRecorder recorder) {
        SatelliteSimulationStep step = new SatelliteSimulationStep(
                orbitalSystem, environment, powerSystem, autonomySystem);
        return new SimulationRunner(new SimulationEngine(clock, step), recorder::record);
    }
}
