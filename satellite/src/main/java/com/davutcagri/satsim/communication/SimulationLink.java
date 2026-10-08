package com.davutcagri.satsim.communication;

import com.davutcagri.satsim.simulation.SimulationInstance;
import org.springframework.web.socket.WebSocketSession;

public record SimulationLink(WebSocketSession session, SimulationInstance simulation) {
}
