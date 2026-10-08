package com.davutcagri.satsim.autonomy;

import com.davutcagri.satsim.link.SatelliteMode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutonomySystem {

    private final AutonomyProperties thresholds;
    private SatelliteMode mode = SatelliteMode.NOMINAL;
    private boolean payloadEnabled;

    public AutonomySystem(AutonomyProperties thresholds) {
        this.thresholds = thresholds;
    }

    public synchronized void evaluate(double batteryChargePercent) {
        SatelliteMode nextMode = nextMode(batteryChargePercent);
        if (nextMode != mode) {
            enter(nextMode);
        }
    }

    public synchronized void enablePayload() {
        if (mode != SatelliteMode.NOMINAL) {
            log.info("Payload enable ignored in {} mode", mode);
            return;
        }
        payloadEnabled = true;
    }

    public synchronized void disablePayload() {
        payloadEnabled = false;
    }

    public synchronized SatelliteMode mode() {
        return mode;
    }

    public synchronized boolean isPayloadEnabled() {
        return payloadEnabled;
    }

    private SatelliteMode nextMode(double batteryChargePercent) {
        return switch (mode) {
            case NOMINAL -> batteryChargePercent < thresholds.powerSavingEntryPercent()
                    ? SatelliteMode.POWER_SAVING
                    : SatelliteMode.NOMINAL;
            case POWER_SAVING -> powerSavingSuccessor(batteryChargePercent);
            case SAFE -> batteryChargePercent >= thresholds.safeExitPercent()
                    ? SatelliteMode.POWER_SAVING
                    : SatelliteMode.SAFE;
        };
    }

    private SatelliteMode powerSavingSuccessor(double batteryChargePercent) {
        if (batteryChargePercent < thresholds.safeEntryPercent()) {
            return SatelliteMode.SAFE;
        }
        if (batteryChargePercent >= thresholds.nominalReturnPercent()) {
            return SatelliteMode.NOMINAL;
        }
        return SatelliteMode.POWER_SAVING;
    }

    private void enter(SatelliteMode nextMode) {
        log.info("Mode change {} -> {}", mode, nextMode);
        mode = nextMode;
        if (nextMode != SatelliteMode.NOMINAL) {
            payloadEnabled = false;
        }
    }
}
