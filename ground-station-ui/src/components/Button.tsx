import type {ReactNode} from 'react';
import styles from './Button.module.css';

interface ButtonProps {
  onClick: () => void;
  children: ReactNode;
  active?: boolean;
  danger?: boolean;
  disabled?: boolean;
}

export function Button({ onClick, children, active = false, danger = false, disabled = false }: ButtonProps) {
  const classNames = [styles.button, active ? styles.active : '', danger ? styles.danger : '']
    .filter(Boolean)
    .join(' ');
  return (
    <button type="button" className={classNames} onClick={onClick} disabled={disabled}>
      {children}
    </button>
  );
}
