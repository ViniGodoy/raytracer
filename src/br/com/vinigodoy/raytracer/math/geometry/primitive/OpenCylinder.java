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
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static java.lang.Math.sqrt;

public class OpenCylinder implements GeometricObject {
    private final float y0;
    private final float y1;
    private final float radius;
    private final Material material;

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

        var ox = ray.getOrigin().getX();
        var oz = ray.getOrigin().getZ();
        var dx = ray.getDirection().getX();
        var dz = ray.getDirection().getZ();
        var t = tmin.value;
        var inv_radius = 1.0f / radius;

        sr.normal = new Vector3((ox + t * dx) * inv_radius, 0.0f, (oz + t * dz) * inv_radius);
        if (Vector3.negate(ray.getDirection()).dot(sr.normal) < 0.0)
            sr.normal.negate();
        sr.worldHitPoint = ray.pointAt(tmin.value);
        sr.localHitPoint = sr.worldHitPoint;
        return true;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        var ox = ray.getOrigin().getX();
        var oy = ray.getOrigin().getY();
        var oz = ray.getOrigin().getZ();
        var dx = ray.getDirection().getX();
        var dy = ray.getDirection().getY();
        var dz = ray.getDirection().getZ();

        var a = dx * dx + dz * dz;
        var b = 2.0f * (ox * dx + oz * dz);
        var c = ox * ox + oz * oz - radius * radius;
        var disc = b * b - 4.0f * a * c;

        if (disc < 0.0) return false;

        var e = (float) sqrt(disc);
        var denom = 2.0f * a;
        var t = (-b - e) / denom;    // smaller root

        if (t > K_EPSILON) {
            var yhit = oy + t * dy;

            if (yhit > y0 && yhit < y1) {
                tmin.value = t;
                return true;
            }
        }

        t = (-b + e) / denom;    // bigger root
        if (t > K_EPSILON) {
            double yhit = oy + t * dy;
            if (yhit > y0 && yhit < y1) {
                tmin.value = t;
                return true;
            }
        }

        return false;
    }

    public BBox getBounds() {
        return new BBox(-radius, radius, y0, y1, -radius, radius);
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
