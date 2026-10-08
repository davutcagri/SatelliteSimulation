package com.davutcagri.satsim.telemetry;

import com.davutcagri.satsim.link.TelemetryPacket;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LatestTelemetry {

    private final AtomicReference<TelemetryPacket> latest = new AtomicReference<>();

    public void update(TelemetryPacket packet) {
        latest.set(packet);
    }

    public Optional<TelemetryPacket> get() {
        return Optional.ofNullable(latest.get());
    }
}
