package com.davutcagri.satsim.api;

import com.davutcagri.satsim.link.UplinkMessage;
import com.davutcagri.satsim.link.UplinkMessageType;
import org.springframework.stereotype.Component;

@Component
public class UplinkValidator {

    public boolean isValid(UplinkMessage message) {
        if (message == null || message.type() == null) {
            return false;
        }
        if (message.type() == UplinkMessageType.SET_SPEED) {
            return hasPositiveSpeed(message.speedMultiplier());
        }
        return true;
    }

    private boolean hasPositiveSpeed(Double speedMultiplier) {
        return speedMultiplier != null && Double.isFinite(speedMultiplier) && speedMultiplier > 0;
    }
}
