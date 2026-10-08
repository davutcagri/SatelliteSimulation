package com.davutcagri.satsim.communication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("satellite.link")
public record LinkProperties(
        @DefaultValue("100") long publishIntervalMillis,
        @DefaultValue("3") int maxSimulations
) {
}
