/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math;


/**
 * Represents a 4x4 matrix.
 */
public final class Matrix4 implements Cloneable {
    private float A[][] = new float[4][4];

    /**
     * Creates a new zeroed matrix.
     */
    public Matrix4() {
    }

    /**
     * Creates a new matrix with the given values.
     * <pre>
     * | m00 m01 m02 m03 |
     * | m10 m11 m12 m13 |
     * | m20 m21 m22 m23 |
     * | m30 m31 m32 m33 |
     * </pre>
     */
    public Matrix4(float m00, float m01, float m02, float m03,
                   float m10, float m11, float m12, float m13,
                   float m20, float m21, float m22, float m23,
                   float m30, float m31, float m32, float m33) {
        set(m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33);
    }

    /**
     * Creates a new matrix with the given values.
     * <pre>
     * | m00 m01 m02 0 |
     * | m10 m11 m12 0 |
     * | m20 m21 m22 0 |
     * |  0   0   0  1 |
     * </pre>
     */
    public Matrix4(float m00, float m01, float m02,
                   float m10, float m11, float m12,
                   float m20, float m21, float m22) {
        set(m00, m01, m02,
                m10, m11, m12,
                m20, m21, m22);
    }

    /**
     * Creates a new matrix with the given values.
     * <pre>
     * | m0x m0y m0z 0 |
     * | m1x m1y m1z 0 |
     * | m2x m2y m2z 0 |
     * |  0   0   0  1 |
     * </pre>
     */
    public Matrix4(Vector3 m0, Vector3 m1, Vector3 m2) {
        set(m0, m1, m2);
    }

