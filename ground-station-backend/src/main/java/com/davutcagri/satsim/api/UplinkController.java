package com.davutcagri.satsim.api;

import com.davutcagri.satsim.link.UplinkMessage;
import com.davutcagri.satsim.satellitelink.SatelliteLink;
import com.davutcagri.satsim.satellitelink.SatelliteLinkDownException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/uplink")
public class UplinkController {

    private final SatelliteLink satelliteLink;
    private final UplinkValidator validator;

    public UplinkController(SatelliteLink satelliteLink, UplinkValidator validator) {
        this.satelliteLink = satelliteLink;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody(required = false) UplinkMessage message) {
        if (!validator.isValid(message)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            satelliteLink.send(message);
        } catch (SatelliteLinkDownException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.accepted().build();
    }
}
