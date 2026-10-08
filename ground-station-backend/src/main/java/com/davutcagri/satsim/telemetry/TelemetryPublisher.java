package com.davutcagri.satsim.telemetry;

import com.davutcagri.satsim.link.TelemetryPacket;

public interface TelemetryPublisher {

    void publish(TelemetryPacket packet);
}
