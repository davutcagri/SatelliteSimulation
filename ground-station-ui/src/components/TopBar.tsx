import type {SimulationClock} from '../types/telemetry';
import type {SendUplink} from '../types/uplink';
import {formatSimulationTime} from '../utils/format';
import {LinkIndicator} from './LinkIndicator';
import {PauseButton} from './PauseButton';
import {SpeedSelector} from './SpeedSelector';
import styles from './TopBar.module.css';

const NO_TIME_PLACEHOLDER = 'Day – · --:--:--';

interface TopBarProps {
  clock: SimulationClock | null;
  hasSignal: boolean;
  sendUplink: SendUplink;
}

export function TopBar({ clock, hasSignal, sendUplink }: TopBarProps) {
  return (
    <header className={styles.bar}>
      <h1 className={styles.title}>Ground Station</h1>
      <LinkIndicator hasSignal={hasSignal} />
      <div className={styles.time}>
        {clock ? formatSimulationTime(clock.simulationTimeSeconds) : NO_TIME_PLACEHOLDER}
      </div>
      <div className={styles.controls}>
        <SpeedSelector currentSpeed={clock?.speedMultiplier ?? null} sendUplink={sendUplink} />
        <PauseButton paused={clock?.paused ?? null} sendUplink={sendUplink} />
      </div>
    </header>
  );
}
