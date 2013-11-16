/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math;

import java.awt.*;

/**
 * Represents a vector in 3D coordinate space.
 * This class has two versions of most methods:
 * <ul>
 * <li>A class version, that changes the vector, representing unary operators like +=;</li>
 * <li>A static version, that does not change the given vectors, thus representing binary operators, like +</li>
 * </ul>
 * Class methods returns the vector itself, allowing invocation chaining e.g.:
 * <code>v1.add(v2).normalize();</code>
 */
public class Vector3 implements Cloneable {
    private float x;
    private float y;
    private float z;

    /**
     * Creates a zero vector.
     */
    public Vector3() {
        this(0, 0, 0);
    }

    /**
     * Creates a vector with the given x, y and z components.
     *
     * @param x x value.
     * @param y y value.
     * @param z z value.
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return The x component of this vector
     */
    public float getX() {
        return x;
    }

    /**
     * Sets this vector x component
     *
     * @param x component value
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return The y component of this vector
     */
    public float getY() {
        return y;
    }

    /**
     * Sets this vector x component
     *
     * @param y component value
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return The z component of this vector
     */
    public float getZ() {
        return z;
    }

    /**
     * Sets this vector x component
     *
     * @param z component value
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Normalizes the given vector.
     *
     * @param vector The vector to normalize.
     * @return A normalized copy of the given vector.
     * @see #normalize()
     */
    public static Vector3 normalize(Vector3 vector) {
        return vector.clone().normalize();
    }

    /**
     * Negates the given vector
     *
     * @param vector Vector to negate
     * @return A negated copy of the given vector.
     */
    public static Vector3 negate(Vector3 vector) {
        return vector.clone().negate();
    }

