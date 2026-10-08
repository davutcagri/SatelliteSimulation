package com.davutcagri.satsim.api;

import com.davutcagri.satsim.link.TelemetryPacket;
import com.davutcagri.satsim.telemetry.TelemetryHistory;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/telemetry/history")
public class TelemetryHistoryController {

    private static final int DEFAULT_LIMIT = 500;
    private static final int MINIMUM_LIMIT = 1;

    private final TelemetryHistory history;

    public TelemetryHistoryController(TelemetryHistory history) {
        this.history = history;
    }

    @GetMapping
    public List<TelemetryPacket> history(@RequestParam(name = "limit", defaultValue = "" + DEFAULT_LIMIT) int limit) {
        if (limit < MINIMUM_LIMIT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be at least " + MINIMUM_LIMIT);
        }
        return history.latest(Math.min(limit, history.capacity()));
    }
}