    /**
     * Creates an identity matrix
     */
    public static Matrix4 newIdentity() {
        return new Matrix4(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Creates a translation matrix.
     *
     * @param x X displacement
     * @param y Y displacement
     * @param z Z displacement
     * @return The translation matrix.
     */
    public static Matrix4 newTranslation(float x, float y, float z) {
        return new Matrix4(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1);
    }

    /**
     * Creates a translation matrix.
     *
     * @param translation The displacement
     * @return The translation matrix.
     */
    public static Matrix4 newTranslation(Vector3 translation) {
        return newTranslation(translation.getX(), translation.getY(), translation.getZ());
    }

    /**
     * Create a scale matrix
     *
     * @param x Scale in x axis
     * @param y Scale in y axis
     * @param z Scale in z axis
     * @return The scale matrix.
     */
    public static Matrix4 newScale(float x, float y, float z) {
        return new Matrix4(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1);
    }

    /**
     * Create an uniform scale matrix.
     *
     * @param scale The scale.
     * @return The scale matrix.
     */
    public static Matrix4 newScale(float scale) {
        return newScale(scale, scale, scale);
    }

    /**
     * Create a matrix to rotate around the x axis.
     *
     * @param angle The angle, in radians.
     * @return The new matrix.
     */
    public static Matrix4 newRotationX(float angle) {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);
        return new Matrix4(
                1, 0, 0, 0,
                0, cosA, -sinA, 0,
                0, sinA, cosA, 0,
                0, 0, 0, 1);
    }

    /**
     * Create a matrix to rotate around the y axis.
     *
     * @param angle The angle, in radians.
     * @return The new matrix.
     */
    public static Matrix4 newRotationY(float angle) {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);
        return new Matrix4(
                cosA, 0.0f, sinA, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                -sinA, 0.0f, cosA, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Create a matrix to rotate around the z axis.
     *
     * @param angle The angle, in radians.
     * @return The new matrix.
     */
    public static Matrix4 newRotationZ(float angle) {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);
        return new Matrix4(
                cosA, -sinA, 0.0f, 0.0f,
                sinA, cosA, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Creates a X reflection matrix
     *
     * @return The scale matrix
     */
    public static Matrix4 newReflectionX() {
        return newScale(-1, 1, 1);
    }

    /**
     * Creates a Y reflection matrix
     *
     * @return The scale matrix
     */
    public static Matrix4 newReflectionY() {
        return newScale(1, -1, 1);
    }

    /**
     * Creates a Z reflection matrix
     *
     * @return The scale matrix
     */
    public static Matrix4 newReflectionZ() {
        return newScale(1, 1, -1);
    }

    /**
     * Creates a shearing matrix
     *
     * @param xy Shearing factor.
     * @param xz Shearing factor.
     * @param yx Shearing factor.
     * @param yz Shearing factor.
     * @param zx Shearing factor.
     * @param zy Shearing factor.
     * @return The shearing matrix.
     */
    public static Matrix4 newShearing(float xy, float xz, float yx, float yz, float zx, float zy) {
        return new Matrix4(
                1, yx, zx, 0,
                xy, 1, zy, 0,
                xz, yz, 1, 0,
                0, 0, 0, 1);
    }

    /**
     * Creates an inverse transformation matrix.
     *
     * @param x The x displacement.
     * @param y The y displacement.
     * @param z The z displacement.
     * @return The inverse transformation matrix.
     */
    public static Matrix4 newInvTranslation(float x, float y, float z) {
        return newTranslation(-x, -y, -z);
    }

    /**
     * Creates an inverse transformation matrix.
     *
     * @param translation The translation.
     * @return The inverse transformation matrix.
     */
    public static Matrix4 newInvTranslation(Vector3 translation) {
        return newInvTranslation(translation.getX(), translation.getY(), translation.getZ());
    }

    /**
     * Creates an inverse scale matrix.
     *
     * @param x The scale in x axis.
     * @param y The scale in y axis.
     * @param z The scale in z axis.
     * @return The inverse scale matrix.
     */
    public static Matrix4 newInvScale(float x, float y, float z) {
        return newScale(1.0f / x, 1.0f / y, 1.0f / z);
    }

    /**
     * Creates an inverse uniform scale matrix.
     *
     * @param scale The scale.
     * @return The inverse uniform scale matrix.
     */
    public static Matrix4 newInvScale(float scale) {
        return newInvScale(scale, scale, scale);
    }

    /**
     * Create an inverse matrix to rotate around the x axis.
     *
     * @param angle The angle, in radians.
     * @return The new matrix.
     */
    public static Matrix4 newInvRotationX(float angle) {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);
        return new Matrix4(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, cosA, sinA, 0.0f,
                0.0f, -sinA, cosA, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Create an inverse matrix to rotate around the y axis.
     *
     * @param angle The angle, in radians.
     * @return The new matrix.
     */
    public static Matrix4 newInvRotationY(float angle) {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);
        return new Matrix4(
                cosA, 0.0f, -sinA, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                sinA, 0.0f, cosA, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Create an inverse matrix to rotate around the z axis.
     *
     * @param angle The angle, in radians.
     * @return The new matrix.
     */
    public static Matrix4 newInvRotationZ(float angle) {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);
        return new Matrix4(
                cosA, sinA, 0.0f, 0.0f,
                -sinA, cosA, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Creates an inverse shearing transformation
     *
     * @param xy Shear factor.
     * @param xz Shear factor.
     * @param yx Shear factor.
     * @param yz Shear factor.
     * @param zx Shear factor.
     * @param zy Shear factor.
     * @return The inverse shearing.
     */
    public static Matrix4 newInvShearing(float xy, float xz, float yx, float yz, float zx, float zy) {
        float D = 1 - xy * yx - xz * zx - yz * zy + xy * yz * zx + xz * yx * zy;

        return new Matrix4(
                1 - yz * zy, -yx + yz * zx, -zx + yx * zy, 0,
                -xy + xz * zy, 1 - xz * zx, -zy + xy * zx, 0,
                -xz + xy * yz, -yz + xz * yx, 1 - xy * yx, 0,
                0, 0, 0, D)
                .multiply(1.0f / D);
    }

    /**
     * Adds two matrices
     *
     * @param m1 Matrix 1
     * @param m2 Matrix 2
     * @return A new matrix with the result.
     */
    public static Matrix4 add(Matrix4 m1, Matrix4 m2) {
        return m1.clone().add(m2);
    }

    /**
     * Subtracts two matrices
     *
     * @param m1 Matrix 1
     * @param m2 Matrix 2
     * @return A new matrix with the result.
     */
    public static Matrix4 subtract(Matrix4 m1, Matrix4 m2) {
        return m1.clone().add(m2);
    }

    /**
     * Multiplies two matrices
     *
     * @param m1 Matrix 1
     * @param m2 Matrix 2
     * @return A new matrix with the result.
     */
    public static Matrix4 multiply(Matrix4 m1, Matrix4 m2) {
        Matrix4 C = new Matrix4();
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                for (int k = 0; k < 4; ++k)
                    C.A[i][j] += m1.A[i][k] * m2.A[k][j];

        return C;
    }

    /**
     * Adds a matrix by the given scalar
     *
     * @param m      The matrix
     * @param scalar Scalar
     * @return A new matrix with the result.
     */
    public static Matrix4 multiply(Matrix4 m, float scalar) {
        return m.clone().multiply(scalar);
    }

    /**
     * Adds a matrix by the given scalar
     *
     * @param m      The matrix
     * @param scalar Scalar
     * @return A new matrix with the result.
     */
    public static Matrix4 divide(Matrix4 m, float scalar) {
        return m.clone().divide(scalar);
    }

    /**
     * Inverses a matrix.
     *
     * @param m The matrix
     * @return A new matrix with the result.
     */
    public static Matrix4 inverse(Matrix4 m) {
        Matrix4 inv = new Matrix4();

        //Calculate the classical adjoint of m --> adj(m)
        inv.A[0][0] = m.get(1, 1) * m.get(2, 2) * m.get(3, 3) -
                m.get(1, 1) * m.get(2, 3) * m.get(3, 2) -
                m.get(2, 1) * m.get(1, 2) * m.get(3, 3) +
                m.get(2, 1) * m.get(1, 3) * m.get(3, 2) +
                m.get(3, 1) * m.get(1, 2) * m.get(2, 3) -
                m.get(3, 1) * m.get(1, 3) * m.get(2, 2);

        inv.A[0][1] = -m.get(0, 1) * m.get(2, 2) * m.get(3, 3) +
                m.get(0, 1) * m.get(2, 3) * m.get(3, 2) +
                m.get(2, 1) * m.get(0, 2) * m.get(3, 3) -
                m.get(2, 1) * m.get(0, 3) * m.get(3, 2) -
                m.get(3, 1) * m.get(0, 2) * m.get(2, 3) +
                m.get(3, 1) * m.get(0, 3) * m.get(2, 2);

        inv.A[0][2] = m.get(0, 1) * m.get(1, 2) * m.get(3, 3) -
                m.get(0, 1) * m.get(1, 3) * m.get(3, 2) -
                m.get(1, 1) * m.get(0, 2) * m.get(3, 3) +
                m.get(1, 1) * m.get(0, 3) * m.get(3, 2) +
                m.get(3, 1) * m.get(0, 2) * m.get(1, 3) -
                m.get(3, 1) * m.get(0, 3) * m.get(1, 2);

        inv.A[0][3] = -m.get(0, 1) * m.get(1, 2) * m.get(2, 3) +
                m.get(0, 1) * m.get(1, 3) * m.get(2, 2) +
                m.get(1, 1) * m.get(0, 2) * m.get(2, 3) -
                m.get(1, 1) * m.get(0, 3) * m.get(2, 2) -
                m.get(2, 1) * m.get(0, 2) * m.get(1, 3) +
                m.get(2, 1) * m.get(0, 3) * m.get(1, 2);

        inv.A[1][0] = -m.get(1, 0) * m.get(2, 2) * m.get(3, 3) +
                m.get(1, 0) * m.get(2, 3) * m.get(3, 2) +
                m.get(2, 0) * m.get(1, 2) * m.get(3, 3) -
                m.get(2, 0) * m.get(1, 3) * m.get(3, 2) -
                m.get(3, 0) * m.get(1, 2) * m.get(2, 3) +
                m.get(3, 0) * m.get(1, 3) * m.get(2, 2);

        inv.A[1][1] = m.get(0, 0) * m.get(2, 2) * m.get(3, 3) -
                m.get(0, 0) * m.get(2, 3) * m.get(3, 2) -
                m.get(2, 0) * m.get(0, 2) * m.get(3, 3) +
                m.get(2, 0) * m.get(0, 3) * m.get(3, 2) +
                m.get(3, 0) * m.get(0, 2) * m.get(2, 3) -
                m.get(3, 0) * m.get(0, 3) * m.get(2, 2);

        inv.A[1][2] = -m.get(0, 0) * m.get(1, 2) * m.get(3, 3) +
                m.get(0, 0) * m.get(1, 3) * m.get(3, 2) +
                m.get(1, 0) * m.get(0, 2) * m.get(3, 3) -
                m.get(1, 0) * m.get(0, 3) * m.get(3, 2) -
                m.get(3, 0) * m.get(0, 2) * m.get(1, 3) +
                m.get(3, 0) * m.get(0, 3) * m.get(1, 2);

        inv.A[1][3] = m.get(0, 0) * m.get(1, 2) * m.get(2, 3) -
                m.get(0, 0) * m.get(1, 3) * m.get(2, 2) -
                m.get(1, 0) * m.get(0, 2) * m.get(2, 3) +
                m.get(1, 0) * m.get(0, 3) * m.get(2, 2) +
                m.get(2, 0) * m.get(0, 2) * m.get(1, 3) -
                m.get(2, 0) * m.get(0, 3) * m.get(1, 2);

        inv.A[2][0] = m.get(1, 0) * m.get(2, 1) * m.get(3, 3) -
                m.get(1, 0) * m.get(2, 3) * m.get(3, 1) -
                m.get(2, 0) * m.get(1, 1) * m.get(3, 3) +
                m.get(2, 0) * m.get(1, 3) * m.get(3, 1) +
                m.get(3, 0) * m.get(1, 1) * m.get(2, 3) -
                m.get(3, 0) * m.get(1, 3) * m.get(2, 1);

        inv.A[2][1] = -m.get(0, 0) * m.get(2, 1) * m.get(3, 3) +
                m.get(0, 0) * m.get(2, 3) * m.get(3, 1) +
                m.get(2, 0) * m.get(0, 1) * m.get(3, 3) -
                m.get(2, 0) * m.get(0, 3) * m.get(3, 1) -
                m.get(3, 0) * m.get(0, 1) * m.get(2, 3) +
                m.get(3, 0) * m.get(0, 3) * m.get(2, 1);

        inv.A[2][2] = m.get(0, 0) * m.get(1, 1) * m.get(3, 3) -
                m.get(0, 0) * m.get(1, 3) * m.get(3, 1) -
                m.get(1, 0) * m.get(0, 1) * m.get(3, 3) +
                m.get(1, 0) * m.get(0, 3) * m.get(3, 1) +
                m.get(3, 0) * m.get(0, 1) * m.get(1, 3) -
                m.get(3, 0) * m.get(0, 3) * m.get(1, 1);

        inv.A[2][3] = -m.get(0, 0) * m.get(1, 1) * m.get(2, 3) +
                m.get(0, 0) * m.get(1, 3) * m.get(2, 1) +
                m.get(1, 0) * m.get(0, 1) * m.get(2, 3) -
                m.get(1, 0) * m.get(0, 3) * m.get(2, 1) -
                m.get(2, 0) * m.get(0, 1) * m.get(1, 3) +
                m.get(2, 0) * m.get(0, 3) * m.get(1, 1);

        inv.A[3][0] = -m.get(1, 0) * m.get(2, 1) * m.get(3, 2) +
                m.get(1, 0) * m.get(2, 2) * m.get(3, 1) +
                m.get(2, 0) * m.get(1, 1) * m.get(3, 2) -
                m.get(2, 0) * m.get(1, 2) * m.get(3, 1) -
                m.get(3, 0) * m.get(1, 1) * m.get(2, 2) +
                m.get(3, 0) * m.get(1, 2) * m.get(2, 1);

        inv.A[3][1] = m.get(0, 0) * m.get(2, 1) * m.get(3, 2) -
                m.get(0, 0) * m.get(2, 2) * m.get(3, 1) -
                m.get(2, 0) * m.get(0, 1) * m.get(3, 2) +
                m.get(2, 0) * m.get(0, 2) * m.get(3, 1) +
                m.get(3, 0) * m.get(0, 1) * m.get(2, 2) -
                m.get(3, 0) * m.get(0, 2) * m.get(2, 1);

        inv.A[3][2] = -m.get(0, 0) * m.get(1, 1) * m.get(3, 2) +
                m.get(0, 0) * m.get(1, 2) * m.get(3, 1) +
                m.get(1, 0) * m.get(0, 1) * m.get(3, 2) -
                m.get(1, 0) * m.get(0, 2) * m.get(3, 1) -
                m.get(3, 0) * m.get(0, 1) * m.get(1, 2) +
                m.get(3, 0) * m.get(0, 2) * m.get(1, 1);

        inv.A[3][3] = m.get(0, 0) * m.get(1, 1) * m.get(2, 2) -
                m.get(0, 0) * m.get(1, 2) * m.get(2, 1) -
                m.get(1, 0) * m.get(0, 1) * m.get(2, 2) +
                m.get(1, 0) * m.get(0, 2) * m.get(2, 1) +
                m.get(2, 0) * m.get(0, 1) * m.get(1, 2) -
                m.get(2, 0) * m.get(0, 2) * m.get(1, 1);

        //Calculate the determinant of m --> det(m)
        //Avoid calling determinant() since some values are already precalculated in the adjoint
        float det = m.get(0, 0) * inv.get(0, 0) +
                m.get(0, 1) * inv.get(1, 0) +
                m.get(0, 2) * inv.get(2, 0) +
                m.get(0, 3) * inv.get(3, 0);

        //Calculate the inverse (that is equals to adj(M) / det(M))
        det = 1.0f / det;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                inv.A[i][j] = inv.get(i, j) * det;

        return inv;
    }

    /**
     * Transposes the given matrix.
     *
     * @param m The matrix
     * @return A new matrix with the result.
     */
    public static Matrix4 transpose(Matrix4 m) {
        Matrix4 r = new Matrix4();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                r.A[i][j] = m.A[j][i];
            }
        return r;
    }

    /**
     * Sets this matrix values as
     * <pre>
     * | m00 m01 m02 m03 |
     * | m10 m11 m12 m13 |
     * | m20 m21 m22 m23 |
     * | m30 m31 m32 m33 |
     * </pre>
     *
     * @return This matrix
     */
    public Matrix4 set(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        A[0][0] = m00;
        A[0][1] = m01;
        A[0][2] = m02;
        A[0][3] = m03;

        A[1][0] = m10;
        A[1][1] = m11;
        A[1][2] = m12;
        A[1][3] = m13;

        A[2][0] = m20;
        A[2][1] = m21;
        A[2][2] = m22;
        A[2][3] = m23;

        A[3][0] = m30;
        A[3][1] = m31;
        A[3][2] = m32;
        A[3][3] = m33;
        return this;
    }

    /**
     * Sets this matrix values as
     * <pre>
     * | m00 m01 m02 0 |
     * | m10 m11 m12 0 |
     * | m20 m21 m22 0 |
     * |  0   0   0  1 |
     * </pre>
     *
     * @return This matrix
     */
    public Matrix4 set(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        return set(m00, m01, m02, 0.0f,
                m10, m11, m12, 0.0f,
                m20, m21, m22, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Sets this matrix values as
     * <pre>
     * | m0x m0y m0z 0 |
     * | m1x m1y m1z 0 |
     * | m2x m2y m2z 0 |
     * |  0   0   0  1 |
     * </pre>
     *
     * @return This matrix
     */
    public Matrix4 set(Vector3 m0, Vector3 m1, Vector3 m2) {
        return set(m0.getX(), m0.getY(), m0.getZ(),
                m1.getX(), m1.getY(), m1.getZ(),
                m2.getX(), m2.getY(), m2.getZ());
    }

    /**
     * Returns the value at the given row and column.
     *
     * @param row The row
     * @param col The column
     * @return The value at specified position.
     */
    public float get(int row, int col) {
        return A[row][col];
    }

    /**
     * Sets the value at the given row and column
     *
     * @param row   The row
     * @param col   The column
     * @param value The value to set
     * @return The value set.
     */
    public float set(int row, int col, float value) {
        return A[row][col] = value;
    }

    /**
     * Adds this matrix to the given one.
     *
     * @param other The matrix to add.
     * @return This matrix, after the operation.
     */
    public Matrix4 add(Matrix4 other) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                A[i][j] += other.A[i][j];
            }
        return this;
    }

    /**
     * Subtracts this matrix to the given one.
     *
     * @param other The matrix to subtract.
     * @return This matrix, after the operation.
     */
    public Matrix4 subtract(Matrix4 other) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                A[i][j] -= other.A[i][j];
            }
        return this;
    }

    /**
     * Multiplies this matrix with the given one.
     * This operation is slower than the static version.
     *
     * @param other The matrix to multiply.
     * @return This matrix, after the operation.
     */
    public Matrix4 multiply(Matrix4 other) {
        A = multiply(this, other).A;
        return this;
    }

    /**
     * Multiplies this matrix by the given scalar.
     *
     * @param scalar The scalar to multiply
     * @return This matrix, after the operation.
     */
    public Matrix4 multiply(float scalar) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                A[i][j] *= scalar;
            }
        return this;
    }

    /**
     * Multiplies the matrix by the given vector. The result is a transformed vector.
     *
     * @param p The point
     * @return The transformed vector.
     */
    public Vector3 transformPoint(Vector3 p) {
        return new Vector3(
                A[0][0] * p.getX() + A[0][1] * p.getY() + A[0][2] * p.getZ() + A[0][3],
                A[1][0] * p.getX() + A[1][1] * p.getY() + A[1][2] * p.getZ() + A[1][3],
                A[2][0] * p.getX() + A[2][1] * p.getY() + A[2][2] * p.getZ() + A[2][3]);
    }

    /**
     * Multiplies the matrix by the given vector. The result is a transformed vector.
     *
     * @param d The point
     * @return The transformed vector.
     */
    public Vector3 transformDirection(Vector3 d) {
        return new Vector3(
                A[0][0] * d.getX() + A[0][1] * d.getY() + A[0][2] * d.getZ(),
                A[1][0] * d.getX() + A[1][1] * d.getY() + A[1][2] * d.getZ(),
                A[2][0] * d.getX() + A[2][1] * d.getY() + A[2][2] * d.getZ());
    }

    /**
     * Multiplies the matrix by the given vector. The result is a transformed vector.
     *
     * @param n The normal
     * @return The transformed vector.
     */
    public Vector3 transformNormal(Vector3 n) {
        return new Vector3(
                A[0][0] * n.getX() + A[1][0] * n.getY() + A[2][0] * n.getZ(),
                A[0][1] * n.getX() + A[1][1] * n.getY() + A[2][1] * n.getZ(),
                A[0][2] * n.getX() + A[1][2] * n.getY() + A[2][2] * n.getZ());
    }

    /**
     * Divides this matrix by the given scalar.
     *
     * @param scalar The scalar to multiply
     * @return This matrix, after the operation.
     */
    public Matrix4 divide(float scalar) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                A[i][j] /= scalar;
            }
        return this;
    }

    /**
     * Calculates and returns the determinant of this matrix.
     */
    public float determinant() {
        return A[0][0] * (A[1][1] * (A[2][2] * A[3][3] - A[2][3] * A[3][2]) +
                A[1][2] * (A[2][3] * A[3][1] - A[2][1] * A[3][3]) +
                A[1][3] * (A[2][1] * A[3][2] - A[2][2] * A[3][1])) -

                A[0][1] * (A[1][0] * (A[2][2] * A[3][3] - A[2][3] * A[3][2]) +
                        A[1][2] * (A[2][3] * A[3][0] - A[2][0] * A[3][3]) +
                        A[1][3] * (A[2][0] * A[3][2] - A[2][2] * A[3][0])) +

                A[0][2] * (A[1][0] * (A[2][1] * A[3][3] - A[2][3] * A[3][1]) +
                        A[1][1] * (A[2][3] * A[3][0] - A[2][0] * A[3][3]) +
                        A[1][3] * (A[2][0] * A[3][1] - A[2][1] * A[3][0])) -

                A[0][3] * (A[1][0] * (A[2][1] * A[3][2] - A[2][2] * A[3][1]) +
                        A[1][1] * (A[2][2] * A[3][0] - A[2][0] * A[3][2]) +
                        A[1][2] * (A[2][0] * A[3][1] - A[2][1] * A[3][0]));
    }

    /**
     * A matrix is invertible if its determinant is not 0.
     *
     * @return True if this matrix is invertible, false if not.
     */
    public boolean isInvertible() {
        return determinant() != 0;
    }

    /**
     * Inverses this matrix. Not all matrices are invertible, so check the {@link #isInvertible()} method prior to
     * calling this method if you are not sure if you can inverse this matrix or not.
     * <p/>
     * <pre>
     * this * inverse = identity
     * </pre>
     * This operation is slower than the static version.
     *
     * @return This matrix, after inversion. If the matrix is not invertible, the result will be undefined.
     * @see #isInvertible()
     * @see br.com.vinigodoy.raytracer.math.Matrix4#inverse()
     */
    public Matrix4 inverse() {
        A = inverse(this).A;
        return this;
    }

    /**
     * Transposes this matrix. If this matrix is:
     * <p/>
     * <pre>
     * [a b c]
     * [d e f]
     * [g h k]
     * </pre>
     * <p/>
     * After transposition it will become:
     * <p/>
     * <pre>
     * [a d g]
     * [b e h]
     * [c f k]
     * </pre>
     * This operation is slower than the static version.
     *
     * @return This matrix transposed.
     */
    public Matrix4 transpose() {
        A = transpose(this).A;
        return this;
    }

    @Override
    public Matrix4 clone() {
        Matrix4 other = new Matrix4();
        for (int i = 0; i < 4; i++)
            System.arraycopy(A[i], 0, other.A[i], 0, 4);
        return other;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;

        Matrix4 other = (Matrix4) obj;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (A[i][j] != other.A[i][j])
                    return false;
            }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 3;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                result = 17 + result * Float.floatToIntBits(A[i][j]);
            }
        return result;
    }
}
