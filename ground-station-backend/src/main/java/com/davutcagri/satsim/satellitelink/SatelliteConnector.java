package com.davutcagri.satsim.satellitelink;

import com.davutcagri.satsim.config.GroundStationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class SatelliteConnector {

    private final GroundStationProperties properties;
    private final ObjectMapper objectMapper;
    private final StandardWebSocketClient client = new StandardWebSocketClient();

    public SatelliteConnector(GroundStationProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<?> connect(SatelliteListener listener) {
        SatelliteSocketHandler handler = new SatelliteSocketHandler(objectMapper, listener);
        return client.execute(handler, new WebSocketHttpHeaders(), properties.satelliteUrl())
                .orTimeout(properties.connectTimeout().toMillis(), TimeUnit.MILLISECONDS);
    }
}
