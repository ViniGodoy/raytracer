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

import java.util.List;

/**
 * A sample is a set of distributed points in a (0,1)-(0,1) space.
 * There are several algorithms to generate samples, usually in a random pattern.
 */
public interface Sample {
    /**
     * Create a set of samples.
     *
     * @param numSamples Number of samples in the set
     * @return The samples.
     * @throws br.com.vinigodoy.raytracer.utility.RaytraceException
     *          If tne number of samples is smaller than 1.
     */
    List<Vector2> createSamples(int numSamples);
}
