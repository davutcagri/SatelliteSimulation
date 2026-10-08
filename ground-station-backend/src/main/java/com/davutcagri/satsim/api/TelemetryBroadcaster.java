package com.davutcagri.satsim.api;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.telemetry.TelemetryPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class TelemetryBroadcaster implements TelemetryPublisher {

    private static final Logger log = LoggerFactory.getLogger(TelemetryBroadcaster.class);

    private final UiSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public TelemetryBroadcaster(UiSessionRegistry sessionRegistry, ObjectMapper objectMapper) {
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(TelemetryPacket packet) {
        TextMessage message = serialize(packet);
        if (message == null) {
            return;
        }
        sessionRegistry.all().forEach(session -> sendTo(session, message));
    }

    private TextMessage serialize(TelemetryPacket packet) {
        try {
            return new TextMessage(objectMapper.writeValueAsString(packet));
        } catch (JsonProcessingException exception) {
            log.warn("Telemetry packet cannot be serialized: {}", exception.getOriginalMessage());
            return null;
        }
    }

    private void sendTo(WebSocketSession session, TextMessage message) {
        if (!session.isOpen()) {
            sessionRegistry.remove(session);
            return;
        }
        try {
            session.sendMessage(message);
        } catch (IOException | RuntimeException exception) {
            log.debug("Dropping UI session {}: {}", session.getId(), exception.getMessage());
            sessionRegistry.remove(session);
            closeQuietly(session);
        }
    }

    private void closeQuietly(WebSocketSession session) {
        try {
            session.close(CloseStatus.SESSION_NOT_RELIABLE);
        } catch (IOException | RuntimeException ignored) {
            return;
        }
    }
}
