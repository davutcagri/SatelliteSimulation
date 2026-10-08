package com.davutcagri.satsim.api;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UiSessionRegistry {

    private static final int SEND_TIME_LIMIT_MILLIS = 2_000;
    private static final int SEND_BUFFER_LIMIT_BYTES = 256 * 1024;

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public void add(WebSocketSession session) {
        sessions.add(new ConcurrentWebSocketSessionDecorator(
                session, SEND_TIME_LIMIT_MILLIS, SEND_BUFFER_LIMIT_BYTES));
    }

    public void remove(WebSocketSession session) {
        sessions.removeIf(registered -> registered.getId().equals(session.getId()));
    }

    public Set<WebSocketSession> all() {
        return Set.copyOf(sessions);
    }
}
