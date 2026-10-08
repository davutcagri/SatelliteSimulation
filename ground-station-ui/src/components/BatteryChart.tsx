import {useMemo} from 'react';
import {CartesianGrid, Line, LineChart, ReferenceLine, ResponsiveContainer, Tooltip, XAxis, YAxis,} from 'recharts';
import {CRITICAL_BATTERY_PERCENT, LOW_BATTERY_PERCENT} from '../config';
import {COLORS} from '../theme';
import type {TelemetryPacket} from '../types/telemetry';
import {formatHours, formatPercent, formatSimulationTime} from '../utils/format';
import styles from './BatteryChart.module.css';

const CHART_HEIGHT_PIXELS = 220;
const MAX_PERCENT = 100;
const AXIS_FONT_SIZE = 11;

interface BatteryChartProps {
  history: TelemetryPacket[];
}

export function BatteryChart({ history }: BatteryChartProps) {
  const points = useMemo(
    () =>
      history.map((packet) => ({
        timeSeconds: packet.clock.simulationTimeSeconds,
        batteryChargePercent: packet.power.batteryChargePercent,
      })),
    [history],
  );

  return (
    <div className={styles.chart}>
      <ResponsiveContainer width="100%" height={CHART_HEIGHT_PIXELS}>
        <LineChart data={points} margin={{ top: 8, right: 8, bottom: 0, left: -16 }}>
          <CartesianGrid stroke={COLORS.border} strokeDasharray="3 3" />
          <XAxis
            dataKey="timeSeconds"
            type="number"
            domain={['dataMin', 'dataMax']}
            tickFormatter={formatHours}
            stroke={COLORS.muted}
            tick={{ fontSize: AXIS_FONT_SIZE }}
          />
          <YAxis
            domain={[0, MAX_PERCENT]}
            stroke={COLORS.muted}
            tick={{ fontSize: AXIS_FONT_SIZE }}
          />
          <ReferenceLine y={LOW_BATTERY_PERCENT} stroke={COLORS.orange} strokeDasharray="4 4" />
          <ReferenceLine y={CRITICAL_BATTERY_PERCENT} stroke={COLORS.red} strokeDasharray="4 4" />
          <Tooltip
            contentStyle={{ background: COLORS.panel, border: `1px solid ${COLORS.border}` }}
            labelFormatter={(label) => formatSimulationTime(Number(label))}
            formatter={(value) => [formatPercent(Number(value)), 'Battery']}
          />
          <Line
            type="monotone"
            dataKey="batteryChargePercent"
            stroke={COLORS.accent}
            strokeWidth={2}
            dot={false}
            isAnimationActive={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
