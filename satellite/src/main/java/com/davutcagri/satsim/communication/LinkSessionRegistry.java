package com.davutcagri.satsim.communication;

import com.davutcagri.satsim.simulation.SimulationInstance;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class LinkSessionRegistry {

    private static final int SEND_TIME_LIMIT_MILLIS = 1_000;
    private static final int BUFFER_SIZE_LIMIT_BYTES = 512 * 1024;

    private final int maxLinks;
    private final Map<String, SimulationLink> links = new ConcurrentHashMap<>();

    public LinkSessionRegistry(int maxLinks) {
        this.maxLinks = maxLinks;
    }

    public synchronized boolean register(WebSocketSession session, Supplier<SimulationInstance> simulationFactory) {
        if (links.size() >= maxLinks) {
            return false;
        }
        WebSocketSession decorated = new ConcurrentWebSocketSessionDecorator(
                session, SEND_TIME_LIMIT_MILLIS, BUFFER_SIZE_LIMIT_BYTES);
        links.put(session.getId(), new SimulationLink(decorated, simulationFactory.get()));
        return true;
    }

    public void unregister(String sessionId) {
        links.remove(sessionId);
    }

    public Optional<SimulationLink> find(String sessionId) {
        return Optional.ofNullable(links.get(sessionId));
    }

    public Collection<SimulationLink> links() {
        return links.values();
    }

    public Collection<SimulationInstance> simulations() {
        return links.values().stream().map(SimulationLink::simulation).toList();
    }

    public int size() {
        return links.size();
    }
}
