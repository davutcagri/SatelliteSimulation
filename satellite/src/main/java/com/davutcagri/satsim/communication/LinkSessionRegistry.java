package com.davutcagri.satsim.communication;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinkSessionRegistry {

    private static final int SEND_TIME_LIMIT_MILLIS = 1_000;
    private static final int BUFFER_SIZE_LIMIT_BYTES = 512 * 1024;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(WebSocketSession session) {
        sessions.put(session.getId(), new ConcurrentWebSocketSessionDecorator(
                session, SEND_TIME_LIMIT_MILLIS, BUFFER_SIZE_LIMIT_BYTES));
    }

    public void unregister(String sessionId) {
        sessions.remove(sessionId);
    }

    public Collection<WebSocketSession> sessions() {
        return sessions.values();
    }
}
