package com.davutcagri.satsim.link;

public record UplinkMessage(
        UplinkMessageType type,
        Double speedMultiplier
) {
}
