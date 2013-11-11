/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.brdf;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

/**
 * Interface for bidirectional reflectance distribution functions. These are functions that represent how the light is
 * reflected at surface.
 */
public interface BRDF {
    /**
     * Compute this BRDF.
     *
     * @param sr Surface point.
     * @param wo Reflected light direction.
     * @param wi Incoming light direction.
     * @return The luminance at point
     */
    Vector3 f(ShadeRec sr, Vector3 wo, Vector3 wi);

    /**
     * Samples the f function.
     *
     * @param sr Surface point.
     * @param wo Reflected light direction.
     * @param wi Incoming light direction.
     * @return The luminance at point
     */
    Vector3 sample_f(ShadeRec sr, Vector3 wo, Vector3 wi);

    /**
     * Samples the f function.
     *
     * @param sr  Surface point.
     * @param wo  Reflected light direction.
     * @param wi  Incoming light direction.
     * @param pdf Probability density function.
     * @return The luminance at point
     */
    Vector3 sample_f(ShadeRec sr, Vector3 wo, Vector3 wi, FloatRef pdf);

    /**
     * Returns the bihemispherical reflectance Phh
     *
     * @param sr Surface point.
     * @param wo Reflected light direction.
     * @return The the bihemispherical reflectance Phh
     */
    Vector3 rho(ShadeRec sr, Vector3 wo);
}
