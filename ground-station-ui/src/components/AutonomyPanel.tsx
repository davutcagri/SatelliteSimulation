import type {AutonomyState} from '../types/telemetry';
import type {SendUplink} from '../types/uplink';
import {Badge} from './Badge';
import {Button} from './Button';
import {MetricRow} from './MetricRow';
import {ModeBadge} from './ModeBadge';
import {Panel} from './Panel';
import styles from './AutonomyPanel.module.css';

interface AutonomyPanelProps {
  autonomy: AutonomyState;
  sendUplink: SendUplink;
}

export function AutonomyPanel({ autonomy, sendUplink }: AutonomyPanelProps) {
  return (
    <Panel title="Autonomy">
      <MetricRow label="Mode">
        <ModeBadge mode={autonomy.mode} />
      </MetricRow>
      <MetricRow label="Payload">
        <Badge tone={autonomy.payloadEnabled ? 'green' : 'neutral'}>
          {autonomy.payloadEnabled ? 'ON' : 'OFF'}
        </Badge>
      </MetricRow>
      <div className={styles.actions}>
        <Button
          onClick={() => sendUplink({ type: autonomy.payloadEnabled ? 'PAYLOAD_OFF' : 'PAYLOAD_ON' })}
        >
          {autonomy.payloadEnabled ? 'Disable payload' : 'Enable payload'}
        </Button>
      </div>
      <MetricRow label="Solar array fault">
        <Badge tone={autonomy.solarArrayFault ? 'red' : 'green'}>
          {autonomy.solarArrayFault ? 'FAULT' : 'NORMAL'}
        </Badge>
      </MetricRow>
      {autonomy.solarArrayFault && (
        <p className={styles.warning} role="alert">
          Solar array fault: output halved.
        </p>
      )}
      <div className={styles.actions}>
        <Button
          danger
          disabled={autonomy.solarArrayFault}
          onClick={() => sendUplink({ type: 'SOLAR_ARRAY_FAULT_INJECT' })}
        >
          Inject fault
        </Button>
        <Button
          disabled={!autonomy.solarArrayFault}
          onClick={() => sendUplink({ type: 'SOLAR_ARRAY_FAULT_CLEAR' })}
        >
          Clear fault
        </Button>
      </div>
    </Panel>
  );
}
