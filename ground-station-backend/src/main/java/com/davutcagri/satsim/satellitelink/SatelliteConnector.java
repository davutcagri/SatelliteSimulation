package com.davutcagri.satsim.satellitelink;

import com.davutcagri.satsim.config.GroundStationProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.*;

@Component
public class SatelliteConnector {

    private static final Logger log = LoggerFactory.getLogger(SatelliteConnector.class);

    private final GroundStationProperties properties;
    private final SatelliteLink satelliteLink;
    private final SatelliteSocketHandler socketHandler;
    private final StandardWebSocketClient client = new StandardWebSocketClient();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(
            runnable -> new Thread(runnable, "satellite-connector"));

    public SatelliteConnector(GroundStationProperties properties,
                              SatelliteLink satelliteLink,
                              SatelliteSocketHandler socketHandler) {
        this.properties = properties;
        this.satelliteLink = satelliteLink;
        this.socketHandler = socketHandler;
    }

    @PostConstruct
    void start() {
        long intervalMillis = properties.reconnectInterval().toMillis();
        scheduler.scheduleWithFixedDelay(this::connectIfDisconnected, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    void stop() {
        scheduler.shutdownNow();
        satelliteLink.close();
    }

    private void connectIfDisconnected() {
        if (satelliteLink.isConnected()) {
            return;
        }
        try {
            client.execute(socketHandler, new WebSocketHttpHeaders(), properties.satelliteUrl())
                    .get(properties.connectTimeout().toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException | TimeoutException | RuntimeException exception) {
            log.debug("Satellite connection attempt failed: {}", exception.getMessage());
        }
    }
}
