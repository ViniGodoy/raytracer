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

import static java.lang.Math.sqrt;

public class OpenCylinder implements GeometricObject {

    private float y0;
    private float y1;
    private float radius;
    private Material material;

    public OpenCylinder(Material material) {
        this(-0.5f, 0.5f, 1.0f, material);
    }

    public OpenCylinder(float y0, float y1, float radius, Material material) {
        this.y0 = y0;
        this.y1 = y1;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        if (!shadow_hit(ray, tmin))
            return false;

        float ox = ray.getOrigin().getX();
        float oz = ray.getOrigin().getZ();
        float dx = ray.getDirection().getX();
        float dz = ray.getDirection().getZ();
        float t = tmin.value;
        float inv_radius = 1.0f / radius;

        sr.normal = new Vector3((ox + t * dx) * inv_radius, 0.0f, (oz + t * dz) * inv_radius);

        // test for hitting from inside
        if (-ray.getDirection().dot(sr.normal) < 0.0)
            sr.normal.negate();

        sr.hitPoint = ray.pointAt(t);
        return true;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        float ox = ray.getOrigin().getX();
        float oy = ray.getOrigin().getY();
        float oz = ray.getOrigin().getZ();
        float dx = ray.getDirection().getX();
        float dy = ray.getDirection().getY();
        float dz = ray.getDirection().getZ();

        float a = dx * dx + dz * dz;
        float b = 2.0f * (ox * dx + oz * dz);
        float c = ox * ox + oz * oz - radius * radius;
        float disc = b * b - 4.0f * a * c;

        if (disc < 0.0)
            return (false);

        float e = (float) sqrt(disc);
        float denom = 2.0f * a;
        float t = (-b - e) / denom;

        if (t <= K_EPSILON) {
            //If not hit, tries the larger root
            t = (-b + e) / denom;
        }

        if (t <= K_EPSILON) {
            return false;
        }

        float yhit = oy + t * dy;
        if (yhit > y0 && yhit < y1) {
            tmin.value = t;
            return true;
        }

        return false;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
