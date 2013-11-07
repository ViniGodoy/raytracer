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

public class HammersleySampler extends AbstractSampler {
    public HammersleySampler(int numSamples) {
        super(numSamples);
    }

    public HammersleySampler(int numSamples, int numSets) {
        super(numSamples, numSets);
    }

    private float phi(int j) {
        double x = 0.0;
        double f = 0.5;

        while (j != 0) {
            x += f * (double) (j % 2);
            j /= 2;
            f *= 0.5;
        }

        return (float) x;
    }


    public void createSamples() {
        for (int p = 0; p < numSets; p++)
            for (int j = 0; j < numSamples; j++) {
                samples.add(new Vector2((float) j / numSamples, phi(j)));
            }
    }
}