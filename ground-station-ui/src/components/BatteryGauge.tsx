import {batteryLevelOf} from '../utils/batteryLevel';
import {formatPercent} from '../utils/format';
import styles from './BatteryGauge.module.css';

const MAX_PERCENT = 100;

interface BatteryGaugeProps {
  percent: number;
}

export function BatteryGauge({ percent }: BatteryGaugeProps) {
  const clampedPercent = Math.min(MAX_PERCENT, Math.max(0, percent));
  return (
    <div className={styles.gauge}>
      <div className={styles.value}>{formatPercent(percent)}</div>
      <div
        className={styles.track}
        role="progressbar"
        aria-label="Battery level"
        aria-valuemin={0}
        aria-valuemax={MAX_PERCENT}
        aria-valuenow={clampedPercent}
      >
        <div
          className={`${styles.fill} ${styles[batteryLevelOf(percent)]}`}
          style={{ width: `${clampedPercent}%` }}
        />
      </div>
    </div>
  );
}
