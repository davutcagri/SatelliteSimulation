import { useCallback, useEffect, useState } from 'react';
import { sendUplink, UplinkRejectedError } from '../api/groundStationApi';
import { SATELLITE_LINK_DOWN_STATUS, UPLINK_ERROR_DISPLAY_MS } from '../config';
import type { SendUplink, UplinkMessage } from '../types/uplink';

const SATELLITE_LINK_DOWN_MESSAGE = 'Satellite link is down';
const COMMAND_REJECTED_MESSAGE = 'Command could not be sent';
const GROUND_STATION_UNREACHABLE_MESSAGE = 'Ground station is unreachable';

function describeUplinkError(error: unknown): string {
  if (!(error instanceof UplinkRejectedError)) return GROUND_STATION_UNREACHABLE_MESSAGE;
  return error.status === SATELLITE_LINK_DOWN_STATUS
    ? SATELLITE_LINK_DOWN_MESSAGE
    : COMMAND_REJECTED_MESSAGE;
}

export function useUplink(): { send: SendUplink; errorMessage: string | null } {
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const send = useCallback(async (message: UplinkMessage) => {
    try {
      await sendUplink(message);
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(describeUplinkError(error));
    }
  }, []);

  useEffect(() => {
    if (!errorMessage) return;
    const timer = window.setTimeout(() => setErrorMessage(null), UPLINK_ERROR_DISPLAY_MS);
    return () => window.clearTimeout(timer);
  }, [errorMessage]);

  return { send, errorMessage };
}
