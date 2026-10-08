import {
  CAPACITY_REACHED_CLOSE_CODE,
  CAPACITY_RETRY_DELAY_MS,
  RECONNECT_DELAY_MS,
  TELEMETRY_SOCKET_URL,
} from '../config';
import { SESSION_ID } from '../session';
import type { TelemetryPacket } from '../types/telemetry';

export interface TelemetryHandlers {
  onPacket: (packet: TelemetryPacket) => void;
  onCapacityChange: (capacityReached: boolean) => void;
}

export function connectTelemetry({ onPacket, onCapacityChange }: TelemetryHandlers): () => void {
  let socket: WebSocket | null = null;
  let reconnectTimer: number | undefined;
  let closedByCaller = false;

  const open = () => {
    socket = new WebSocket(`${TELEMETRY_SOCKET_URL}?session=${encodeURIComponent(SESSION_ID)}`);
    socket.onmessage = (event) => {
      onCapacityChange(false);
      onPacket(JSON.parse(event.data as string) as TelemetryPacket);
    };
    socket.onclose = (event) => {
      if (closedByCaller) return;
      const capacityReached = event.code === CAPACITY_REACHED_CLOSE_CODE;
      onCapacityChange(capacityReached);
      reconnectTimer = window.setTimeout(open, capacityReached ? CAPACITY_RETRY_DELAY_MS : RECONNECT_DELAY_MS);
    };
  };

  open();

  return () => {
    closedByCaller = true;
    window.clearTimeout(reconnectTimer);
    socket?.close();
  };
}
