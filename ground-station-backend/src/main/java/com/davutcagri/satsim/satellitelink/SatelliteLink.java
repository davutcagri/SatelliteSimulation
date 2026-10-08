package com.davutcagri.satsim.satellitelink;

import com.davutcagri.satsim.link.UplinkMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;

public class SatelliteLink {

    private static final int SEND_TIME_LIMIT_MILLIS = 5_000;
    private static final int SEND_BUFFER_LIMIT_BYTES = 64 * 1024;

    private final WebSocketSession session;
    private final ObjectMapper objectMapper;

    public SatelliteLink(WebSocketSession session, ObjectMapper objectMapper) {
        this.session = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT_MILLIS, SEND_BUFFER_LIMIT_BYTES);
        this.objectMapper = objectMapper;
    }

    public boolean isConnected() {
        return session.isOpen();
    }

    public void send(UplinkMessage message) {
        if (!session.isOpen()) {
            throw new SatelliteLinkDownException("Satellite link is down");
        }
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Uplink message cannot be serialized", exception);
        } catch (IOException | RuntimeException exception) {
            throw new SatelliteLinkDownException("Uplink message could not be sent", exception);
        }
    }

    public void close() {
        try {
            session.close();
        } catch (IOException ignored) {
            return;
        }
    }
}
