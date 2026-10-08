package com.davutcagri.satsim.simulation;

import com.davutcagri.satsim.communication.UplinkCommandHandler;
import com.davutcagri.satsim.engine.SimulationEngine;
import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.link.UplinkMessage;
import com.davutcagri.satsim.telemetry.TelemetryRecorder;

public class SimulationInstance {

    private final SimulationEngine engine;
    private final TelemetryRecorder recorder;
    private final UplinkCommandHandler commandHandler;

    SimulationInstance(SimulationEngine engine, TelemetryRecorder recorder, UplinkCommandHandler commandHandler) {
        this.engine = engine;
        this.recorder = recorder;
        this.commandHandler = commandHandler;
    }

    public void update(double realDeltaSeconds) {
        engine.update(realDeltaSeconds);
        recorder.record();
    }

    public TelemetryPacket latest() {
        return recorder.latest();
    }

    public void apply(UplinkMessage message) {
        commandHandler.apply(message);
    }
}
