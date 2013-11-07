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

public class JitteredSampler extends AbstractSampler {
    public JitteredSampler(int numSamples) {
        super(numSamples);
    }

    public JitteredSampler(int numSamples, int numSets) {
        super(numSamples, numSets);
    }

    @Override
    public void createSamples() {
        int n = (int) Math.sqrt(getNumSamples());

        for (int p = 0; p < getNumSets(); p++)
            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++) {
                    addSample(new Vector2((k + randomFloat()) / n, (j + randomFloat()) / n));
                }
    }
}
