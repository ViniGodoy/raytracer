/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.utility;


import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.scene.World;

public class ShadeRec implements Cloneable {
    /**
     * Indicate if the ray hit an object
     */
    public boolean hitAnObject = false;

    /**
     * Nearest object material
     */
    public Material material;

    /**
     * World coordinates of hit point.
     */
    public Vector3 worldHitPoint = null;

    /**
     * Local coordinates of hit point. Will only be different from world coordinates in transformed objects.
     */
    public Vector3 localHitPoint = null;

    /**
     * Normal at hit point.
     */
    public Vector3 normal = null;

    //For specular highlights
    public Ray ray;

    /**
     * Recursion depth.
     */
    public int depth;

    //For area lights
    public Vector3 dir;

    public final World world;

    public ShadeRec(World world) {
        this.world = world;
    }

    @Override
    public ShadeRec clone() {
        final var sr = new ShadeRec(world);
        sr.hitAnObject = hitAnObject;
        sr.material = material == null ? null : material.clone();
        sr.worldHitPoint = worldHitPoint == null ? null : worldHitPoint.clone();
        sr.ray = ray == null ? null : new Ray(ray.getOrigin(), ray.getDirection());
        sr.depth = depth;
        sr.dir = dir == null ? null : dir.clone();
        sr.normal = normal == null ? null : normal.clone();
        return sr;
    }
}
