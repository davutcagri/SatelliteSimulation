package com.davutcagri.satsim.power;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("satellite.loads")
public record LoadProperties(
        double nominalBusWatts,
        double powerSavingBusWatts,
        double safeBusWatts,
        double payloadWatts
) {
}
