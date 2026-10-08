import styles from './LinkIndicator.module.css';

interface LinkIndicatorProps {
  hasSignal: boolean;
}

export function LinkIndicator({ hasSignal }: LinkIndicatorProps) {
  return (
    <div className={`${styles.indicator} ${hasSignal ? styles.connected : styles.lost}`}>
      {hasSignal ? 'Satellite linked' : 'No signal'}
    </div>
  );
}
