import {HISTORY_LIMIT, HISTORY_URL, UPLINK_URL} from '../config';
import type {TelemetryPacket} from '../types/telemetry';
import type {UplinkMessage} from '../types/uplink';

export class UplinkRejectedError extends Error {
  constructor(readonly status: number) {
    super(`Uplink rejected with status ${status}`);
  }
}

export async function sendUplink(message: UplinkMessage): Promise<void> {
  const response = await fetch(UPLINK_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(message),
  });
  if (!response.ok) throw new UplinkRejectedError(response.status);
}

export async function fetchTelemetryHistory(): Promise<TelemetryPacket[]> {
  const response = await fetch(`${HISTORY_URL}?limit=${HISTORY_LIMIT}`);
  if (!response.ok) throw new Error(`History request failed with status ${response.status}`);
  return (await response.json()) as TelemetryPacket[];
}
