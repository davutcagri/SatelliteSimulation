package com.davutcagri.satsim.engine;

@FunctionalInterface
public interface SimulationStep {

    void advance(double deltaTimeSeconds);
}
