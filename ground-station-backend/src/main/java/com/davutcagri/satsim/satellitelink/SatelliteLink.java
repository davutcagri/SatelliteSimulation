package com.davutcagri.satsim.satellitelink;

import com.davutcagri.satsim.link.UplinkMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

@Component
public class SatelliteLink {

    private static final int SEND_TIME_LIMIT_MILLIS = 5_000;
    private static final int SEND_BUFFER_LIMIT_BYTES = 64 * 1024;

    private final ObjectMapper objectMapper;
    private final AtomicReference<WebSocketSession> session = new AtomicReference<>();

    public SatelliteLink(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void attach(WebSocketSession connected) {
        session.set(new ConcurrentWebSocketSessionDecorator(
                connected, SEND_TIME_LIMIT_MILLIS, SEND_BUFFER_LIMIT_BYTES));
    }

    public void detach(WebSocketSession closed) {
        session.updateAndGet(current ->
                current != null && current.getId().equals(closed.getId()) ? null : current);
    }

    public boolean isConnected() {
        WebSocketSession current = session.get();
        return current != null && current.isOpen();
    }

    public void send(UplinkMessage message) {
        WebSocketSession current = session.get();
        if (current == null || !current.isOpen()) {
            throw new SatelliteLinkDownException("Satellite link is down");
        }
        try {
            current.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Uplink message cannot be serialized", exception);
        } catch (IOException | RuntimeException exception) {
            throw new SatelliteLinkDownException("Uplink message could not be sent", exception);
        }
    }

    public void close() {
        WebSocketSession current = session.getAndSet(null);
        if (current != null) {
            try {
                current.close();
            } catch (IOException ignored) {
                return;
            }
        }
    }
}
