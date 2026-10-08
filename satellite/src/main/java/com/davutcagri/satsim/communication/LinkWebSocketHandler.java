package com.davutcagri.satsim.communication;

import com.davutcagri.satsim.link.UplinkMessage;
import com.davutcagri.satsim.simulation.SimulationFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
public class LinkWebSocketHandler extends TextWebSocketHandler {

    private final LinkSessionRegistry registry;
    private final SimulationFactory simulationFactory;
    private final ObjectMapper objectMapper;

    public LinkWebSocketHandler(LinkSessionRegistry registry,
                                SimulationFactory simulationFactory,
                                ObjectMapper objectMapper) {
        this.registry = registry;
        this.simulationFactory = simulationFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if (registry.register(session, simulationFactory::create)) {
            log.info("Ground station connected: {} ({} active simulations)", session.getId(), registry.size());
            return;
        }
        log.warn("Rejected ground station {}: simulation limit reached", session.getId());
        closeQuietly(session, CloseStatus.SERVICE_OVERLOAD);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        registry.unregister(session.getId());
        log.info("Ground station disconnected: {} ({} active simulations)", session.getId(), registry.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            UplinkMessage uplink = objectMapper.readValue(message.getPayload(), UplinkMessage.class);
            registry.find(session.getId()).ifPresent(link -> link.simulation().apply(uplink));
        } catch (Exception exception) {
            log.warn("Ignored invalid uplink message: {}", exception.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("Transport error on session {}: {}", session.getId(), exception.getMessage());
        registry.unregister(session.getId());
    }

    private void closeQuietly(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
        } catch (IOException exception) {
            log.debug("Closing session {} failed: {}", session.getId(), exception.getMessage());
        }
    }
}
