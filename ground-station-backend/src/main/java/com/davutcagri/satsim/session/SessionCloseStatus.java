package com.davutcagri.satsim.session;

import org.springframework.web.socket.CloseStatus;

public final class SessionCloseStatus {

    public static final CloseStatus INVALID_SESSION = new CloseStatus(4000, "Invalid session id");
    public static final CloseStatus CAPACITY_REACHED = new CloseStatus(4001, "Simulation capacity reached");
    public static final CloseStatus SATELLITE_UNAVAILABLE = new CloseStatus(4002, "Satellite unavailable");

    private SessionCloseStatus() {
    }
}
