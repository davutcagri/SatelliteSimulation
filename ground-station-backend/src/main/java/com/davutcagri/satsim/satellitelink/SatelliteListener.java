package com.davutcagri.satsim.satellitelink;

import com.davutcagri.satsim.link.TelemetryPacket;
import org.springframework.web.socket.CloseStatus;

public interface SatelliteListener {

    void onConnected(SatelliteLink link);

    void onTelemetry(TelemetryPacket packet, String json);

    void onDisconnected(CloseStatus status);
}
