import styles from './Badge.module.css';

export type BadgeTone = 'green' | 'orange' | 'red' | 'sun' | 'neutral';

interface BadgeProps {
  tone: BadgeTone;
  children: string;
}

export function Badge({ tone, children }: BadgeProps) {
  return <span className={`${styles.badge} ${styles[tone]}`}>{children}</span>;
}
