package com.davutcagri.satsim.simulation;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class SimulationRunner {

    private static final long TICK_MILLIS = 20;
    private static final double NANOS_PER_SECOND = 1_000_000_000.0;

    private final Supplier<Collection<SimulationInstance>> simulations;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private long lastTickNanos;

    public SimulationRunner(Supplier<Collection<SimulationInstance>> simulations) {
        this.simulations = simulations;
    }

    public void start() {
        lastTickNanos = System.nanoTime();
        scheduler.scheduleAtFixedRate(this::tick, TICK_MILLIS, TICK_MILLIS, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    private void tick() {
        long now = System.nanoTime();
        double realDeltaSeconds = (now - lastTickNanos) / NANOS_PER_SECOND;
        lastTickNanos = now;
        for (SimulationInstance simulation : simulations.get()) {
            try {
                simulation.update(realDeltaSeconds);
            } catch (RuntimeException exception) {
                log.error("Simulation tick failed", exception);
            }
        }
    }
}
