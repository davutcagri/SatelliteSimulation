package com.davutcagri.satsim.session;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.link.UplinkMessage;
import com.davutcagri.satsim.satellitelink.SatelliteLink;
import com.davutcagri.satsim.satellitelink.SatelliteLinkDownException;
import com.davutcagri.satsim.telemetry.TelemetryHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class GroundStationSession {

    private static final Logger log = LoggerFactory.getLogger(GroundStationSession.class);

    private static final int SEND_TIME_LIMIT_MILLIS = 2_000;
    private static final int SEND_BUFFER_LIMIT_BYTES = 256 * 1024;

    private final String id;
    private final TelemetryHistory history;
    private final Map<String, WebSocketSession> uiSockets = new ConcurrentHashMap<>();
    private volatile SatelliteLink satelliteLink;
    private ScheduledFuture<?> expiry;

    GroundStationSession(String id, TelemetryHistory history) {
        this.id = id;
        this.history = history;
    }

    public String id() {
        return id;
    }

    public TelemetryHistory history() {
        return history;
    }

    public boolean isSatelliteConnected() {
        SatelliteLink link = satelliteLink;
        return link != null && link.isConnected();
    }

    public void send(UplinkMessage message) {
        SatelliteLink link = satelliteLink;
        if (link == null) {
            throw new SatelliteLinkDownException("Satellite link is down");
        }
        link.send(message);
    }

    void attachSatellite(SatelliteLink link) {
        satelliteLink = link;
    }

    void addUiSocket(WebSocketSession socket) {
        uiSockets.put(socket.getId(), new ConcurrentWebSocketSessionDecorator(
                socket, SEND_TIME_LIMIT_MILLIS, SEND_BUFFER_LIMIT_BYTES));
    }

    void removeUiSocket(String socketId) {
        uiSockets.remove(socketId);
    }

    boolean hasUiSockets() {
        return !uiSockets.isEmpty();
    }

    void scheduleExpiry(ScheduledFuture<?> scheduled) {
        cancelExpiry();
        expiry = scheduled;
    }

    void cancelExpiry() {
        if (expiry != null) {
            expiry.cancel(false);
            expiry = null;
        }
    }

    void receive(TelemetryPacket packet, String json) {
        history.record(packet);
        TextMessage message = new TextMessage(json);
        uiSockets.values().forEach(socket -> sendTo(socket, message));
    }

    void close(CloseStatus uiStatus) {
        SatelliteLink link = satelliteLink;
        if (link != null) {
            link.close();
        }
        uiSockets.values().forEach(socket -> closeQuietly(socket, uiStatus));
        uiSockets.clear();
    }

    private void sendTo(WebSocketSession socket, TextMessage message) {
        if (!socket.isOpen()) {
            uiSockets.remove(socket.getId());
            return;
        }
        try {
            socket.sendMessage(message);
        } catch (IOException | RuntimeException exception) {
            log.debug("Dropping UI socket {}: {}", socket.getId(), exception.getMessage());
            uiSockets.remove(socket.getId());
            closeQuietly(socket, CloseStatus.SESSION_NOT_RELIABLE);
        }
    }

    private void closeQuietly(WebSocketSession socket, CloseStatus status) {
        try {
            socket.close(status);
        } catch (IOException | RuntimeException ignored) {
            return;
        }
    }
}
