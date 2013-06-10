/*
 ****************************************************************************
 * <p/>
 * COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.
 * <p/>
 * This software cannot be copied, stored, distributed without
 * Vinícius G.Mendonça prior authorization.
 * <p/>
 * is free to be redistributed or used under Creative Commons license 2.5 br:
 * http://creativecommons.org/licenses/by-sa/2.5/br/
 * <p/>
 * *****************************************************************************
 */

package br.com.vinigodoy.raytrace.math;

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
    private static final float EPSILON = 0.0001f;
    public float x;
    public float y;
    public float z;

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
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
     * Adds a very little component to the vector towards the given direction.
     * This method is useful to deviate the vector from it's origin point a bit,
     * in order to prevent ray collisions to the origin point itself.
     *
     * @param original  The original vector
     * @param direction Normalized direction to deviate
     * @return The deviated vector.
     */
    public static Vector3 deviate(Vector3 original, Vector3 direction) {
        return multiply(direction, EPSILON).add(original);
    }

    /**
     * Calculate the reflect vector from the giver direction bouncing in a surface with the given normal.
     *
     * @param direction The incident direction.
     * @param normal    The surface normal.
     * @return The reflection.
     */
    public static Vector3 reflect(Vector3 direction, Vector3 normal) {
        return subtract(direction, multiply(normal, 2.0f * direction.dot(normal)));
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
        x = -x;
        y = -y;
        z = -z;
        return this;
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
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
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
}
