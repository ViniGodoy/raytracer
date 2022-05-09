/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.math.geometry.part;

import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

public class ConvexPartSphere implements GeometricObject {
    private final Vector3 center;        // center coordinates
    private final float radius;            // sphere radius
    private final float phiMin;            // minimum azimiuth angle measured counter clockwise from the +ve z axis
    private final float phiMax;            // maximum azimiuth angle measured counter clockwise from the +ve z axis

    private final float cosThetaMin;    // stored to avoid repeated calculations
    private final float cosThetaMax;    // stored to avoid repeated calculations

    private final Material material;

    public ConvexPartSphere(Vector3 center, float radius,
                            float azimuthMin, float azimuthMax,
                            float polarMin, float polarMax, // measured from top
                            Material material) {
        this.center = center;
        this.radius = radius;
        this.phiMin = azimuthMin;
        this.phiMax = azimuthMax;
        // minimum polar angle measured down from the +ve y axis
        // maximum polar angle measured down from the +ve y axis
        this.material = material;

        this.cosThetaMin = (float) Math.cos(polarMin);
        this.cosThetaMax = (float) Math.cos(polarMax);
    }

    public ConvexPartSphere(float azimuthMin, float azimuthMax,
                            float polarMin, float polarMax,        // measured from top
                            Material material) {
        this(new Vector3(), 1,
                azimuthMin, azimuthMax,
                polarMin, polarMax,
                material);
    }

    public static ConvexPartSphere withDegrees(Vector3 center, float radius,
                                               float azimuthMin, float azimuthMax,
                                               float polarMin, float polarMax,
                                               Material material) {
        return new ConvexPartSphere(center, radius,
                (float) Math.toRadians(azimuthMin), (float) Math.toRadians(azimuthMax),
                (float) Math.toRadians(polarMin), (float) Math.toRadians(polarMax),
                material);
    }

    public static ConvexPartSphere withDegrees(float azimuthMin, float azimuthMax,
                                               float polarMin, float polarMax,
                                               Material material) {
        return withDegrees(new Vector3(), 1, azimuthMin, azimuthMax, polarMin, polarMax, material);

    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        var temp = Vector3.subtract(ray.getOrigin(), center);
        var b = 2.0f * temp.dot(ray.getDirection());
        var c = temp.sizeSqr() - radius * radius;
        var disc = b * b - 4.0f * c;

        if (disc < 0.0) return false;

        var e = (float) Math.sqrt(disc);
        var t = (-b - e) / 2.0f;    // smaller root

        if (t > K_EPSILON) {
            var hitPoint = ray.pointAt(t);
            var hit = Vector3.subtract(hitPoint, center);

            var phi = Math.atan2(hit.getX(), hit.getZ());
            if (phi < 0.0)
                phi += 2 * Math.PI;

            if (hit.getY() <= radius * cosThetaMin &&
                    hit.getY() >= radius * cosThetaMax &&
                    phi >= phiMin && phi <= phiMax) {

                tmin.value = t;
                sr.normal = Vector3.multiply(ray.getDirection(), t).add(temp).divide(radius);   // points outwards
                if (Vector3.negate(ray.getDirection()).dot(sr.normal) < 0)
                    sr.normal.negate();
                sr.worldHitPoint = hitPoint;
                return true;
            }
        }

        t = (-b + e) / 2.0f;    // larger root

        if (t > K_EPSILON) {
            var hitPoint = ray.pointAt(t);
            var hit = Vector3.subtract(hitPoint, center);

            var phi = Math.atan2(hit.getX(), hit.getZ());
            if (phi < 0.0)
                phi += 2 * Math.PI;

            if (hit.getY() <= radius * cosThetaMin &&
                    hit.getY() >= radius * cosThetaMax &&
                    phi >= phiMin && phi <= phiMax)
            {
                tmin.value = t;
                sr.normal = Vector3.multiply(ray.getDirection(), t).add(temp).divide(radius);   // points outwards
                if (Vector3.negate(ray.getDirection()).dot(sr.normal) < 0)
                    sr.normal.negate();
                sr.worldHitPoint = hitPoint;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        var temp = Vector3.subtract(ray.getOrigin(), center);
        var b = 2.0f * temp.dot(ray.getDirection());
        var c = temp.sizeSqr() - radius * radius;
        var disc = b * b - 4.0f * c;

        if (disc < 0.0) return false;

        var e = (float) Math.sqrt(disc);
        var t = (-b - e) / 2.0f;    // smaller root

        if (t > K_EPSILON) {
            var hitPoint = ray.pointAt(t);
            var hit = Vector3.subtract(hitPoint, center);

            var phi = Math.atan2(hit.getX(), hit.getZ());
            if (phi < 0.0) phi += 2 * Math.PI;

            if (hit.getY() <= radius * cosThetaMin &&
                    hit.getY() >= radius * cosThetaMax &&
                    phi >= phiMin && phi <= phiMax) {

                tmin.value = t;
                return true;
            }
        }

        t = (-b + e) / 2.0f;    // larger root

        if (t > K_EPSILON) {
            var hitPoint = ray.pointAt(t);
            var hit = Vector3.subtract(hitPoint, center);

            var phi = Math.atan2(hit.getX(), hit.getZ());
            if (phi < 0.0)
                phi += 2 * Math.PI;

            if (hit.getY() <= radius * cosThetaMin &&
                    hit.getY() >= radius * cosThetaMax &&
                    phi >= phiMin && phi <= phiMax) {

                tmin.value = t;
                return true;
            }
        }

        return false;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
