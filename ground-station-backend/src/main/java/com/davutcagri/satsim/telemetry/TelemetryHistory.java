package com.davutcagri.satsim.telemetry;

import com.davutcagri.satsim.config.GroundStationProperties;
import com.davutcagri.satsim.link.TelemetryPacket;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TelemetryHistory {

    private final double sampleIntervalSeconds;
    private final int capacity;
    private final Deque<TelemetryPacket> samples = new ArrayDeque<>();
    private double lastSampleTimeSeconds;

    public TelemetryHistory(GroundStationProperties.History properties) {
        this.sampleIntervalSeconds = properties.sampleIntervalSeconds();
        this.capacity = properties.capacity();
    }

    public synchronized void record(TelemetryPacket packet) {
        double timeSeconds = packet.clock().simulationTimeSeconds();
        if (timeSeconds < lastSampleTimeSeconds) {
            samples.clear();
        }
        if (samples.isEmpty() || timeSeconds - lastSampleTimeSeconds >= sampleIntervalSeconds) {
            append(packet, timeSeconds);
        }
    }

    public synchronized List<TelemetryPacket> latest(int limit) {
        int skipped = Math.max(0, samples.size() - limit);
        return new ArrayList<>(samples.stream().skip(skipped).toList());
    }

    public int capacity() {
        return capacity;
    }

    private void append(TelemetryPacket packet, double timeSeconds) {
        samples.addLast(packet);
        lastSampleTimeSeconds = timeSeconds;
        if (samples.size() > capacity) {
            samples.removeFirst();
        }
    }
}
