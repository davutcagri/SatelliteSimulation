import {METERS_PER_KILOMETER, SECONDS_PER_DAY, SECONDS_PER_HOUR, SECONDS_PER_MINUTE,} from '../config';

const TIME_FIELD_WIDTH = 2;

function padTimeField(value: number): string {
  return String(value).padStart(TIME_FIELD_WIDTH, '0');
}

export function formatSimulationTime(totalSeconds: number): string {
  const wholeSeconds = Math.floor(totalSeconds);
  const days = Math.floor(wholeSeconds / SECONDS_PER_DAY);
  const hours = Math.floor((wholeSeconds % SECONDS_PER_DAY) / SECONDS_PER_HOUR);
  const minutes = Math.floor((wholeSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
  const seconds = wholeSeconds % SECONDS_PER_MINUTE;
  return `Day ${days} · ${padTimeField(hours)}:${padTimeField(minutes)}:${padTimeField(seconds)}`;
}

export function formatHours(totalSeconds: number): string {
  return `${(totalSeconds / SECONDS_PER_HOUR).toFixed(1)} h`;
}

export function formatWatts(watts: number): string {
  return `${Math.round(watts).toLocaleString('en-US')} W`;
}

export function formatSignedWatts(watts: number): string {
  const sign = watts > 0 ? '+' : '';
  return `${sign}${formatWatts(watts)}`;
}

export function formatKilometers(meters: number): string {
  return `${(meters / METERS_PER_KILOMETER).toFixed(0)} km`;
}

export function formatKilometersPerSecond(metersPerSecond: number): string {
  return `${(metersPerSecond / METERS_PER_KILOMETER).toFixed(2)} km/s`;
}

export function formatPercent(percent: number): string {
  return `${percent.toFixed(1)}%`;
}
