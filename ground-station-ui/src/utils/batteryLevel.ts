import {CRITICAL_BATTERY_PERCENT, LOW_BATTERY_PERCENT} from '../config';

export type BatteryLevel = 'critical' | 'low' | 'normal';

export function batteryLevelOf(percent: number): BatteryLevel {
  if (percent < CRITICAL_BATTERY_PERCENT) return 'critical';
  if (percent < LOW_BATTERY_PERCENT) return 'low';
  return 'normal';
}
