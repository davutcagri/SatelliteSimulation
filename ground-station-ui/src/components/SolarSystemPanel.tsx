import {useCallback} from 'react';
import {drawSolarSystemScene} from '../rendering/solarSystemRenderer';
import type {Vector3} from '../types/telemetry';
import {DrawingCanvas} from './DrawingCanvas';
import {Panel} from './Panel';

interface SolarSystemPanelProps {
  earthPositionMeters: Vector3;
}

export function SolarSystemPanel({ earthPositionMeters }: SolarSystemPanelProps) {
  const draw = useCallback(
    (ctx: CanvasRenderingContext2D, width: number, height: number) =>
      drawSolarSystemScene(ctx, width, height, earthPositionMeters),
    [earthPositionMeters],
  );

  return (
    <Panel title="Solar system (schematic, not to scale)">
      <DrawingCanvas label="Earth's position around the Sun" draw={draw} />
    </Panel>
  );
}
