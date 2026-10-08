export interface Vector3 {
  x: number;
  y: number;
  z: number;
}

export interface SimulationClock {
  simulationTimeSeconds: number;
  speedMultiplier: number;
  paused: boolean;
}

export interface OrbitState {
  earthPositionMeters: Vector3;
  satellitePositionMeters: Vector3;
  satelliteVelocityMetersPerSecond: Vector3;
  sunDirection: Vector3;
  altitudeMeters: number;
  speedMetersPerSecond: number;
  sunlit: boolean;
}

export interface PowerState {
  solarGenerationWatts: number;
  loadWatts: number;
  batteryChargePercent: number;
}

export type AutonomyMode = 'NOMINAL' | 'POWER_SAVING' | 'SAFE';

export interface AutonomyState {
  mode: AutonomyMode;
  payloadEnabled: boolean;
  solarArrayFault: boolean;
}

export interface TelemetryPacket {
  clock: SimulationClock;
  orbit: OrbitState;
  power: PowerState;
  autonomy: AutonomyState;
}
