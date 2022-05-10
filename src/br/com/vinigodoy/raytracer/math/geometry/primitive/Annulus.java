/*
===========================================================================
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
import static br.com.vinigodoy.raytracer.math.Vector3.subtract;

public class Annulus implements GeometricObject {
    private final Vector3 center;
    private final Vector3 normal;
    private final float outerRadius;
    private final float innerRadius;

    private final Material material;

    /**
     * Creates a new generic anullus centered at origin, pointing up and with radius 1.
     *
     * @param material The disk material
     */
    public Annulus(Material material) {
        this(new Vector3(), new Vector3(0, 1, 0), 0.5f, 1.0f, material);
    }

    public Annulus(Vector3 center, Vector3 normal, float innerRadius, float outerRadius, Material material) {
        this.center = center;
        this.normal = normalize(normal);
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        final var t = subtract(center, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);
        if (t < K_EPSILON) return false;

        final var p = ray.pointAt(t);
        final var centerDistanceSqr = center.distanceSqr(p);

        if ((centerDistanceSqr >= outerRadius * outerRadius) || (centerDistanceSqr <= innerRadius * innerRadius)) {
            return false;
        }

        tmin.value = t;
        sr.normal = normal;
        sr.worldHitPoint = p;
        sr.localHitPoint = sr.worldHitPoint;
        return true;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        final var t = subtract(center, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);
        if (t < K_EPSILON) return false;

        tmin.value = t;
        final var p = ray.pointAt(t);
        final var centerDistanceSqr = center.distanceSqr(p);
        return (centerDistanceSqr < outerRadius * outerRadius) && (centerDistanceSqr >= innerRadius * innerRadius);

    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
