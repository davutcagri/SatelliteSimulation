package com.davutcagri.satsim.api;

import com.davutcagri.satsim.satellitelink.SatelliteLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/link/status")
public class LinkStatusController {

    private final SatelliteLink satelliteLink;

    public LinkStatusController(SatelliteLink satelliteLink) {
        this.satelliteLink = satelliteLink;
    }

    @GetMapping
    public LinkStatusResponse status() {
        return new LinkStatusResponse(satelliteLink.isConnected());
    }
}
