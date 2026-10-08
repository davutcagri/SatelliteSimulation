package com.davutcagri.satsim.session;

import com.davutcagri.satsim.config.GroundStationProperties;
import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.satellitelink.SatelliteConnector;
import com.davutcagri.satsim.satellitelink.SatelliteLink;
import com.davutcagri.satsim.satellitelink.SatelliteListener;
import com.davutcagri.satsim.telemetry.TelemetryHistory;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GroundStationSessionRegistry {

    private static final Logger log = LoggerFactory.getLogger(GroundStationSessionRegistry.class);

    private final GroundStationProperties properties;
    private final SatelliteConnector connector;
    private final Map<String, GroundStationSession> sessions = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(
            runnable -> new Thread(runnable, "session-expiry"));

    public GroundStationSessionRegistry(GroundStationProperties properties, SatelliteConnector connector) {
        this.properties = properties;
        this.connector = connector;
    }

    public boolean attach(String sessionId, WebSocketSession uiSocket) {
        GroundStationSession created = null;
        synchronized (this) {
            GroundStationSession session = sessions.get(sessionId);
            if (session == null) {
                if (sessions.size() >= properties.maxSessions()) {
                    log.info("Rejected session {}: {} sessions active", sessionId, sessions.size());
                    return false;
                }
                session = new GroundStationSession(sessionId, new TelemetryHistory(properties.history()));
                sessions.put(sessionId, session);
                created = session;
            }
            session.cancelExpiry();
            session.addUiSocket(uiSocket);
        }
        if (created != null) {
            log.info("Opened session {}", sessionId);
            connectSatellite(created);
        }
        return true;
    }

    public synchronized void detach(String sessionId, WebSocketSession uiSocket) {
        GroundStationSession session = sessions.get(sessionId);
        if (session == null) {
            return;
        }
        session.removeUiSocket(uiSocket.getId());
        if (!session.hasUiSockets()) {
            long delayMillis = properties.sessionIdleTimeout().toMillis();
            session.scheduleExpiry(scheduler.schedule(() -> expire(session), delayMillis, TimeUnit.MILLISECONDS));
        }
    }

    public synchronized Optional<GroundStationSession> find(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @PreDestroy
    void stop() {
        scheduler.shutdownNow();
        List<GroundStationSession> active;
        synchronized (this) {
            active = List.copyOf(sessions.values());
            sessions.clear();
        }
        active.forEach(session -> session.close(CloseStatus.GOING_AWAY));
    }

    private void connectSatellite(GroundStationSession session) {
        connector.connect(new SessionSatelliteListener(session)).whenComplete((ignored, error) -> {
            if (error != null) {
                log.warn("Satellite connection for session {} failed: {}", session.id(), error.toString());
                remove(session, SessionCloseStatus.SATELLITE_UNAVAILABLE);
            }
        });
    }

    private void expire(GroundStationSession session) {
        synchronized (this) {
            if (session.hasUiSockets()) {
                return;
            }
        }
        remove(session, CloseStatus.NORMAL);
    }

    private void remove(GroundStationSession session, CloseStatus uiStatus) {
        synchronized (this) {
            if (!sessions.remove(session.id(), session)) {
                return;
            }
            session.cancelExpiry();
        }
        log.info("Closed session {}", session.id());
        session.close(uiStatus);
    }

    private final class SessionSatelliteListener implements SatelliteListener {

        private final GroundStationSession session;

        private SessionSatelliteListener(GroundStationSession session) {
            this.session = session;
        }

        @Override
        public void onConnected(SatelliteLink link) {
            synchronized (GroundStationSessionRegistry.this) {
                if (sessions.get(session.id()) == session) {
                    session.attachSatellite(link);
                    return;
                }
            }
            link.close();
        }

        @Override
        public void onTelemetry(TelemetryPacket packet, String json) {
            session.receive(packet, json);
        }

        @Override
        public void onDisconnected(CloseStatus status) {
            boolean satelliteFull = status.getCode() == CloseStatus.SERVICE_OVERLOAD.getCode();
            remove(session, satelliteFull ? SessionCloseStatus.CAPACITY_REACHED : SessionCloseStatus.SATELLITE_UNAVAILABLE);
        }
    }
}
