package com.davutcagri.satsim.satellitelink;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SatelliteSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SatelliteSocketHandler.class);

    private final ObjectMapper objectMapper;
    private final SatelliteListener listener;

    public SatelliteSocketHandler(ObjectMapper objectMapper, SatelliteListener listener) {
        this.objectMapper = objectMapper;
        this.listener = listener;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        listener.onConnected(new SatelliteLink(session, objectMapper));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String json = message.getPayload();
        try {
            listener.onTelemetry(objectMapper.readValue(json, TelemetryPacket.class), json);
        } catch (JsonProcessingException exception) {
            log.warn("Discarded malformed telemetry frame: {}", exception.getOriginalMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("Satellite link transport error: {}", exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        listener.onDisconnected(status);
    }
}
