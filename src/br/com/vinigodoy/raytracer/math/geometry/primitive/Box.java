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

public class Box implements GeometricObject {
    private enum Face {LEFT, RIGHT, BOTTOM, TOP, FRONT, BACK}

    private float x0, y0, z0;
    private float x1, y1, z1;
    private Material material;

    public Box(Material material) {
        this(-0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, material);
    }

    public Box(float x0, float x1,
               float y0, float y1,
               float z0, float z1,
               Material material) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z1;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        double ox = ray.getOrigin().getX();
        double oy = ray.getOrigin().getY();
        double oz = ray.getOrigin().getZ();

        double dx = ray.getDirection().getX();
        double dy = ray.getDirection().getY();
        double dz = ray.getDirection().getZ();

        double tx_min, ty_min, tz_min;
        double tx_max, ty_max, tz_max;

        double a = 1.0 / dx;
        if (a >= 0) {
            tx_min = (x0 - ox) * a;
            tx_max = (x1 - ox) * a;
        } else {
            tx_min = (x1 - ox) * a;
            tx_max = (x0 - ox) * a;
        }

        double b = 1.0 / dy;
        if (b >= 0) {
            ty_min = (y0 - oy) * b;
            ty_max = (y1 - oy) * b;
        } else {
            ty_min = (y1 - oy) * b;
            ty_max = (y0 - oy) * b;
        }

        double c = 1.0 / dz;
        if (c >= 0) {
            tz_min = (z0 - oz) * c;
            tz_max = (z1 - oz) * c;
        } else {
            tz_min = (z1 - oz) * c;
            tz_max = (z0 - oz) * c;
        }


        //Find the largest entering value
        Face faceIn;
        double t0;

        if (tx_min > ty_min) {
            t0 = tx_min;
            faceIn = a >= 0 ? Face.LEFT : Face.RIGHT;
        } else {
            t0 = ty_min;
            faceIn = b >= 0 ? Face.BOTTOM : Face.TOP;
        }
        if (tz_min > t0) {
            t0 = tz_min;
            faceIn = c >= 0 ? Face.BACK : Face.FRONT;
        }

        //Find the smallest exiting value
        double t1;
        Face faceOut;
        if (tx_max < ty_max) {
            t1 = tx_max;
            faceOut = a >= 0 ? Face.RIGHT : Face.LEFT;
        } else {
            t1 = ty_max;
            faceOut = b >= 0 ? Face.TOP : Face.BOTTOM;
        }
        if (tz_max < t1) {
            t1 = tz_max;
            faceOut = c >= 0 ? Face.FRONT : Face.BACK;
        }

        if (t0 < t1 && t1 > K_EPSILON) {
            if (t0 > K_EPSILON) {
                tmin.value = (float) t0;
                sr.normal = getNormal(faceIn);
            } else {
                tmin.value = (float) t1;
                sr.normal = getNormal(faceOut);
            }
            sr.hitPoint = ray.pointAt(tmin.value);
            return true;
        }

        return false;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        double ox = ray.getOrigin().getX();
        double oy = ray.getOrigin().getY();
        double oz = ray.getOrigin().getZ();

        double dx = ray.getDirection().getX();
        double dy = ray.getDirection().getY();
        double dz = ray.getDirection().getZ();

        double tx_min, ty_min, tz_min;
        double tx_max, ty_max, tz_max;

        double a = 1.0 / dx;
        if (a >= 0) {
            tx_min = (x0 - ox) * a;
            tx_max = (x1 - ox) * a;
        } else {
            tx_min = (x1 - ox) * a;
            tx_max = (x0 - ox) * a;
        }

        double b = 1.0 / dy;
        if (b >= 0) {
            ty_min = (y0 - oy) * b;
            ty_max = (y1 - oy) * b;
        } else {
            ty_min = (y1 - oy) * b;
            ty_max = (y0 - oy) * b;
        }

        double c = 1.0 / dz;
        if (c >= 0) {
            tz_min = (z0 - oz) * c;
            tz_max = (z1 - oz) * c;
        } else {
            tz_min = (z1 - oz) * c;
            tz_max = (z0 - oz) * c;
        }

        //Find the largest entering value
        double t0;

        if (tx_min > ty_min) {
            t0 = tx_min;
        } else {
            t0 = ty_min;
        }
        if (tz_min > t0) {
            t0 = tz_min;
        }

        //Find the smallest exiting value
        double t1;
        if (tx_max < ty_max) {
            t1 = tx_max;
        } else {
            t1 = ty_max;
        }
        if (tz_max < t1) {
            t1 = tz_max;
        }

        if (t0 < t1 && t1 > K_EPSILON) {
            if (t0 > K_EPSILON) {
                tmin.value = (float) t0;
            } else {
                tmin.value = (float) t1;
            }

            return true;
        }

        return false;
    }

    private Vector3 getNormal(Face face) {
        switch (face) {
            case LEFT:
                return new Vector3(-1, 0, 0);
            case BOTTOM:
                return new Vector3(0, -1, 0);
            case BACK:
                return new Vector3(0, 0, -1);
            case RIGHT:
                return new Vector3(1, 0, 0);
            case TOP:
                return new Vector3(0, 1, 0);
            case FRONT:
                return new Vector3(0, 0, 1);
        }
        throw new IllegalArgumentException("Invalid face: " + face);
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
