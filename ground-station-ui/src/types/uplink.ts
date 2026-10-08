export type UplinkType =
  | 'PAYLOAD_ON'
  | 'PAYLOAD_OFF'
  | 'SOLAR_ARRAY_FAULT_INJECT'
  | 'SOLAR_ARRAY_FAULT_CLEAR'
  | 'SET_SPEED'
  | 'PAUSE'
  | 'RESUME';

export interface UplinkMessage {
  type: UplinkType;
  speedMultiplier?: number;
}

export type SendUplink = (message: UplinkMessage) => Promise<void>;
