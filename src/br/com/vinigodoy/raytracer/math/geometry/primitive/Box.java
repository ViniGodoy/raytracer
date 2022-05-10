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
import br.com.vinigodoy.raytracer.math.BBox;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

/**
 * Represents an axis-aligned box.
 */
public class Box implements GeometricObject {
    private enum Face {LEFT, RIGHT, BOTTOM, TOP, FRONT, BACK}

    private final float x0;
    private final float y0;
    private final float z0;
    private final float x1;
    private final float y1;
    private final float z1;
    private final Material material;

    /**
     * Creates a cube with side 1 and centered at origin.
     *
     * @param material Cube's material.
     */
    public Box(Material material) {
        this(1.0f, material);
    }

    /**
     * Create a cube with side size centered at origin.
     *
     * @param side     Size of the cube side.
     * @param material Cube material.
     */
    public Box(float side, Material material) {
        this(side, side, side, material);
    }

    /**
     * Creates a box with the given dimensions centered at origin.
     *
     * @param width    Box width (x-axis)
     * @param height   Box height (y-axis)
     * @param depth    Box depth (z-axis)
     * @param material The material.
     */
    public Box(float width, float height, float depth, Material material) {
        final var hw = width / 2.0f;
        final var hh = height / 2.0f;
        final var hd = depth / 2.0f;

        x0 = -hw;
        x1 = hw;
        y0 = -hh;
        y1 = hh;
        z0 = -hd;
        z1 = hd;
        this.material = material;
    }

    /**
     * Creates a box in the given coordinates.
     *
     * @param x0       x0 coordinate
     * @param x1       x1 coordinate
     * @param y0       y0 coordinate
     * @param y1       y1 coordinate
     * @param z0       z0 coordinate
     * @param z1       z1 coordinate.
     * @param material The box material.
     */
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

    /**
     * Creates a box in the given coordinates.
     *
     * @param minCorner Minimum corner
     * @param maxCorner Maximum corner
     * @param material  Material
     */
    public Box(Vector3 minCorner, Vector3 maxCorner, Material material) {
        this(minCorner.getX(), minCorner.getY(), minCorner.getZ(),
                maxCorner.getX(), maxCorner.getY(), maxCorner.getZ(),
                material);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        final var ox = ray.getOrigin().getX();
        final var oy = ray.getOrigin().getY();
        final var oz = ray.getOrigin().getZ();

        final var dx = ray.getDirection().getX();
        final var dy = ray.getDirection().getY();
        final var dz = ray.getDirection().getZ();

        final double tx_min;
        final double ty_min;
        final double tz_min;
        final double tx_max;
        final double ty_max;
        final double tz_max;

        final var a = 1.0 / dx;
        if (a >= 0) {
            tx_min = (x0 - ox) * a;
            tx_max = (x1 - ox) * a;
        } else {
            tx_min = (x1 - ox) * a;
            tx_max = (x0 - ox) * a;
        }

        final var b = 1.0 / dy;
        if (b >= 0) {
            ty_min = (y0 - oy) * b;
            ty_max = (y1 - oy) * b;
        } else {
            ty_min = (y1 - oy) * b;
            ty_max = (y0 - oy) * b;
        }

        final var c = 1.0 / dz;
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
            sr.worldHitPoint = ray.pointAt(tmin.value);
            sr.localHitPoint = sr.worldHitPoint;
            return true;
        }

        return false;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        final var ox = ray.getOrigin().getX();
        final var oy = ray.getOrigin().getY();
        final var oz = ray.getOrigin().getZ();

        final var dx = ray.getDirection().getX();
        final var dy = ray.getDirection().getY();
        final var dz = ray.getDirection().getZ();

        final double tx_min;
        final double ty_min;
        final double tz_min;
        final double tx_max;
        final double ty_max;
        final double tz_max;

        final var a = 1.0 / dx;
        if (a >= 0) {
            tx_min = (x0 - ox) * a;
            tx_max = (x1 - ox) * a;
        } else {
            tx_min = (x1 - ox) * a;
            tx_max = (x0 - ox) * a;
        }

        final var b = 1.0 / dy;
        if (b >= 0) {
            ty_min = (y0 - oy) * b;
            ty_max = (y1 - oy) * b;
        } else {
            ty_min = (y1 - oy) * b;
            ty_max = (y0 - oy) * b;
        }

        final var c = 1.0 / dz;
        if (c >= 0) {
            tz_min = (z0 - oz) * c;
            tz_max = (z1 - oz) * c;
        } else {
            tz_min = (z1 - oz) * c;
            tz_max = (z0 - oz) * c;
        }

        //Find the largest entering value
        var t0 = Math.max(tx_min, ty_min);

        if (tz_min > t0) {
            t0 = tz_min;
        }

        //Find the smallest exiting value
        var t1 = Math.min(tx_max, ty_max);
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
        return switch (face) {
            case LEFT -> new Vector3(-1, 0, 0);
            case BOTTOM -> new Vector3(0, -1, 0);
            case BACK -> new Vector3(0, 0, -1);
            case RIGHT -> new Vector3(1, 0, 0);
            case TOP -> new Vector3(0, 1, 0);
            case FRONT -> new Vector3(0, 0, 1);
        };
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public BBox getBounds() {
        return new BBox(x0, x1, y0, y1, z0, z1);
    }
}
