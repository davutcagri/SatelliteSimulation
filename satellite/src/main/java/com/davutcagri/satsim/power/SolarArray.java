package com.davutcagri.satsim.power;

public class SolarArray {

    private final double peakWatts;
    private final double faultOutputFraction;
    private volatile boolean faulty;

    public SolarArray(PowerProperties properties) {
        this.peakWatts = properties.solarArrayPeakWatts();
        this.faultOutputFraction = properties.faultOutputFraction();
    }

    public double generationWatts(boolean sunlit) {
        if (!sunlit) {
            return 0;
        }
        return faulty ? peakWatts * faultOutputFraction : peakWatts;
    }

    public boolean isFaulty() {
        return faulty;
    }

    public void injectFault() {
        faulty = true;
    }

    public void clearFault() {
        faulty = false;
    }
}
