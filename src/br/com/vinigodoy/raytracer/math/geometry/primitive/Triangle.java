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
    private final Material material;
    private final Vector3 v0;
    private final Vector3 v1;
    private final Vector3 v2;
    private final Vector3 normal;

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
        final var a = v0.getX() - v1.getX();
        final var b = v0.getX() - v2.getX();
        final var c = ray.getDirection().getX();
        final var d = v0.getX() - ray.getOrigin().getX();

        final var e = v0.getY() - v1.getY();
        final var f = v0.getY() - v2.getY();
        final var g = ray.getDirection().getY();
        final var h = v0.getY() - ray.getOrigin().getY();

        final var i = v0.getZ() - v1.getZ();
        final var j = v0.getZ() - v2.getZ();
        final var k = ray.getDirection().getZ();
        final var l = v0.getZ() - ray.getOrigin().getZ();

        final double m = f * k - g * j;
        final double n = h * k - g * l;
        final double p = f * l - h * j;
        final double q = g * i - e * k;
        final double s = e * j - f * i;

        final var inv_denom = 1.0 / (a * m + b * q + c * s);

        final var e1 = d * m - b * n - c * p;
        final var beta = e1 * inv_denom;

        if (beta < 0.0) return false;

        final var r = e * l - h * i;
        final var e2 = a * n + d * q + c * r;
        final var gamma = e2 * inv_denom;

        if (gamma < 0.0) return false;

        if (beta + gamma > 1.0) return false;

        final var e3 = a * p - b * r + d * s;
        final var t = e3 * inv_denom;

        if (t < K_EPSILON) return false;

        tmin.value = (float) t;
        return true;
    }
}
