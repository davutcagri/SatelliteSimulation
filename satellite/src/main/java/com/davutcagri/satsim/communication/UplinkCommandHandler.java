package com.davutcagri.satsim.communication;

import com.davutcagri.satsim.autonomy.AutonomySystem;
import com.davutcagri.satsim.engine.SimulationClock;
import com.davutcagri.satsim.link.UplinkMessage;
import com.davutcagri.satsim.power.SolarArray;

public class UplinkCommandHandler {

    private final SimulationClock clock;
    private final AutonomySystem autonomySystem;
    private final SolarArray solarArray;

    public UplinkCommandHandler(SimulationClock clock, AutonomySystem autonomySystem, SolarArray solarArray) {
        this.clock = clock;
        this.autonomySystem = autonomySystem;
        this.solarArray = solarArray;
    }

    public void apply(UplinkMessage message) {
        if (message == null || message.type() == null) {
            throw new IllegalArgumentException("Uplink message has no type");
        }
        switch (message.type()) {
            case PAYLOAD_ON -> autonomySystem.enablePayload();
            case PAYLOAD_OFF -> autonomySystem.disablePayload();
            case SOLAR_ARRAY_FAULT_INJECT -> solarArray.injectFault();
            case SOLAR_ARRAY_FAULT_CLEAR -> solarArray.clearFault();
            case SET_SPEED -> clock.setSpeedMultiplier(requiredSpeed(message));
            case PAUSE -> clock.pause();
            case RESUME -> clock.resume();
        }
    }

    private double requiredSpeed(UplinkMessage message) {
        if (message.speedMultiplier() == null) {
            throw new IllegalArgumentException("SET_SPEED requires a speedMultiplier");
        }
        return message.speedMultiplier();
    }
}
