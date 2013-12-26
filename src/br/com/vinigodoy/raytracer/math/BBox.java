/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math;

import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;

/**
 * Represents an axis aligned bounding box.
 */
public class BBox {
    private float x0, y0, z0;
    private float x1, y1, z1;

    public BBox(Vector3 p0, Vector3 p1) {
        this(p0.getX(), p0.getY(), p0.getZ(),
                p1.getX(), p1.getY(), p1.getZ());
    }

    public BBox(float x0, float x1,
                float y0, float y1,
                float z0, float z1) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;

        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
    }

    /**
     * Test if the rays intersects the bounding box
     *
     * @param ray The ray
     * @return True if intersects
     */
    public boolean hit(Ray ray) {
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

        double t0, t1;

        // find largest entering t value

        if (tx_min > ty_min)
            t0 = tx_min;
        else
            t0 = ty_min;

        if (tz_min > t0)
            t0 = tz_min;

        // find smallest exiting t value

        if (tx_max < ty_max)
            t1 = tx_max;
        else
            t1 = ty_max;

        if (tz_max < t1)
            t1 = tz_max;

        return (t0 < t1 && t1 > GeometricObject.K_EPSILON);
    }

    /**
     * Test if the given point is inside the bounding box.
     *
     * @param p The point
     * @return True if the point is inside.
     */
    public boolean inside(Vector3 p) {
        return p.getX() > x0 && p.getX() < x1 &&
                p.getY() > y0 && p.getY() < y1 &&
                p.getZ() > z0 && p.getZ() < z1;
    }


}
