import {HISTORY_LIMIT, HISTORY_URL, SESSION_HEADER, UPLINK_URL} from '../config';
import {SESSION_ID} from '../session';
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
    headers: { 'Content-Type': 'application/json', [SESSION_HEADER]: SESSION_ID },
    body: JSON.stringify(message),
  });
  if (!response.ok) throw new UplinkRejectedError(response.status);
}

export async function fetchTelemetryHistory(): Promise<TelemetryPacket[]> {
  const response = await fetch(`${HISTORY_URL}?limit=${HISTORY_LIMIT}&session=${encodeURIComponent(SESSION_ID)}`);
  if (!response.ok) throw new Error(`History request failed with status ${response.status}`);
  return (await response.json()) as TelemetryPacket[];
}
