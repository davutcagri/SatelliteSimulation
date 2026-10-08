package com.davutcagri.satsim.api;

import com.davutcagri.satsim.config.GroundStationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final String TELEMETRY_PATH = "/ws/telemetry";

    private final TelemetrySocketHandler telemetrySocketHandler;
    private final GroundStationProperties properties;

    public WebSocketConfig(TelemetrySocketHandler telemetrySocketHandler, GroundStationProperties properties) {
        this.telemetrySocketHandler = telemetrySocketHandler;
        this.properties = properties;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(telemetrySocketHandler, TELEMETRY_PATH)
                .setAllowedOrigins(properties.allowedOrigin());
    }
}
