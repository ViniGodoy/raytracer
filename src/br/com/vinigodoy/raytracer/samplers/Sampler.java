/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.samplers;

import br.com.vinigodoy.raytracer.math.Vector2;

/**
 * Produces a set of points for sampling.
 */
public interface Sampler {
    /**
     * @return The next sample point considering a square as sampling shape.
     */
    Vector2 nextSampleUnitSquare();

    /**
     * @return The number of generated sample points.
     */
    int getNumSamples();
}
