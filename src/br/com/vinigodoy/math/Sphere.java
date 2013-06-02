package br.com.vinigodoy.math;

import static br.com.vinigodoy.math.Vector3.subtract;

/**
 * ***************************************************************************
 * <p/>
 * COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.
 * <p/>
 * This software cannot be copied, stored, distributed without
 * Vinícius G.Mendonça prior authorization.
 * <p/>
 * This file was made available on https://github.com/ViniGodoy and it
 * is free to be redistributed or used under Creative Commons license 2.5 br:
 * http://creativecommons.org/licenses/by-sa/2.5/br/
 * <p/>
 * *****************************************************************************
 */
public class Sphere implements Shape {
    private Vector3 center;
    private float radius;

    public Sphere(Vector3 center, float radius)
    {
        this.center = center.clone();
        this.radius = radius;
    }

    @Override
    public RayResult intersects(Ray ray, float distance) {
        Vector3 v = subtract(ray.getOrigin(), center);
        float b = -v.dot(ray.getDirection());
        float det = (b * b) - v.sizeSqr() + radius;

        if (det <= 0)
            return RayResult.MISS;

        det = (float)Math.sqrt(det);
        float i2 = b + det;
        if (i2 <= 0)
            return RayResult.MISS;

        float i1 = b - det;
        if (i1 < 0)
        {
            if (i2 < distance)
                return new RayResult(RayResult.Type.HitInside, i2);
        }
        else
        {
            if (i1 < distance)
                return new RayResult(RayResult.Type.Hit, i1);
        }

        return RayResult.MISS;
    }

    @Override
    public Vector3 getNormal(Vector3 position) {
        return subtract(position, center).normalize();
    }

    public Vector3 getCenter() {
        return center;
    }
}
