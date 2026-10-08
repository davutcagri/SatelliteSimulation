package com.davutcagri.satsim.engine;

import lombok.Getter;

@Getter
public class SimulationClock {

    public static final double STEP_SECONDS = 1.0;

    private volatile long elapsedSteps;
    private volatile double speedMultiplier = 1.0;
    private volatile boolean paused;

    public void advanceStep() {
        elapsedSteps++;
    }

    public double elapsedSeconds() {
        return elapsedSteps * STEP_SECONDS;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        if (speedMultiplier <= 0) {
            throw new IllegalArgumentException("Speed multiplier must be positive: " + speedMultiplier);
        }
        this.speedMultiplier = speedMultiplier;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
}
