package com.davutcagri.satsim.config;

import com.davutcagri.satsim.autonomy.AutonomyProperties;
import com.davutcagri.satsim.power.LoadProperties;
import com.davutcagri.satsim.power.PowerProperties;
import com.davutcagri.satsim.simulation.SimulationFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PowerProperties.class, LoadProperties.class, AutonomyProperties.class})
public class SimulationConfiguration {

    @Bean
    public SimulationFactory simulationFactory(PowerProperties powerProperties,
                                               LoadProperties loadProperties,
                                               AutonomyProperties autonomyProperties) {
        return new SimulationFactory(powerProperties, loadProperties, autonomyProperties);
    }
}
