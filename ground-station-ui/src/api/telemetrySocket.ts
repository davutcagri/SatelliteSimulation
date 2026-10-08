import { RECONNECT_DELAY_MS, TELEMETRY_SOCKET_URL } from '../config';
import type { TelemetryPacket } from '../types/telemetry';

export function connectTelemetry(onPacket: (packet: TelemetryPacket) => void): () => void {
  let socket: WebSocket | null = null;
  let reconnectTimer: number | undefined;
  let closedByCaller = false;

  const open = () => {
    socket = new WebSocket(TELEMETRY_SOCKET_URL);
    socket.onmessage = (event) => onPacket(JSON.parse(event.data as string) as TelemetryPacket);
    socket.onclose = () => {
      if (!closedByCaller) reconnectTimer = window.setTimeout(open, RECONNECT_DELAY_MS);
    };
  };

  open();

  return () => {
    closedByCaller = true;
    window.clearTimeout(reconnectTimer);
    socket?.close();
  };
}
