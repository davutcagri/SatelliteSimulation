package com.davutcagri.satsim.engine;

public class SimulationEngine {

    private static final int MAX_STEPS_PER_UPDATE = 100_000;

    private final SimulationClock clock;
    private final SimulationStep simulationStep;
    private double accumulatedSeconds;

    public SimulationEngine(SimulationClock clock, SimulationStep simulationStep) {
        this.clock = clock;
        this.simulationStep = simulationStep;
    }

    public void update(double realDeltaSeconds) {
        if (clock.isPaused()) {
            return;
        }

        accumulatedSeconds += realDeltaSeconds * clock.getSpeedMultiplier();

        double dueSteps = accumulatedSeconds / SimulationClock.STEP_SECONDS;
        int stepsToRun = (int) Math.min(dueSteps, MAX_STEPS_PER_UPDATE);
        runSteps(stepsToRun);

        boolean fellBehind = dueSteps > MAX_STEPS_PER_UPDATE;
        accumulatedSeconds = fellBehind ? 0 : accumulatedSeconds - stepsToRun * SimulationClock.STEP_SECONDS;
    }

    private void runSteps(int stepCount) {
        for (int i = 0; i < stepCount; i++) {
            simulationStep.advance(SimulationClock.STEP_SECONDS);
            clock.advanceStep();
        }
    }
}
