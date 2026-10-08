import type { ReactNode } from 'react';
import styles from './MetricRow.module.css';

interface MetricRowProps {
  label: string;
  children: ReactNode;
}

export function MetricRow({ label, children }: MetricRowProps) {
  return (
    <div className={styles.row}>
      <span className={styles.label}>{label}</span>
      <span className={styles.value}>{children}</span>
    </div>
  );
}
