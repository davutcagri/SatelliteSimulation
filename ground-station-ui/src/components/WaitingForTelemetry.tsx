import styles from './WaitingForTelemetry.module.css';

interface WaitingForTelemetryProps {
  capacityReached: boolean;
}

export function WaitingForTelemetry({ capacityReached }: WaitingForTelemetryProps) {
  return (
    <p className={styles.message}>
      {capacityReached
        ? 'All simulation slots are in use right now. Retrying…'
        : 'Waiting for telemetry…'}
    </p>
  );
}
