/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.light;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

/**
 * Base class for all light sources.
 */
public interface Light extends Cloneable {
    /**
     * Returns the light incident direction at the shade rect.
     *
     * @param sr The shade rect
     * @return Light direction.
     */
    Vector3 getDirection(ShadeRec sr);

    /**
     * Calculates the luminance for a given shade rect.
     *
     * @param sr The shade rect.
     * @return The luminance
     */
    Vector3 L(ShadeRec sr);

    /**
     * Calculates the geometric factor for a given shade rect
     *
     * @param sr The shade rect
     * @return The geometric factor
     */
    float G(ShadeRec sr);

    /**
     * Calculates the probability density function for a given shade rect.
     *
     * @param sr The shade rect
     * @return The probability density function result.
     */
    float pdf(ShadeRec sr);

    /**
     * @return True if this light can cast shadows.
     */
    boolean castShadows();

    /**
     * Test if the sr is in shadow considering the given ray of light
     *
     * @param ray Ray of light
     * @param sr  Shade rect to test
     * @return True, if it is in shadow.
     */
    boolean inShadow(Ray ray, ShadeRec sr);

    /**
     * @return A clone this light.
     */
    Light clone();
}