package com.davutcagri.satsim.api;

import com.davutcagri.satsim.session.GroundStationSession;
import com.davutcagri.satsim.session.GroundStationSessionRegistry;
import com.davutcagri.satsim.session.SessionIds;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/link/status")
public class LinkStatusController {

    private final GroundStationSessionRegistry sessionRegistry;

    public LinkStatusController(GroundStationSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping
    public LinkStatusResponse status(@RequestParam(name = SessionIds.QUERY_PARAMETER, required = false) String sessionId) {
        boolean connected = SessionIds.parse(sessionId)
                .flatMap(sessionRegistry::find)
                .map(GroundStationSession::isSatelliteConnected)
                .orElse(false);
        return new LinkStatusResponse(connected);
    }
}
