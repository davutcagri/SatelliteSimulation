import { useEffect, useRef, useState } from 'react';
import type { CanvasSize } from '../rendering/canvasTypes';
import styles from './DrawingCanvas.module.css';

interface DrawingCanvasProps {
  label: string;
  draw: (ctx: CanvasRenderingContext2D, width: number, height: number) => void;
}

export function DrawingCanvas({ label, draw }: DrawingCanvasProps) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [size, setSize] = useState<CanvasSize>({ width: 0, height: 0 });

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const observer = new ResizeObserver(([entry]) => {
      setSize({ width: entry.contentRect.width, height: entry.contentRect.height });
    });
    observer.observe(canvas);
    return () => observer.disconnect();
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas?.getContext('2d');
    if (!canvas || !ctx || size.width === 0) return;
    const pixelRatio = window.devicePixelRatio || 1;
    canvas.width = size.width * pixelRatio;
    canvas.height = size.height * pixelRatio;
    ctx.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);
    draw(ctx, size.width, size.height);
  }, [draw, size]);

  return <canvas ref={canvasRef} className={styles.canvas} role="img" aria-label={label} />;
}
