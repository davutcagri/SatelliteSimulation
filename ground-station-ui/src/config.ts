const DEFAULT_API_URL = 'http://localhost:8080';

export const API_URL = import.meta.env.VITE_API_URL ?? DEFAULT_API_URL;
export const TELEMETRY_SOCKET_URL = `${API_URL.replace(/^http/, 'ws')}/ws/telemetry`;
export const UPLINK_URL = `${API_URL}/api/uplink`;
export const HISTORY_URL = `${API_URL}/api/telemetry/history`;

export const HISTORY_LIMIT = 2000;
export const HISTORY_STEP_SECONDS = 10;
export const SIGNAL_TIMEOUT_MS = 2000;
export const SIGNAL_CHECK_INTERVAL_MS = 500;
export const RECONNECT_DELAY_MS = 1000;
export const UPLINK_ERROR_DISPLAY_MS = 4000;
export const SATELLITE_LINK_DOWN_STATUS = 503;

export const SPEED_OPTIONS = [1, 60, 600, 3600];

export const EARTH_RADIUS_METERS = 6_371_000;
export const LOW_BATTERY_PERCENT = 30;
export const CRITICAL_BATTERY_PERCENT = 15;

export const SECONDS_PER_MINUTE = 60;
export const SECONDS_PER_HOUR = 3600;
export const SECONDS_PER_DAY = 86_400;
export const METERS_PER_KILOMETER = 1000;
export const TRAIL_POINT_COUNT = 1500;