    /**
     * Adds two vectors together.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The addition result.
     */
    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return v1.clone().add(v2);
    }

    /**
     * Subtracts two vectors together.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The subtraction result.
     * @see #subtract(Vector3)
     */
    public static Vector3 subtract(Vector3 v1, Vector3 v2) {
        return v1.clone().subtract(v2);
    }

    /**
     * Calculate the cross product between two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The cross product.
     */
    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x);
    }

    /**
     * Multiplies the vector by the given scalar
     *
     * @param vector The vector to multiply
     * @param scalar The scalar
     * @return The multiplication result.
     * @see #multiply(float)
     * @see #mul(Vector3, Vector3)
     */
    public static Vector3 multiply(Vector3 vector, float scalar) {
        return vector.clone().multiply(scalar);
    }

    /**
     * Elevate each vector component by a given potency.
     *
     * @param vector  Vector to elevate
     * @param potency A scalar factor
     * @return A new vector, with the result.
     */
    public static Vector3 pow(Vector3 vector, float potency) {
        return vector.clone().pow(potency);
    }

    public static Vector3 rotate(Vector3 vector, Vector3 axis, float radians) {
        return vector.clone().rotate(axis, radians);
    }

    /**
     * Divides the vector by the given scalar.
     *
     * @param vector The vector to divide
     * @param scalar The scalar.
     * @return The division result.
     * @see #divide(float)
     */
    public static Vector3 divide(Vector3 vector, float scalar) {
        return vector.clone().divide(scalar);
    }

    /**
     * Multiplicates every component. This is not a standard vector operation, but it's usefull when the vector
     * describes materials and lights.
     *
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return The multiplied vectors.
     */
    public static Vector3 mul(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }

    /**
     * Truncates this vector component sizes to fit in [0,1] range.
     * Useful when this vector represents colors.
     *
     * @return A saturated copy of this vector.
     * @see #normalize()
     */
    public static Vector3 saturate(Vector3 vector) {
        return vector.clone().saturate();
    }

    /**
     * Calculate the reflection vector from the given direction bouncing in a surface with the given normal.
     *
     * @param direction The incident direction.
     * @param normal    The surface normal.
     * @return The reflection.
     */
    public static Vector3 reflect(Vector3 direction, Vector3 normal) {
        direction = negate(direction);
        return subtract(direction, multiply(normal, 2.0f * direction.dot(normal)));
    }

    /**
     * Calculate the refraction vector from the given direction, deviating in a surface from the given normal,
     * comming from a r1 refraction index region to a r2 refraction index region.
     *
     * @param direction Ray direction
     * @param normal    Surface normal
     * @param r1        Source refraction index
     * @param r2        Destination refraction index.
     * @return The refracted ray.
     */
    public static Vector3 refract(Vector3 direction, Vector3 normal, float r1, float r2) {
        float n = r1 / r2;
        float cosI = normal.dot(direction);
        float cosT2 = 1.0f - n * n * (1.0f - cosI * cosI);
        if (cosT2 <= 0)
            return null; //No refraction
        return multiply(direction, n).add(multiply(normal, n * cosI - (float) Math.sqrt(cosT2)));
    }

    /**
     * Changes all three vector components
     *
     * @param x New x value
     * @param y New y value
     * @param z New z value
     * @return This vector.
     */
    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * @return the size of this vector squared.
     */
    public float sizeSqr() {
        return x * x + y * y + z * z;
    }

    /**
     * @return the size of this vector
     */
    public float size() {
        return (float) Math.sqrt(sizeSqr());
    }

    /**
     * @return Turns this vector into an unitary vector.
     * @see Vector3#normalize(Vector3)
     */
    public Vector3 normalize() {
        return divide(size());
    }

    /**
     * Adds other vector to this vector.
     *
     * @param other The vector to add.
     * @return This vector after addition.
     * @see Vector3#add(Vector3, Vector3)
     */
    public Vector3 add(Vector3 other) {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }

    /**
     * Negates this vector, reversing it's side.
     *
     * @return This vector after negation.
     * @see Vector3#negate(Vector3)
     */
    public Vector3 negate() {
        return set(-x, -y, -z);
    }

    /**
     * Subtracts the other vector from this one.
     *
     * @param other The vector to subtract to.
     * @return This vector after subtraction.
     * @see Vector3#subtract(Vector3, Vector3)
     */
    public Vector3 subtract(Vector3 other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        return this;
    }

    /**
     * Multiplies this vector by the given scalar.
     *
     * @param scalar The scalar.
     * @return This vector after multiplication.
     * @see Vector3#multiply(Vector3, float)
     * @see Vector3#mul(Vector3, Vector3)
     */
    public Vector3 multiply(float scalar) {
        return set(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Elevate each component by the given potency.
     *
     * @param potency The potency to elevate to.
     * @return The vector after the operation.
     */
    public Vector3 pow(float potency) {
        return set((float) Math.pow(x, potency), (float) Math.pow(y, potency), (float) Math.pow(z, potency));
    }

    /**
     * Divides this vector by the given scalar.
     *
     * @param scalar The scalar.
     * @return This vector divided.
     * @see Vector3#divide(Vector3, float)
     */
    public Vector3 divide(float scalar) {
        return multiply(1.0f / scalar);
    }

    /**
     * Calculates the dot product between this vector an the given one.
     *
     * @param other A vector.
     * @return The dot product.
     */
    public float dot(Vector3 other) {
        return x * other.x +
                y * other.y +
                z * other.z;
    }

    /**
     * Calculate the distance between this point and another one.
     *
     * @param other Other point
     * @return The distance
     */
    public float distance(Vector3 other) {
        return subtract(this, other).sizeSqr();
    }

    /**
     * @return Trus if this is a unitary (normal) vector.
     */
    public boolean isUnit() {
        return sizeSqr() == 1.0f;
    }

    /**
     * @return True if this is the zero vector.
     */
    public boolean isZero() {
        return sizeSqr() == 0;
    }

    /**
     * Truncates this vector component sizes to fit in [0,1] range.
     * Useful when this vector represents colors.
     *
     * @return This vector.
     * @see Vector3#saturate(Vector3)
     * @see #normalize()
     */
    public Vector3 saturate() {
        float max = Math.max(Math.max(x, y), z);
        return max > 1.0f ? divide(max) : this;
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

    /**
     * Converts this vector to a Color.
     *
     * @return The color.
     * @see Color
     * @see Color#Color(float, float, float)
     */
    public Color toColor() {
        return new Color(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("v(%.2f, %.2f, %.2f)", x, y, z);
    }

    public Vector3 rotate(Vector3 axis, float radians) {
        float s = (float) Math.sin(radians);
        float c = (float) Math.cos(radians);
        float k = 1.0F - c;

        float nx = x * (c + k * axis.x * axis.x) +
                y * (k * axis.x * axis.y - s * axis.z) +
                z * (k * axis.x * axis.z + s * axis.y);

        float ny = x * (k * axis.x * axis.y + s * axis.z) +
                y * (c + k * axis.y * axis.y) +
                z * (k * axis.y * axis.z - s * axis.x);

        float nz = x * (k * axis.x * axis.z - s * axis.y) +
                y * (k * axis.y * axis.z + s * axis.x) +
                z * (c + k * axis.z * axis.z);

        return set(nx, ny, nz);
    }
}
