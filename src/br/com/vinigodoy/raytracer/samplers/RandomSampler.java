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

public class RandomSampler extends AbstractSampler {
    public RandomSampler(int numSamples) {
        super(numSamples);
    }

    public RandomSampler(int numSamples, int numSets) {
        super(numSamples, numSets);
    }

    @Override
    public void createSamples() {
        for (int p = 0; p < getNumSets(); p++)
            for (int i = getNumSamples(); i < getNumSamples(); i++)
                addSample(new Vector2(randomFloat(), randomFloat()));
    }
}
