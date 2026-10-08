package com.davutcagri.satsim.communication;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TelemetryBroadcaster {

    private final LinkSessionRegistry registry;
    private final ObjectMapper objectMapper;
    private final long publishIntervalMillis;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TelemetryBroadcaster(LinkSessionRegistry registry,
                                ObjectMapper objectMapper,
                                LinkProperties properties) {
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
        registry.links().forEach(this::send);
    }

    private void send(SimulationLink link) {
        TelemetryPacket packet = link.simulation().latest();
        if (packet == null) {
            return;
        }
        try {
            link.session().sendMessage(new TextMessage(objectMapper.writeValueAsString(packet)));
        } catch (JsonProcessingException exception) {
            log.error("Telemetry packet cannot be serialized", exception);
        } catch (Exception exception) {
            log.warn("Dropping session {}: {}", link.session().getId(), exception.getMessage());
            registry.unregister(link.session().getId());
        }
    }
}
