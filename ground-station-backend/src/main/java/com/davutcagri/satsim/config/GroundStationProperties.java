package com.davutcagri.satsim.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties("groundstation")
public record GroundStationProperties(
        @DefaultValue("ws://localhost:8081/link") URI satelliteUrl,
        @DefaultValue("http://localhost:5173") String allowedOrigin,
        @DefaultValue("2s") Duration reconnectInterval,
        @DefaultValue("2s") Duration connectTimeout,
        @DefaultValue History history
) {

    public record History(
            @DefaultValue("10") double sampleIntervalSeconds,
            @DefaultValue("2000") int capacity
    ) {
    }
}
