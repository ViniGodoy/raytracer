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

public class Disk implements GeometricObject {
    private Vector3 center;
    private Vector3 normal;
    private float radius;

    private Material material;

    /**
     * Creates a new generic disk centered at origin, pointing up and with radius 1.
     *
     * @param material The disk material
     */
    public Disk(Material material) {
        this(new Vector3(), new Vector3(0, 1, 0), 1.0f, material);
    }

    public Disk(Vector3 center, Vector3 normal, float radius, Material material) {
        this.center = center;
        this.normal = normalize(normal);
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        float t = subtract(center, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);
        if (t < K_EPSILON) {
            return false;
        }

        Vector3 p = ray.pointAt(t);
        if (center.distanceSqr(p) >= radius * radius) {
            return false;
        }

        tmin.value = t;
        sr.normal = normal;
        sr.hitPoint = p;
        return true;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        float t = subtract(center, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);
        if (t < K_EPSILON) {
            return false;
        }

        tmin.value = t;
        Vector3 p = ray.pointAt(t);
        return (center.distanceSqr(p) < radius * radius);
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
