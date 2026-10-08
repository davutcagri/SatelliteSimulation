import { SPEED_OPTIONS } from '../config';
import type { SendUplink } from '../types/uplink';
import { Button } from './Button';
import styles from './SpeedSelector.module.css';

interface SpeedSelectorProps {
  currentSpeed: number | null;
  sendUplink: SendUplink;
}

export function SpeedSelector({ currentSpeed, sendUplink }: SpeedSelectorProps) {
  return (
    <div className={styles.group} role="group" aria-label="Simulation speed">
      {SPEED_OPTIONS.map((speed) => (
        <Button
          key={speed}
          active={speed === currentSpeed}
          disabled={currentSpeed === null}
          onClick={() => sendUplink({ type: 'SET_SPEED', speedMultiplier: speed })}
        >
          {`${speed}x`}
        </Button>
      ))}
    </div>
  );
}
