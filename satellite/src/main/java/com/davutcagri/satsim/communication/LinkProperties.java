package com.davutcagri.satsim.communication;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("satellite.link")
public record LinkProperties(long publishIntervalMillis) {
}
