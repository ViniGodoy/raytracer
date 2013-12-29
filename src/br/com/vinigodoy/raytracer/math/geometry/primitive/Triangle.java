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
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.normalize;

public class Triangle implements GeometricObject {
    private Material material;
    private Vector3 v0, v1, v2;
    private Vector3 normal;

    /**
     * Creates a new triangle. The vertices must be given in a counter-clockwise direction.
     *
     * @param v0       Vertex 0
     * @param v1       Vertex 1
     * @param v2       Vertex 2
     * @param normal   The triangle normal
     * @param material The triangle material
     */
    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2, Vector3 normal, Material material) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.normal = normalize(normal);
        this.material = material;
    }

    /**
     * Creates a new triangle. The vertices must be given in a counter-clockwise direction.
     * The normal is automatically calculated.
     *
     * @param v0       Vertex 0
     * @param v1       Vertex 1
     * @param v2       Vertex 2
     * @param material The triangle material
     */
    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2, Material material) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.normal = Vector3.cross(Vector3.subtract(v1, v0), Vector3.subtract(v2, v0)).normalize();
        this.material = material;
    }

    public Vector3 getV0() {
        return v0;
    }

    public Vector3 getV1() {
        return v1;
    }

    public Vector3 getV2() {
        return v2;
    }

    public Vector3 getNormal() {
        return normal;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        if (shadow_hit(ray, tmin)) {
            sr.worldHitPoint = ray.pointAt(tmin.value);
            sr.localHitPoint = sr.worldHitPoint;
            sr.normal = normal;
            return true;
        }

        return false;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        double a = v0.getX() - v1.getX();
        double b = v0.getX() - v2.getX();
        double c = ray.getDirection().getX();
        double d = v0.getX() - ray.getOrigin().getX();

        double e = v0.getY() - v1.getY();
        double f = v0.getY() - v2.getY();
        double g = ray.getDirection().getY();
        double h = v0.getY() - ray.getOrigin().getY();

        double i = v0.getZ() - v1.getZ();
        double j = v0.getZ() - v2.getZ();
        double k = ray.getDirection().getZ();
        double l = v0.getZ() - ray.getOrigin().getZ();

        double m = f * k - g * j, n = h * k - g * l, p = f * l - h * j;
        double q = g * i - e * k, s = e * j - f * i;

        double inv_denom = 1.0 / (a * m + b * q + c * s);

        double e1 = d * m - b * n - c * p;
        double beta = e1 * inv_denom;

        if (beta < 0.0)
            return false;

        double r = e * l - h * i;
        double e2 = a * n + d * q + c * r;
        double gamma = e2 * inv_denom;

        if (gamma < 0.0)
            return false;

        if (beta + gamma > 1.0)
            return false;

        double e3 = a * p - b * r + d * s;
        double t = e3 * inv_denom;

        if (t < K_EPSILON)
            return false;

        tmin.value = (float) t;
        return true;
    }
}
