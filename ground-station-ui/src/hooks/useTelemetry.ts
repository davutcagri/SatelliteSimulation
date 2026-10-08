import { useEffect, useRef, useState } from 'react';
import { fetchTelemetryHistory } from '../api/groundStationApi';
import { connectTelemetry } from '../api/telemetrySocket';
import { SIGNAL_CHECK_INTERVAL_MS, SIGNAL_TIMEOUT_MS } from '../config';
import type { TelemetryPacket } from '../types/telemetry';
import { appendToHistory, mergeHistory } from '../utils/telemetryHistory';

export interface TelemetryState {
  latest: TelemetryPacket | null;
  history: TelemetryPacket[];
  hasSignal: boolean;
}

export function useTelemetry(): TelemetryState {
  const [latest, setLatest] = useState<TelemetryPacket | null>(null);
  const [history, setHistory] = useState<TelemetryPacket[]>([]);
  const [hasSignal, setHasSignal] = useState(false);
  const lastPacketAtMs = useRef(0);

  useEffect(() => {
    let cancelled = false;
    fetchTelemetryHistory()
      .then((loaded) => {
        if (!cancelled) setHistory((live) => mergeHistory(loaded, live));
      })
      .catch(() => undefined);
    return () => {
      cancelled = true;
    };
  }, []);

  useEffect(
    () =>
      connectTelemetry((packet) => {
        lastPacketAtMs.current = Date.now();
        setHasSignal(true);
        setLatest(packet);
        setHistory((current) => appendToHistory(current, packet));
      }),
    [],
  );

  useEffect(() => {
    const timer = window.setInterval(() => {
      setHasSignal(Date.now() - lastPacketAtMs.current < SIGNAL_TIMEOUT_MS);
    }, SIGNAL_CHECK_INTERVAL_MS);
    return () => window.clearInterval(timer);
  }, []);

  return { latest, history, hasSignal };
}
