/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.math.geometry;

import br.com.vinigodoy.raytracer.material.EmissiveMaterial;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

/**
 * Represents an object capable of emmiting lights
 */
public interface EmissiveObject {
    /**
     * @return A sampled point in objects surface
     */
    Vector3 sample();

    /**
     * Calculate the pdf at a given point
     *
     * @param sr Shade rect of the point
     * @return The pdf.
     */
    float pdf(ShadeRec sr);

    /**
     * Calculates the normal at the given point
     *
     * @param p The point
     * @return The normal
     */
    Vector3 getNormal(Vector3 p);

    /**
     * Clones this object.
     *
     * @return The object clone
     */
    EmissiveObject clone();

    /**
     * @return The emissive material associated with this object
     */
    EmissiveMaterial getEmissiveMaterial();
}
