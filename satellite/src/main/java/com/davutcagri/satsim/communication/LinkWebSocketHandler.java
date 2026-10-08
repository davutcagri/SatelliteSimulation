package com.davutcagri.satsim.communication;

import com.davutcagri.satsim.link.UplinkMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class LinkWebSocketHandler extends TextWebSocketHandler {

    private final LinkSessionRegistry registry;
    private final UplinkCommandHandler commandHandler;
    private final ObjectMapper objectMapper;

    public LinkWebSocketHandler(LinkSessionRegistry registry,
                                UplinkCommandHandler commandHandler,
                                ObjectMapper objectMapper) {
        this.registry = registry;
        this.commandHandler = commandHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        registry.register(session);
        log.info("Ground station connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        registry.unregister(session.getId());
        log.info("Ground station disconnected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            commandHandler.apply(objectMapper.readValue(message.getPayload(), UplinkMessage.class));
        } catch (Exception exception) {
            log.warn("Ignored invalid uplink message: {}", exception.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("Transport error on session {}: {}", session.getId(), exception.getMessage());
        registry.unregister(session.getId());
    }
}
