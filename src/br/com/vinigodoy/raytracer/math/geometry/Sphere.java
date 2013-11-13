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
import br.com.vinigodoy.raytracer.material.Matte;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;
import static br.com.vinigodoy.raytracer.math.Vector3.subtract;

public class Sphere implements GeometricObject {
    private Vector3 center;
    private float radius;
    private Material material;

    public Sphere(Vector3 center, float radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public Sphere(Vector3 center, float radius, Vector3 color) {
        this(center, radius, new Matte(0.2f, 0.85f, color));
    }


    public Vector3 getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public HitResult hit(Ray ray, ShadeRec sr) {
        Vector3 temp = subtract(ray.getOrigin(), center);

        //Bhaskara equation a, b and c terms and delta calculation (bˆ2 - 4ac).

        //float a = 1, since ray.getDirection().sizeSqr() is always one.
        float b = multiply(temp, 2.0f).dot(ray.getDirection());
        float c = temp.sizeSqr() - radius * radius;
        float delta = b * b - 4.0f * c;

        if (delta < 0.0) {
            return HitResult.MISS;
        }

        float e = (float) Math.sqrt(delta);

        //Smaller root
        float t = (-b - e) / 2.0f;
        if (t <= K_EPSILON) {
            //If not hit, tries the larger root
            t = (-b + e) / 2.0f;
        }

        if (t > K_EPSILON) {
            sr.normal = multiply(ray.getDirection(), t).add(temp).divide(radius);
            sr.localHitPoint = ray.pointAt(t);
            return new HitResult(t);
        }

        //Too close to hit
        return HitResult.MISS;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
