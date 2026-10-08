package com.davutcagri.satsim.communication;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.telemetry.TelemetryRecorder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TelemetryBroadcaster {

    private final TelemetryRecorder recorder;
    private final LinkSessionRegistry registry;
    private final ObjectMapper objectMapper;
    private final long publishIntervalMillis;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TelemetryBroadcaster(TelemetryRecorder recorder,
                                LinkSessionRegistry registry,
                                ObjectMapper objectMapper,
                                LinkProperties properties) {
        this.recorder = recorder;
        this.registry = registry;
        this.objectMapper = objectMapper;
        this.publishIntervalMillis = properties.publishIntervalMillis();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::broadcast, publishIntervalMillis, publishIntervalMillis, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    private void broadcast() {
        try {
            TelemetryPacket packet = recorder.latest();
            if (packet != null && !registry.sessions().isEmpty()) {
                TextMessage message = new TextMessage(objectMapper.writeValueAsString(packet));
                registry.sessions().forEach(session -> send(session, message));
            }
        } catch (JsonProcessingException | RuntimeException exception) {
            log.error("Telemetry broadcast failed", exception);
        }
    }

    private void send(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (Exception exception) {
            log.warn("Dropping session {}: {}", session.getId(), exception.getMessage());
            registry.unregister(session.getId());
        }
    }
}
