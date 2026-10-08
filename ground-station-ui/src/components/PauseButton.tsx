import type { SendUplink } from '../types/uplink';
import { Button } from './Button';

interface PauseButtonProps {
  paused: boolean | null;
  sendUplink: SendUplink;
}

export function PauseButton({ paused, sendUplink }: PauseButtonProps) {
  return (
    <Button
      disabled={paused === null}
      onClick={() => sendUplink({ type: paused ? 'RESUME' : 'PAUSE' })}
    >
      {paused ? 'Resume' : 'Pause'}
    </Button>
  );
}
