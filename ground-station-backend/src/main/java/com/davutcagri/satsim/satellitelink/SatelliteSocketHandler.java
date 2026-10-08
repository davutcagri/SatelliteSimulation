package com.davutcagri.satsim.satellitelink;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.telemetry.TelemetryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SatelliteSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SatelliteSocketHandler.class);

    private final SatelliteLink satelliteLink;
    private final TelemetryService telemetryService;
    private final ObjectMapper objectMapper;

    public SatelliteSocketHandler(SatelliteLink satelliteLink,
                                  TelemetryService telemetryService,
                                  ObjectMapper objectMapper) {
        this.satelliteLink = satelliteLink;
        this.telemetryService = telemetryService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        satelliteLink.attach(session);
        log.info("Satellite link connected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            telemetryService.receive(objectMapper.readValue(message.getPayload(), TelemetryPacket.class));
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
        satelliteLink.detach(session);
        log.info("Satellite link closed: {}", status);
    }
}
