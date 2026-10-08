package com.davutcagri.satsim.api;

import com.davutcagri.satsim.link.UplinkMessage;
import com.davutcagri.satsim.satellitelink.SatelliteLinkDownException;
import com.davutcagri.satsim.session.GroundStationSession;
import com.davutcagri.satsim.session.GroundStationSessionRegistry;
import com.davutcagri.satsim.session.SessionIds;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/uplink")
public class UplinkController {

    private final GroundStationSessionRegistry sessionRegistry;
    private final UplinkValidator validator;

    public UplinkController(GroundStationSessionRegistry sessionRegistry, UplinkValidator validator) {
        this.sessionRegistry = sessionRegistry;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<Void> send(@RequestHeader(name = SessionIds.HEADER, required = false) String sessionId,
                                     @RequestBody(required = false) UplinkMessage message) {
        if (!validator.isValid(message)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<GroundStationSession> session = SessionIds.parse(sessionId).flatMap(sessionRegistry::find);
        if (session.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            session.get().send(message);
        } catch (SatelliteLinkDownException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.accepted().build();
    }
}
