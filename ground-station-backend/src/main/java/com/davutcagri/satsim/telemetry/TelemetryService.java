package com.davutcagri.satsim.telemetry;

import com.davutcagri.satsim.link.TelemetryPacket;
import org.springframework.stereotype.Service;

@Service
public class TelemetryService {

    private final LatestTelemetry latestTelemetry;
    private final TelemetryHistory history;
    private final TelemetryPublisher publisher;

    public TelemetryService(LatestTelemetry latestTelemetry,
                            TelemetryHistory history,
                            TelemetryPublisher publisher) {
        this.latestTelemetry = latestTelemetry;
        this.history = history;
        this.publisher = publisher;
    }

    public void receive(TelemetryPacket packet) {
        latestTelemetry.update(packet);
        history.record(packet);
        publisher.publish(packet);
    }
}
