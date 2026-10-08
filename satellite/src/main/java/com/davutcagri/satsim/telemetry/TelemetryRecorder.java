package com.davutcagri.satsim.telemetry;

import com.davutcagri.satsim.link.TelemetryPacket;

import java.util.concurrent.atomic.AtomicReference;

public class TelemetryRecorder {

    private final TelemetryAssembler assembler;
    private final AtomicReference<TelemetryPacket> latestPacket = new AtomicReference<>();

    public TelemetryRecorder(TelemetryAssembler assembler) {
        this.assembler = assembler;
    }

    public void record() {
        latestPacket.set(assembler.assemble());
    }

    public TelemetryPacket latest() {
        return latestPacket.get();
    }
}
