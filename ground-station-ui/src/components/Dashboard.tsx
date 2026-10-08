import type {TelemetryPacket} from '../types/telemetry';
import type {SendUplink} from '../types/uplink';
import {AutonomyPanel} from './AutonomyPanel';
import {OrbitPanel} from './OrbitPanel';
import {PowerPanel} from './PowerPanel';
import {SolarSystemPanel} from './SolarSystemPanel';
import styles from './Dashboard.module.css';

interface DashboardProps {
  packet: TelemetryPacket;
  history: TelemetryPacket[];
  hasSignal: boolean;
  sendUplink: SendUplink;
}

export function Dashboard({ packet, history, hasSignal, sendUplink }: DashboardProps) {
  return (
    <main className={`${styles.grid} ${hasSignal ? '' : styles.stale}`}>
      <OrbitPanel orbit={packet.orbit} history={history} />
      <SolarSystemPanel earthPositionMeters={packet.orbit.earthPositionMeters} />
      <PowerPanel power={packet.power} sunlit={packet.orbit.sunlit} history={history} />
      <AutonomyPanel autonomy={packet.autonomy} sendUplink={sendUplink} />
    </main>
  );
}
