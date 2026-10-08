import { useCallback, useMemo } from 'react';
import { TRAIL_POINT_COUNT } from '../config';
import { drawOrbitScene } from '../rendering/orbitRenderer';
import type { OrbitState, TelemetryPacket } from '../types/telemetry';
import { formatKilometers, formatKilometersPerSecond } from '../utils/format';
import { Badge } from './Badge';
import { DrawingCanvas } from './DrawingCanvas';
import { MetricRow } from './MetricRow';
import { Panel } from './Panel';

interface OrbitPanelProps {
  orbit: OrbitState;
  history: TelemetryPacket[];
}

export function OrbitPanel({ orbit, history }: OrbitPanelProps) {
  const pastPositions = useMemo(
    () => history.slice(-TRAIL_POINT_COUNT).map((packet) => packet.orbit.satellitePositionMeters),
    [history],
  );

  const draw = useCallback(
    (ctx: CanvasRenderingContext2D, width: number, height: number) =>
      drawOrbitScene(ctx, width, height, {
        orbit,
        trail: [...pastPositions, orbit.satellitePositionMeters],
      }),
    [orbit, pastPositions],
  );

  return (
    <Panel title="Orbit (Earth-centered)">
      <DrawingCanvas label="Satellite orbit around the Earth" draw={draw} />
      <MetricRow label="Illumination">
        <Badge tone={orbit.sunlit ? 'sun' : 'neutral'}>{orbit.sunlit ? 'Sunlit' : 'In shadow'}</Badge>
      </MetricRow>
      <MetricRow label="Altitude">{formatKilometers(orbit.altitudeMeters)}</MetricRow>
      <MetricRow label="Speed">{formatKilometersPerSecond(orbit.speedMetersPerSecond)}</MetricRow>
    </Panel>
  );
}
