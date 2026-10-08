import type {AutonomyMode} from '../types/telemetry';
import {Badge, type BadgeTone} from './Badge';

const MODE_TONES: Record<AutonomyMode, BadgeTone> = {
  NOMINAL: 'green',
  POWER_SAVING: 'orange',
  SAFE: 'red',
};

const MODE_LABELS: Record<AutonomyMode, string> = {
  NOMINAL: 'NOMINAL',
  POWER_SAVING: 'POWER SAVING',
  SAFE: 'SAFE MODE',
};

interface ModeBadgeProps {
  mode: AutonomyMode;
}

export function ModeBadge({ mode }: ModeBadgeProps) {
  return <Badge tone={MODE_TONES[mode]}>{MODE_LABELS[mode]}</Badge>;
}
