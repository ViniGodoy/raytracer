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
import br.com.vinigodoy.raytracer.material.Phong;
import br.com.vinigodoy.raytracer.math.BBox;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;
import static br.com.vinigodoy.raytracer.math.Vector3.subtract;

public class Sphere implements GeometricObject {
    private Vector3 center;
    private float radius;
    private Material material;

    /**
     * Creates a generic sphere centered in the origin and with radius 1.
     *
     * @param material The sphere material.
     */
    public Sphere(Material material) {
        this(new Vector3(), 1.0f, material);
    }

    /**
     * Creates a sphere with the given center, radius and material.
     *
     * @param center   Sphere center
     * @param radius   Sphere radius
     * @param material Sphere material
     */
    public Sphere(Vector3 center, float radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    /**
     * Creates a new sphere with the given center, radius and a default material of the given color.
     *
     * @param center The sphere center
     * @param radius The sphere radius
     * @param color  The sphere color
     */
    public Sphere(Vector3 center, float radius, Vector3 color) {
        this(center, radius, new Phong(0.2f, 0.65f, 0.1f, 8.00f, color));
    }


    public Vector3 getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        if (shadow_hit(ray, tmin)) {
            Vector3 temp = subtract(ray.getOrigin(), center);
            sr.normal = multiply(ray.getDirection(), tmin.value).add(temp).divide(radius);
            sr.worldHitPoint = ray.pointAt(tmin.value);
            sr.localHitPoint = sr.worldHitPoint;
            return true;
        }
        return false;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        Vector3 temp = subtract(ray.getOrigin(), center);

        //Bhaskara equation a, b and c terms and delta calculation (bˆ2 - 4ac).

        //float a = 1, since ray.getDirection().sizeSqr() is always one.
        float b = multiply(temp, 2.0f).dot(ray.getDirection());
        float c = temp.sizeSqr() - radius * radius;
        float delta = b * b - 4.0f * c;

        if (delta < 0.0) {
            return false;
        }

        float e = (float) Math.sqrt(delta);

        //Smaller root
        float t = (-b - e) / 2.0f;
        if (t <= K_EPSILON) {
            //If not hit, tries the larger root
            t = (-b + e) / 2.0f;
        }

        if (t > K_EPSILON) {
            tmin.value = t;
            return true;
        }

        //Too close to hit
        return false;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public BBox getBounds() {
        return new BBox(-radius, radius, -radius, radius, -radius, radius);
    }

}
