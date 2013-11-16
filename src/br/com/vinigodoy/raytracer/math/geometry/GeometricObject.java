/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math.geometry;

import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

public interface GeometricObject {
    public static final float K_EPSILON = 0.1f;

    /**
     * Test if the object was hit by the ray and return shading information about the hit point.
     *
     * @param ray  The ray test.
     * @param sr   A ShadeRec with all hit point properties
     * @param tmin Returns the hit distance.
     * @return The hit result.
     */
    boolean hit(Ray ray, ShadeRec sr, FloatRef tmin);

    /**
     * Test if the object was hit by the ray. This is a faster version of hit function, but without shading information.
     *
     * @param ray  The ray test.
     * @param tmin Returns the hit distance.
     * @return The hit result.
     */
    boolean shadow_hit(Ray ray, FloatRef tmin);

    Material getMaterial();
}
