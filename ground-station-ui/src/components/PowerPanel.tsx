import type {OrbitState, PowerState, TelemetryPacket} from '../types/telemetry';
import {formatSignedWatts, formatWatts} from '../utils/format';
import {Badge} from './Badge';
import {BatteryChart} from './BatteryChart';
import {BatteryGauge} from './BatteryGauge';
import {MetricRow} from './MetricRow';
import {Panel} from './Panel';

interface PowerPanelProps {
  power: PowerState;
  sunlit: OrbitState['sunlit'];
  history: TelemetryPacket[];
}

export function PowerPanel({ power, sunlit, history }: PowerPanelProps) {
  const netWatts = power.solarGenerationWatts - power.loadWatts;
  return (
    <Panel title="Power system">
      <BatteryGauge percent={power.batteryChargePercent} />
      <MetricRow label="Solar array">
        <Badge tone={sunlit ? 'sun' : 'neutral'}>{sunlit ? 'Sunlit' : 'In shadow'}</Badge>
      </MetricRow>
      <MetricRow label="Generation">{formatWatts(power.solarGenerationWatts)}</MetricRow>
      <MetricRow label="Load">{formatWatts(power.loadWatts)}</MetricRow>
      <MetricRow label="Net power">{formatSignedWatts(netWatts)}</MetricRow>
      <BatteryChart history={history} />
    </Panel>
  );
}
