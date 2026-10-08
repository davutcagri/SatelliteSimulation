import {Dashboard} from './components/Dashboard';
import {TopBar} from './components/TopBar';
import {UplinkErrorToast} from './components/UplinkErrorToast';
import {WaitingForTelemetry} from './components/WaitingForTelemetry';
import {useTelemetry} from './hooks/useTelemetry';
import {useUplink} from './hooks/useUplink';

export function App() {
  const { latest, history, hasSignal } = useTelemetry();
  const { send, errorMessage } = useUplink();

  return (
    <>
      <TopBar clock={latest?.clock ?? null} hasSignal={hasSignal} sendUplink={send} />
      {latest ? (
        <Dashboard packet={latest} history={history} hasSignal={hasSignal} sendUplink={send} />
      ) : (
        <WaitingForTelemetry />
      )}
      <UplinkErrorToast message={errorMessage} />
    </>
  );
}
