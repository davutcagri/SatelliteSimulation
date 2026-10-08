import {HISTORY_LIMIT, HISTORY_STEP_SECONDS} from '../config';
import type {TelemetryPacket} from '../types/telemetry';

function timeOf(packet: TelemetryPacket): number {
  return packet.clock.simulationTimeSeconds;
}

export function appendToHistory(
  history: TelemetryPacket[],
  packet: TelemetryPacket,
): TelemetryPacket[] {
  const last = history[history.length - 1];
  if (!last || timeOf(packet) < timeOf(last)) return [packet];
  if (timeOf(packet) - timeOf(last) < HISTORY_STEP_SECONDS) return history;
  return [...history, packet].slice(-HISTORY_LIMIT);
}

export function mergeHistory(
  loaded: TelemetryPacket[],
  live: TelemetryPacket[],
): TelemetryPacket[] {
  const lastLoaded = loaded[loaded.length - 1];
  if (!lastLoaded) return live;
  const newerLive = live.filter((packet) => timeOf(packet) > timeOf(lastLoaded));
  return [...loaded, ...newerLive].slice(-HISTORY_LIMIT);
}
