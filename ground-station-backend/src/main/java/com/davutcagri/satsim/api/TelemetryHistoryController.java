package com.davutcagri.satsim.api;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.session.GroundStationSessionRegistry;
import com.davutcagri.satsim.session.SessionIds;
import com.davutcagri.satsim.telemetry.TelemetryHistory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/telemetry/history")
public class TelemetryHistoryController {

    private static final int DEFAULT_LIMIT = 500;
    private static final int MINIMUM_LIMIT = 1;

    private final GroundStationSessionRegistry sessionRegistry;

    public TelemetryHistoryController(GroundStationSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping
    public List<TelemetryPacket> history(@RequestParam(name = SessionIds.QUERY_PARAMETER, required = false) String sessionId,
                                         @RequestParam(name = "limit", defaultValue = "" + DEFAULT_LIMIT) int limit) {
        if (limit < MINIMUM_LIMIT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be at least " + MINIMUM_LIMIT);
        }
        return SessionIds.parse(sessionId)
                .flatMap(sessionRegistry::find)
                .map(session -> latest(session.history(), limit))
                .orElse(List.of());
    }

    private List<TelemetryPacket> latest(TelemetryHistory history, int limit) {
        return history.latest(Math.min(limit, history.capacity()));
    }
}
