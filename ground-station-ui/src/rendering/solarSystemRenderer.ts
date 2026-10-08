import {COLORS} from '../theme';
import type {Vector3} from '../types/telemetry';

const ORBIT_FILL_RATIO = 0.8;
const SUN_RADIUS_PIXELS = 14;
const EARTH_RADIUS_PIXELS = 6;
const LABEL_FONT = '12px system-ui, sans-serif';
const LABEL_OFFSET_PIXELS = 12;
const ORBIT_DASH_PIXELS = [4, 6];

function drawDisc(
  ctx: CanvasRenderingContext2D,
  x: number,
  y: number,
  radius: number,
  color: string,
  alpha = 1,
) {
  ctx.globalAlpha = alpha;
  ctx.fillStyle = color;
  ctx.beginPath();
  ctx.arc(x, y, radius, 0, Math.PI * 2);
  ctx.fill();
  ctx.globalAlpha = 1;
}

function drawLabel(ctx: CanvasRenderingContext2D, text: string, x: number, y: number, color: string) {
  ctx.fillStyle = color;
  ctx.font = LABEL_FONT;
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';
  ctx.fillText(text, x, y);
}

export function drawSolarSystemScene(
  ctx: CanvasRenderingContext2D,
  width: number,
  height: number,
  earthPositionMeters: Vector3,
) {
  const centerX = width / 2;
  const centerY = height / 2;
  const orbitRadiusMeters = Math.hypot(earthPositionMeters.x, earthPositionMeters.y);
  const orbitRadiusPixels = (Math.min(width, height) / 2) * ORBIT_FILL_RATIO;
  const pixelsPerMeter = orbitRadiusMeters > 0 ? orbitRadiusPixels / orbitRadiusMeters : 0;
  const earthX = centerX + earthPositionMeters.x * pixelsPerMeter;
  const earthY = centerY - earthPositionMeters.y * pixelsPerMeter;

  ctx.clearRect(0, 0, width, height);

  ctx.strokeStyle = COLORS.border;
  ctx.lineWidth = 1.5;
  ctx.setLineDash(ORBIT_DASH_PIXELS);
  ctx.beginPath();
  ctx.arc(centerX, centerY, orbitRadiusPixels, 0, Math.PI * 2);
  ctx.stroke();
  ctx.setLineDash([]);

  drawDisc(ctx, centerX, centerY, SUN_RADIUS_PIXELS, COLORS.sun);
  drawLabel(ctx, 'Sun', centerX, centerY + SUN_RADIUS_PIXELS + LABEL_OFFSET_PIXELS, COLORS.sun);

  drawDisc(ctx, earthX, earthY, EARTH_RADIUS_PIXELS, COLORS.earthEdge);
  drawLabel(ctx, 'Earth', earthX, earthY - LABEL_OFFSET_PIXELS - EARTH_RADIUS_PIXELS, COLORS.text);
}
