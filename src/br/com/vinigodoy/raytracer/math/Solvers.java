/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math;

import static java.lang.Math.*;

/**
 * Solvers for quadric, cubic and quartic equations.
 */
public class Solvers {
    private static final double EQN_EPS = 1e-30f;

    public static boolean isZero(double x) {
        return x > -EQN_EPS && x < EQN_EPS;
    }

    public static int solveQuadric(double[] c, double[] s) {
        /* normal form: x^2 + px + q = 0 */

        final var p = c[1] / (2 * c[2]);
        final var q = c[0] / c[2];
        final var D = p * p - q;

        if (isZero(D)) {
            s[0] = -p;
            return 1;
        }

        if (D < 0) return 0;

        final var sqrt_D = sqrt(D);
        s[0] = sqrt_D - p;
        s[1] = -sqrt_D - p;
        return 2;
    }

    public static int solveCubic(double[] c, double[] s) {
        /* normal form: x^3 + Ax^2 + Bx + C = 0 */

        final var A = c[2] / c[3];
        final var B = c[1] / c[3];
        final var C = c[0] / c[3];

        /*  substitute x = y - A/3 to eliminate quadric term:
        x^3 +px + q = 0 */

        final var sq_A = A * A;
        final var p = 1.0 / 3 * (-1.0 / 3 * sq_A + B);
        final var q = 1.0 / 2 * (2.0 / 27 * A * sq_A - 1.0 / 3 * A * B + C);

        /* use Cardano's formula */

        final var cb_p = p * p * p;
        final var D = q * q + cb_p;

        final int num;
        if (isZero(D)) {
            if (isZero(q)) { /* one triple solution */
                s[0] = 0;
                num = 1;
            } else { /* one single and one double solution */
                final var u = cbrt(-q);
                s[0] = 2 * u;
                s[1] = -u;
                num = 2;
            }
        } else if (D < 0) { /* Casus irreducibilis: three real solutions */
            final var phi = 1.0 / 3 * acos(-q / sqrt(-cb_p));
            final var t = 2 * sqrt(-p);

            s[0] = t * cos(phi);
            s[1] = -t * cos(phi + PI / 3);
            s[2] = -t * cos(phi - PI / 3);
            num = 3;
        } else { /* one real solution */
            final var sqrt_D = sqrt(D);
            final var u = cbrt(sqrt_D - q);
            final var v = -cbrt(sqrt_D + q);

            s[0] = u + v;
            num = 1;
        }

        /* resubstitute */
        final var sub = 1.0 / 3 * A;

        for (var i = 0; i < num; ++i)
            s[i] -= sub;

        return num;
    }

    public static int solveQuartic(double[] c, double[] s) {
        /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */

        final var A = c[3] / c[4];
        final var B = c[2] / c[4];
        final var C = c[1] / c[4];
        final var D = c[0] / c[4];

        /*  substitute x = y - A/4 to eliminate cubic term:
        x^4 + px^2 + qx + r = 0 */

        final var sq_A = A * A;
        final var p = -3.0 / 8 * sq_A + B;
        final var q = 1.0 / 8 * sq_A * A - 1.0 / 2 * A * B + C;
        final var r = -3.0 / 256 * sq_A * sq_A + 1.0 / 16 * sq_A * B - 1.0 / 4 * A * C + D;

        final var coeffs = new double[4];
        int num;
        if (isZero(r)) {
            /* no absolute term: y(y^3 + py + q) = 0 */
            coeffs[0] = q;
            coeffs[1] = p;
            coeffs[2] = 0;
            coeffs[3] = 1;

            num = solveCubic(coeffs, s);

            s[num++] = 0;
        } else {
            /* solve the resolvent cubic ... */
            coeffs[0] = 1.0 / 2 * r * p - 1.0 / 8 * q * q;
            coeffs[1] = -r;
            coeffs[2] = -1.0 / 2 * p;
            coeffs[3] = 1;

            solveCubic(coeffs, s);

		    /* ... and take the one real solution ... */

            final var z = s[0];

		    /* ... to build two quadric equations */

            var u = z * z - r;
            var v = 2 * z - p;

            if (isZero(u))
                u = 0;
            else if (u > 0)
                u = sqrt(u);
            else
                return 0;

            if (isZero(v))
                v = 0;
            else if (v > 0)
                v = sqrt(v);
            else
                return 0;

            coeffs[0] = z - u;
            coeffs[1] = q < 0 ? -v : v;
            coeffs[2] = 1;

            num = solveQuadric(coeffs, s);

            coeffs[0] = z + u;
            coeffs[1] = q < 0 ? v : -v;
            coeffs[2] = 1;

            final var s2 = new double[]{s[num], s[num + 1]};
            num += solveQuadric(coeffs, s2);
        }

        /* resubstitute */

        final var sub = 1.0 / 4 * A;

        for (var i = 0; i < num; ++i)
            s[i] -= sub;

        return num;
    }


}
