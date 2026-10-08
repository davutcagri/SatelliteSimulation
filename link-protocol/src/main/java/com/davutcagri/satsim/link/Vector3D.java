package com.davutcagri.satsim.link;

public record Vector3D(
        double x,
        double y,
        double z
) {

    public static final Vector3D ZERO = new Vector3D(0, 0, 0);

    public Vector3D plus(Vector3D other) {
        return new Vector3D(x + other.x, y + other.y, z + other.z);
    }

    public Vector3D minus(Vector3D other) {
        return new Vector3D(x - other.x, y - other.y, z - other.z);
    }

    public Vector3D times(double scalar) {
        return new Vector3D(x * scalar, y * scalar, z * scalar);
    }

    public Vector3D divide(double scalar) {
        return new Vector3D(x / scalar, y / scalar, z / scalar);
    }

    public double dot(Vector3D other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public Vector3D normalized() {
        return divide(magnitude());
    }
}
