package com.davutcagri.satsim.config;

import com.davutcagri.satsim.communication.LinkProperties;
import com.davutcagri.satsim.communication.LinkSessionRegistry;
import com.davutcagri.satsim.communication.LinkWebSocketHandler;
import com.davutcagri.satsim.communication.TelemetryBroadcaster;
import com.davutcagri.satsim.simulation.SimulationFactory;
import com.davutcagri.satsim.simulation.SimulationRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LinkProperties.class)
public class LinkConfiguration {

    @Bean
    public LinkSessionRegistry linkSessionRegistry(LinkProperties properties) {
        return new LinkSessionRegistry(properties.maxSimulations());
    }

    @Bean
    public LinkWebSocketHandler linkWebSocketHandler(LinkSessionRegistry registry,
                                                     SimulationFactory simulationFactory,
                                                     ObjectMapper objectMapper) {
        return new LinkWebSocketHandler(registry, simulationFactory, objectMapper);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public TelemetryBroadcaster telemetryBroadcaster(LinkSessionRegistry registry,
                                                     ObjectMapper objectMapper,
                                                     LinkProperties properties) {
        return new TelemetryBroadcaster(registry, objectMapper, properties);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SimulationRunner simulationRunner(LinkSessionRegistry registry) {
        return new SimulationRunner(registry::simulations);
    }
}
