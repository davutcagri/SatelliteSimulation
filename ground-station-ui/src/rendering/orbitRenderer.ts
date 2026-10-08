import {EARTH_RADIUS_METERS} from '../config';
import {COLORS} from '../theme';
import type {OrbitState, Vector3} from '../types/telemetry';
import type {CanvasSize, ScreenPoint} from './canvasTypes';

const VIEW_RADIUS_METERS = 14_000_000;
const MIN_DIRECTION_LENGTH = 1e-9;
const SUN_ARROW_START_RATIO = 0.58;
const SUN_ARROW_END_RATIO = 0.7;
const SUN_LABEL_OFFSET_PIXELS = 28;
const ARROW_HEAD_PIXELS = 8;
const ARROW_HEAD_SPREAD_RADIANS = 0.45;
const SATELLITE_RADIUS_PIXELS = 5;
const TRAIL_MAX_ALPHA = 0.8;
const TRAIL_WIDTH_PIXELS = 1.5;
const LABEL_FONT = '12px system-ui, sans-serif';

export interface OrbitScene {
  orbit: OrbitState;
  trail: Vector3[];
}

interface Viewport {
  center: ScreenPoint;
  pixelsPerMeter: number;
  size: CanvasSize;
}

function createViewport(size: CanvasSize): Viewport {
  const halfExtent = Math.min(size.width, size.height) / 2;
  return {
    center: { x: size.width / 2, y: size.height / 2 },
    pixelsPerMeter: halfExtent / VIEW_RADIUS_METERS,
    size,
  };
}

function project(position: Vector3, viewport: Viewport): ScreenPoint {
  return {
    x: viewport.center.x + position.x * viewport.pixelsPerMeter,
    y: viewport.center.y - position.z * viewport.pixelsPerMeter,
  };
}

function screenDirectionOf(direction: Vector3): ScreenPoint | null {
  const length = Math.hypot(direction.x, direction.z);
  if (length < MIN_DIRECTION_LENGTH) return null;
  return { x: direction.x / length, y: -direction.z / length };
}

function pointAlong(viewport: Viewport, direction: ScreenPoint, ratio: number): ScreenPoint {
  const halfExtent = viewport.pixelsPerMeter * VIEW_RADIUS_METERS;
  return {
    x: viewport.center.x + direction.x * halfExtent * ratio,
    y: viewport.center.y + direction.y * halfExtent * ratio,
  };
}

function drawShadow(ctx: CanvasRenderingContext2D, viewport: Viewport, sunDirection: ScreenPoint) {
  const radius = EARTH_RADIUS_METERS * viewport.pixelsPerMeter;
  const length = viewport.size.width + viewport.size.height;
  const side = { x: -sunDirection.y * radius, y: sunDirection.x * radius };
  const far = {
    x: viewport.center.x - sunDirection.x * length,
    y: viewport.center.y - sunDirection.y * length,
  };
  ctx.fillStyle = COLORS.shadow;
  ctx.beginPath();
  ctx.moveTo(viewport.center.x + side.x, viewport.center.y + side.y);
  ctx.lineTo(far.x + side.x, far.y + side.y);
  ctx.lineTo(far.x - side.x, far.y - side.y);
  ctx.lineTo(viewport.center.x - side.x, viewport.center.y - side.y);
  ctx.closePath();
  ctx.fill();
}

function drawEarth(ctx: CanvasRenderingContext2D, viewport: Viewport) {
  const radius = EARTH_RADIUS_METERS * viewport.pixelsPerMeter;
  ctx.fillStyle = COLORS.earth;
  ctx.strokeStyle = COLORS.earthEdge;
  ctx.lineWidth = 1.5;
  ctx.beginPath();
  ctx.arc(viewport.center.x, viewport.center.y, radius, 0, Math.PI * 2);
  ctx.fill();
  ctx.stroke();
  ctx.fillStyle = COLORS.text;
  ctx.font = LABEL_FONT;
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';
  ctx.fillText('Earth', viewport.center.x, viewport.center.y);
}

function drawTrail(ctx: CanvasRenderingContext2D, viewport: Viewport, trail: Vector3[]) {
  ctx.lineWidth = TRAIL_WIDTH_PIXELS;
  ctx.lineCap = 'round';
  for (let index = 1; index < trail.length; index++) {
    const alpha = (index / trail.length) * TRAIL_MAX_ALPHA;
    const from = project(trail[index - 1], viewport);
    const to = project(trail[index], viewport);
    ctx.strokeStyle = `rgba(26, 86, 165, ${alpha})`;
    ctx.beginPath();
    ctx.moveTo(from.x, from.y);
    ctx.lineTo(to.x, to.y);
    ctx.stroke();
  }
}

function drawArrow(ctx: CanvasRenderingContext2D, from: ScreenPoint, to: ScreenPoint) {
  const angle = Math.atan2(to.y - from.y, to.x - from.x);
  ctx.strokeStyle = COLORS.sun;
  ctx.fillStyle = COLORS.sun;
  ctx.lineWidth = 2;
  ctx.beginPath();
  ctx.moveTo(from.x, from.y);
  ctx.lineTo(to.x, to.y);
  ctx.stroke();
  ctx.beginPath();
  ctx.moveTo(to.x, to.y);
  for (const side of [-1, 1]) {
    const headAngle = angle + Math.PI + side * ARROW_HEAD_SPREAD_RADIANS;
    ctx.lineTo(
      to.x + Math.cos(headAngle) * ARROW_HEAD_PIXELS,
      to.y + Math.sin(headAngle) * ARROW_HEAD_PIXELS,
    );
    ctx.moveTo(to.x, to.y);
  }
  ctx.stroke();
}

function drawSunDirection(ctx: CanvasRenderingContext2D, viewport: Viewport, sun: ScreenPoint) {
  drawArrow(
    ctx,
    pointAlong(viewport, sun, SUN_ARROW_START_RATIO),
    pointAlong(viewport, sun, SUN_ARROW_END_RATIO),
  );
  const arrowEnd = pointAlong(viewport, sun, SUN_ARROW_END_RATIO);
  const label = {
    x: arrowEnd.x + sun.x * SUN_LABEL_OFFSET_PIXELS,
    y: arrowEnd.y + sun.y * SUN_LABEL_OFFSET_PIXELS,
  };
  ctx.fillStyle = COLORS.sun;
  ctx.font = LABEL_FONT;
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';
  ctx.fillText('Sun', label.x, label.y);
}

function drawSatellite(
  ctx: CanvasRenderingContext2D,
  viewport: Viewport,
  position: Vector3,
  sunlit: boolean,
) {
  const point = project(position, viewport);
  const color = sunlit ? COLORS.sun : COLORS.satelliteShade;
  ctx.fillStyle = color;
  ctx.beginPath();
  ctx.arc(point.x, point.y, SATELLITE_RADIUS_PIXELS, 0, Math.PI * 2);
  ctx.fill();
}

export function drawOrbitScene(
  ctx: CanvasRenderingContext2D,
  width: number,
  height: number,
  scene: OrbitScene,
) {
  const viewport = createViewport({ width, height });
  const sun = screenDirectionOf(scene.orbit.sunDirection);
  ctx.clearRect(0, 0, width, height);
  if (sun) drawShadow(ctx, viewport, sun);
  drawEarth(ctx, viewport);
  drawTrail(ctx, viewport, scene.trail);
  if (sun) drawSunDirection(ctx, viewport, sun);
  drawSatellite(ctx, viewport, scene.orbit.satellitePositionMeters, scene.orbit.sunlit);
}
