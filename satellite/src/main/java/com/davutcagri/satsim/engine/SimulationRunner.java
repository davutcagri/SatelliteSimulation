package com.davutcagri.satsim.engine;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimulationRunner {

    private static final long TICK_MILLIS = 20;
    private static final double NANOS_PER_SECOND = 1_000_000_000.0;

    private final SimulationEngine engine;
    private final Runnable afterTick;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private long lastTickNanos;

    public SimulationRunner(SimulationEngine engine, Runnable afterTick) {
        this.engine = engine;
        this.afterTick = afterTick;
    }

    public void start() {
        lastTickNanos = System.nanoTime();
        scheduler.scheduleAtFixedRate(this::tick, TICK_MILLIS, TICK_MILLIS, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    private void tick() {
        try {
            long now = System.nanoTime();
            double realDeltaSeconds = (now - lastTickNanos) / NANOS_PER_SECOND;
            lastTickNanos = now;
            engine.update(realDeltaSeconds);
            afterTick.run();
        } catch (RuntimeException exception) {
            log.error("Simulation tick failed", exception);
        }
    }
}
