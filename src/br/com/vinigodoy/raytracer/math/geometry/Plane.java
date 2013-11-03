package br.com.vinigodoy.raytracer.math.geometry;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.subtract;


/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
public class Plane implements GeometricObject {
    private Vector3 point;
    private Vector3 normal;
    private Vector3 color;

    public Plane(Vector3 point, Vector3 normal, Vector3 color) {
        this.point = point;
        this.normal = normal.normalize();
        this.color = color;
    }

    @Override
    public HitResult hit(Ray ray, ShadeRec sr) {
        float t = subtract(point, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);
        if (t < K_EPSILON)
            return HitResult.MISS;

        sr.normal = normal;
        sr.localHitPoint = ray.pointAt(t);

        return new HitResult(t);
    }

    @Override
    public Vector3 getColor() {
        return color;
    }
}
