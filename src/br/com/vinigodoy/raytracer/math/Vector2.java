/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math;


import java.util.Objects;

/**
 * Represents a vector in 2D coordinate space.
 * This class has two versions of most methods:
 * <ul>
 * <li>A class version, that changes the vector, representing unary operators like +=;</li>
 * <li>A static version, that does not change the given vectors, thus representing binary operators, like +</li>
 * </ul>
 * Class methods returns the vector itself, allowing invocation chaining e.g.:
 * <code>v1.add(v2).normalize();</code>
 */
public class Vector2 implements Cloneable {
    private float x;
    private float y;

    /**
     * Creates a zero vector.
     */
    public Vector2() {
        this(0, 0);
    }

    /**
     * Creates a vector with the given x and y components.
     *
     * @param x x value.
     * @param y y value.
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
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
     * Normalizes the given vector.
     *
     * @param vector The vector to normalize.
     * @return A normalized copy of the given vector.
     * @see #normalize()
     */
    public static Vector2 normalize(Vector2 vector) {
        return vector.clone().normalize();
    }

    /**
     * Negates the given vector
     *
     * @param vector Vector to negate
     * @return A negated copy of the given vector.
     */
    public static Vector2 negate(Vector2 vector) {
        return vector.clone().negate();
    }

    /**
     * Adds two vectors together.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The addition result.
     */
    public static Vector2 add(Vector2 v1, Vector2 v2) {
        return v1.clone().add(v2);
    }

    /**
     * Subtracts two vectors together.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The subtraction result.
     * @see #subtract(Vector2)
     */
    public static Vector2 subtract(Vector2 v1, Vector2 v2) {
        return v1.clone().subtract(v2);
    }

    /**
     * Calculates the perpendicular vector.
     *
     * @param v1 Vector 1
     * @return The perpendicular vector.
     */
    public static Vector2 perp(Vector2 v1) {
        return new Vector2(v1.x, -v1.y);
    }

    /**
     * Calculate dot product between the perpendicular of v1 and v2.
     * This operation is commonly known as the 2D cross product.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The perpendicular dot product.
     */
    public static float perpDot(Vector2 v1, Vector2 v2) {
        return perp(v1).dot(v2);
    }

    /**
     * Multiplies the vector by the given scalar
     *
     * @param vector The vector to multiply
     * @param scalar The scalar
     * @return The multiplication result.
     * @see #multiply(float)
     */
    public static Vector2 multiply(Vector2 vector, float scalar) {
        return vector.clone().multiply(scalar);
    }

    /**
     * Elevate each vector component by a given potency.
     *
     * @param vector  Vector to elevate
     * @param potency A scalar factor
     * @return A new vector, with the result.
     */
    public static Vector2 pow(Vector2 vector, float potency) {
        return vector.clone().pow(potency);
    }

    /**
     * Divides the vector by the given scalar.
     *
     * @param vector The vector to divide
     * @param scalar The scalar.
     * @return The division result.
     * @see #divide(float)
     */
    public static Vector2 divide(Vector2 vector, float scalar) {
        return vector.clone().divide(scalar);
    }


    /**
     * Truncates this vector component sizes to fit in [0,1] range.
     * Useful when this vector represents colors.
     *
     * @return A saturated copy of this vector.
     * @see #normalize()
     */
    public static Vector2 saturate(Vector2 vector) {
        return vector.clone().saturate();
    }

    /**
     * Changes all vector components
     *
     * @param x New x value
     * @param y New y value
     * @return This vector.
     */
    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * @return the size of this vector squared.
     */
    public float sizeSqr() {
        return x * x + y * y;
    }

    /**
     * @return the size of this vector
     */
    public float size() {
        return (float) Math.sqrt(sizeSqr());
    }

    /**
     * @return Turns this vector into an unitary vector.
     * @see Vector2#normalize(Vector2)
     */
    public Vector2 normalize() {
        return divide(size());
    }

    /**
     * Adds other vector to this vector.
     *
     * @param other The vector to add.
     * @return This vector after addition.
     * @see Vector2#add(Vector2, Vector2)
     */
    public Vector2 add(Vector2 other) {
        x += other.x;
        y += other.y;
        return this;
    }

    /**
     * Negates this vector, reversing it's side.
     *
     * @return This vector after negation.
     * @see Vector2#negate(Vector2)
     */
    public Vector2 negate() {
        return set(-x, -y);
    }

    /**
     * Subtracts the other vector from this one.
     *
     * @param other The vector to subtract to.
     * @return This vector after subtraction.
     * @see Vector2#subtract(Vector2, Vector2)
     */
    public Vector2 subtract(Vector2 other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    /**
     * Multiplies this vector by the given scalar.
     *
     * @param scalar The scalar.
     * @return This vector after multiplication.
     * @see Vector2#multiply(Vector2, float)
     */
    public Vector2 multiply(float scalar) {
        return set(x * scalar, y * scalar);
    }

    /**
     * Elevate each component by the given potency.
     *
     * @param potency The potency to elevate to.
     * @return The vector after the operation.
     */
    public Vector2 pow(float potency) {
        return set((float) Math.pow(x, potency), (float) Math.pow(y, potency));
    }

    /**
     * Divides this vector by the given scalar.
     *
     * @param scalar The scalar.
     * @return This vector divided.
     * @see Vector2#divide(Vector2, float)
     */
    public Vector2 divide(float scalar) {
        return multiply(1.0f / scalar);
    }

    /**
     * Calculates the dot product between this vector an the given one.
     *
     * @param other A vector.
     * @return The dot product.
     */
    public float dot(Vector2 other) {
        return x * other.x +
                y * other.y;
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
     * @see Vector2#saturate(Vector2)
     * @see #normalize()
     */
    public Vector2 saturate() {
        x = x < 0 ? 0 : (x > 1 ? 1 : x);
        y = y < 0 ? 0 : (y > 1 ? 1 : y);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        var other = (Vector2) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Vector2 clone() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return String.format("v(%.2f, %.2f)", x, y);
    }
}
