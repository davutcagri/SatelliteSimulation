package com.davutcagri.satsim.autonomy;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("satellite.autonomy")
public record AutonomyProperties(
        double powerSavingEntryPercent,
        double safeEntryPercent,
        double safeExitPercent,
        double nominalReturnPercent
) {
}
