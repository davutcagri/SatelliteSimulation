package com.davutcagri.satsim.physics;

import com.davutcagri.satsim.link.Vector3D;
import com.davutcagri.satsim.physics.body.SpaceObject;

public class Rk4Propagator {

    private static final double GRAVITATIONAL_CONSTANT = 6.67430e-11;

    public void propagate(SpaceObject body, SpaceObject centralBody, double deltaTimeSeconds) {
        double gravitationalParameter = GRAVITATIONAL_CONSTANT * centralBody.getMassKilograms();
        Vector3D position = body.getPosition();
        Vector3D velocity = body.getVelocity();
        double halfStep = deltaTimeSeconds / 2;

        Vector3D positionRate1 = velocity;
        Vector3D velocityRate1 = acceleration(position, gravitationalParameter);

        Vector3D positionRate2 = velocity.plus(velocityRate1.times(halfStep));
        Vector3D velocityRate2 = acceleration(position.plus(positionRate1.times(halfStep)), gravitationalParameter);

        Vector3D positionRate3 = velocity.plus(velocityRate2.times(halfStep));
        Vector3D velocityRate3 = acceleration(position.plus(positionRate2.times(halfStep)), gravitationalParameter);

        Vector3D positionRate4 = velocity.plus(velocityRate3.times(deltaTimeSeconds));
        Vector3D velocityRate4 = acceleration(position.plus(positionRate3.times(deltaTimeSeconds)), gravitationalParameter);

        Vector3D positionRate = weightedAverage(positionRate1, positionRate2, positionRate3, positionRate4);
        Vector3D velocityRate = weightedAverage(velocityRate1, velocityRate2, velocityRate3, velocityRate4);

        body.setPosition(position.plus(positionRate.times(deltaTimeSeconds)));
        body.setVelocity(velocity.plus(velocityRate.times(deltaTimeSeconds)));
    }

    private Vector3D acceleration(Vector3D position, double gravitationalParameter) {
        double distanceCubed = position.magnitudeSquared() * position.magnitude();
        return position.times(-gravitationalParameter / distanceCubed);
    }

    private Vector3D weightedAverage(Vector3D first, Vector3D second, Vector3D third, Vector3D fourth) {
        return first
                .plus(second.times(2))
                .plus(third.times(2))
                .plus(fourth)
                .divide(6);
    }
}
