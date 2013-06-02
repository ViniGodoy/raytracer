package br.com.vinigodoy.math;

import java.awt.*;

/******************************************************************************
 *
 * COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.
 *
 * This software cannot be copied, stored, distributed without
 * Vinícius G.Mendonça prior authorization.
 *
 * is free to be redistributed or used under Creative Commons license 2.5 br:
 * http://creativecommons.org/licenses/by-sa/2.5/br/
 *
 *******************************************************************************/
public class Vector3 implements Cloneable {
    public float x;
    public float y;
    public float z;

    public Vector3() {
        this(0,0,0);
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float sizeSqr()
    {
        return x*x + y*y + z*z;
    }

    public float size()
    {
        return (float) Math.sqrt(sizeSqr());
    }

    public Vector3 normalize()
    {
        return divide(size());
    }

    public Vector3 add(Vector3 other)
    {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }

    public Vector3 negate()
    {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector3 subtract(Vector3 other)
    {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        return this;
    }

    public Vector3 multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    public Vector3 divide(float scalar) {
        return multiply(1.0f / scalar);
    }

    public float dot(Vector3 other) {
        return x*other.x +
               y * other.y +
               z * other.z;
    }

    public Vector3 cross(Vector3 v)
    {
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
    }

    public boolean isUnit() {
        return sizeSqr() == 1.0f;
    }

    public boolean isZero() {
        return sizeSqr() == 0;
    }

    /**
     * Truncates this vector component sizes to fit in [0,1] range.
     * Useful when this vector represents colors.
     * @return This vector.
     * @see #normalize()
     */
    public Vector3 saturate()
    {
        x = x < 0 ? 0 : (x > 1 ? 1 : x);
        y = y < 0 ? 0 : (y > 1 ? 1 : y);
        z = z < 0 ? 0 : (z > 1 ? 1 : z);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Vector3 other = (Vector3) obj;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public static Vector3 normalize(Vector3 vector) {
        return vector.clone().normalize();
    }

    public static Vector3 negate(Vector3 vector) {
        return vector.clone().negate();
    }

    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return v1.clone().add(v2);
    }

    public static Vector3 subtract(Vector3 v1, Vector3 v2) {
        return v1.clone().subtract(v2);
    }

    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return v1.cross(v2);
    }

    public static Vector3 multiply(Vector3 vector, float scalar) {
        return vector.clone().multiply(scalar);
    }

    public Color toColor() {
        return new Color(x, y, z);
    }

    /**
     * Multiplicates every component. This is not a standard vector operation, but it's usefull when the vector
     * describes materials and lights.
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return The multiplied vectors.
     */
    public static Vector3 mul(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }

    public static Vector3 saturate(Vector3 vector)
    {
        return vector.clone().saturate();
    }
}
