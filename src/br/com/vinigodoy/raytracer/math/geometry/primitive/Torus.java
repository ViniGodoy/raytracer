/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math.geometry.primitive;

import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.BBox;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Solvers;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

public class Torus implements GeometricObject {
    private float a; //Swept radius
    private float b; //Tube radius
    private Material material;
    private BBox bbox;

    public Torus(float sweptRadius, float tubeRadius, Material material) {
        this.a = sweptRadius;
        this.b = tubeRadius;
        this.bbox = new BBox(-a - b, a + b, -b, b, -a - b, a + b);
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        if (!shadow_hit(ray, tmin))
            return false;

        sr.hitPoint = ray.pointAt(tmin.value);
        sr.normal = getNormal(sr.hitPoint);
        return true;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        if (!bbox.hit(ray))
            return false;

        double x1 = ray.getOrigin().getX();
        double y1 = ray.getOrigin().getY();
        double z1 = ray.getOrigin().getZ();

        double d1 = ray.getDirection().getX();
        double d2 = ray.getDirection().getY();
        double d3 = ray.getDirection().getZ();

        double coeffs[] = new double[5];    // coefficient array for the quartic equation
        double roots[] = new double[4];    // solution array for the quartic equation

        // define the coefficients of the quartic equation
        double sum_d_sqrd = d1 * d1 + d2 * d2 + d3 * d3;
        double e = x1 * x1 + y1 * y1 + z1 * z1 - a * a - b * b;
        double f = x1 * d1 + y1 * d2 + z1 * d3;
        double four_a_sqrd = 4.0 * a * a;

        coeffs[0] = e * e - four_a_sqrd * (b * b - y1 * y1);    // constant term
        coeffs[1] = 4.0 * f * e + 2.0 * four_a_sqrd * y1 * d2;
        coeffs[2] = 2.0 * sum_d_sqrd * e + 4.0 * f * f + four_a_sqrd * d2 * d2;
        coeffs[3] = 4.0 * sum_d_sqrd * f;
        coeffs[4] = sum_d_sqrd * sum_d_sqrd;                    // coefficient of t^4

        // find roots of the quartic equation
        int num_real_roots = Solvers.solveQuartic(coeffs, roots);

        boolean intersected = false;
        double t = Double.MAX_VALUE;

        if (num_real_roots == 0)  // ray misses the torus
            return false;

        // find the smallest root greater than kEpsilon, if any
        // the roots array is not sorted
        for (int j = 0; j < num_real_roots; j++)
            if (roots[j] > K_EPSILON) {
                intersected = true;
                if (roots[j] < t)
                    t = roots[j];
            }

        if (!intersected)
            return false;

        tmin.value = (float) t;
        return true;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public Vector3 getNormal(Vector3 p) {
        float param_squared = a * a + b * b;

        float x = p.getX();
        float y = p.getY();
        float z = p.getZ();
        float sum_squared = x * x + y * y + z * z;

        return new Vector3(
                4.0f * x * (sum_squared - param_squared),
                4.0f * y * (sum_squared - param_squared + 2.0f * a * a),
                4.0f * z * (sum_squared - param_squared)).normalize();
    }

    @Override
    public Torus clone() {
        return new Torus(a, b, material.clone());
    }

    public BBox getBounds() {
        return bbox;
    }
}
