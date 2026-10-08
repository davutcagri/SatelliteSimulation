import styles from './UplinkErrorToast.module.css';

interface UplinkErrorToastProps {
  message: string | null;
}

export function UplinkErrorToast({ message }: UplinkErrorToastProps) {
  if (!message) return null;
  return (
    <div className={styles.toast} role="alert">
      {message}
    </div>
  );
}
