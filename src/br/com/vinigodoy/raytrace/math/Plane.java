package br.com.vinigodoy.raytrace.math;

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
public class Plane implements Shape {
    private Vector3 normal;
    private float distance;

    public Plane()
    {
        this.normal = new Vector3(0,1,0);
        this.distance = 0;
    }

    public Plane(Vector3 normal, float distance)
    {
        this.normal = Vector3.normalize(normal);
        this.distance = distance;
    }

    @Override
    public RayResult intersects(Ray ray, float maxDist) {
        float d = normal.dot(ray.getDirection());

        //Ray is parallel to the plane. Never hit.
        if (d == 0)
            return RayResult.MISS;

        //Calculate hit distance. If 0 or negative, missed.
        float dist = -(normal.dot(ray.getOrigin()) + distance) / d;

        if (dist <= 0 || dist >= maxDist)
            return RayResult.MISS;


        //Returns the calculated distance.
        return new RayResult(RayResult.Type.Hit, dist);
    }

    @Override
    public Vector3 getNormal(Vector3 position) {
        return normal;
    }
}
