package com.davutcagri.satsim.environment;

import com.davutcagri.satsim.link.Vector3D;

public class CylindricalShadowModel {

    public boolean isInShadow(Vector3D satellitePosition, Vector3D sunDirection, double occultingRadiusMeters) {
        double alongAxis = satellitePosition.dot(sunDirection);
        double perpendicularDistance = satellitePosition.minus(sunDirection.times(alongAxis)).magnitude();
        return alongAxis < 0 && perpendicularDistance < occultingRadiusMeters;
    }
}
