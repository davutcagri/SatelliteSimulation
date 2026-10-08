package com.davutcagri.satsim.config;

import com.davutcagri.satsim.autonomy.AutonomySystem;
import com.davutcagri.satsim.communication.*;
import com.davutcagri.satsim.engine.SimulationClock;
import com.davutcagri.satsim.power.SolarArray;
import com.davutcagri.satsim.telemetry.TelemetryRecorder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LinkProperties.class)
public class LinkConfiguration {

    @Bean
    public LinkSessionRegistry linkSessionRegistry() {
        return new LinkSessionRegistry();
    }

    @Bean
    public UplinkCommandHandler uplinkCommandHandler(SimulationClock clock,
                                                     AutonomySystem autonomySystem,
                                                     SolarArray solarArray) {
        return new UplinkCommandHandler(clock, autonomySystem, solarArray);
    }

    @Bean
    public LinkWebSocketHandler linkWebSocketHandler(LinkSessionRegistry registry,
                                                     UplinkCommandHandler commandHandler,
                                                     ObjectMapper objectMapper) {
        return new LinkWebSocketHandler(registry, commandHandler, objectMapper);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public TelemetryBroadcaster telemetryBroadcaster(TelemetryRecorder recorder,
                                                     LinkSessionRegistry registry,
                                                     ObjectMapper objectMapper,
                                                     LinkProperties properties) {
        return new TelemetryBroadcaster(recorder, registry, objectMapper, properties);
    }
}
