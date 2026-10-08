package com.davutcagri.satsim.api;

import com.davutcagri.satsim.session.GroundStationSessionRegistry;
import com.davutcagri.satsim.session.SessionCloseStatus;
import com.davutcagri.satsim.session.SessionIds;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Optional;

@Component
public class TelemetrySocketHandler extends TextWebSocketHandler {

    private final GroundStationSessionRegistry sessionRegistry;

    public TelemetrySocketHandler(GroundStationSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession socket) {
        Optional<String> sessionId = SessionIds.fromUri(socket.getUri());
        if (sessionId.isEmpty()) {
            closeQuietly(socket, SessionCloseStatus.INVALID_SESSION);
            return;
        }
        if (!sessionRegistry.attach(sessionId.get(), socket)) {
            closeQuietly(socket, SessionCloseStatus.CAPACITY_REACHED);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession socket, CloseStatus status) {
        detach(socket);
    }

    @Override
    public void handleTransportError(WebSocketSession socket, Throwable exception) {
        detach(socket);
    }

    private void detach(WebSocketSession socket) {
        SessionIds.fromUri(socket.getUri()).ifPresent(sessionId -> sessionRegistry.detach(sessionId, socket));
    }

    private void closeQuietly(WebSocketSession socket, CloseStatus status) {
        try {
            socket.close(status);
        } catch (IOException ignored) {
            return;
        }
    }
}
