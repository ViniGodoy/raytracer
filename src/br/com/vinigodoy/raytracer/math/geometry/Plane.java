/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math.geometry;

import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.subtract;

public class Plane implements GeometricObject {
    private Vector3 point;
    private Vector3 normal;
    private Material material;

    public Plane(Vector3 point, Vector3 normal, Material material) {
        this.point = point;
        this.normal = normal.normalize();
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        float t = subtract(point, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);
        if (t < K_EPSILON)
            return false;

        sr.normal = normal;
        sr.localHitPoint = ray.pointAt(t);
        tmin.value = t;

        return true;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        float t = subtract(point, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);
        if (t < K_EPSILON)
            return false;

        tmin.value = t;
        return true;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
