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

public class NRooksSampler extends AbstractSampler {
    public NRooksSampler(int numSamples) {
        super(numSamples);
    }

    public NRooksSampler(int numSamples, int numSets) {
        super(numSamples, numSets);
    }

    @Override
    public void createSamples() {
        for (int p = 0; p < getNumSets(); p++)
            for (int j = 0; j < getNumSamples(); j++) {
                addSample(new Vector2((j + randomFloat()) / getNumSamples(), (j + randomFloat()) / getNumSamples()));
            }

        shuffleCoordinates();
    }

    private void shuffleCoordinates() {
        //Shuffle x coordinates
        for (int p = 0; p < getNumSets(); p++)
            for (int i = 0; i < getNumSamples() - 1; i++) {
                int target = randomInt(getNumSamples()) + p * getNumSamples();
                float temp = getSample(i + p * getNumSamples() + 1).getX();
                getSample(i + p * getNumSamples() + 1).setX(getSample(target).getX());
                getSample(target).setX(temp);
            }

        //Shuffle y coordinates
        for (int p = 0; p < getNumSets(); p++)
            for (int i = 0; i < getNumSamples() - 1; i++) {
                int target = randomInt(getNumSamples()) + p * getNumSamples();
                float temp = getSample(i + p * getNumSamples() + 1).getY();
                getSample(i + p * getNumSamples() + 1).setY(getSample(target).getY());
                getSample(target).setY(temp);
            }
    }
}
