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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class AbstractSampler implements Sampler {
    protected static final int DEFAULT_NUM_SETS = 83;

    protected static final Random RND = new Random();

    protected int numSamples;
    protected int numSets;

    protected List<Vector2> samples = new ArrayList<>();
    protected List<Integer> shuffledIndices = new ArrayList<>();

    protected int count = 0;
    protected int jump = 0;

    public AbstractSampler(int numSamples, int numSets) {
        this.numSamples = numSamples <= 0 ? 1 : numSamples;
        this.numSets = numSets <= 0 ? 1 : numSamples;
        shuffleIndices();
    }

    private void shuffleIndices() {
        shuffledIndices.clear();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numSamples; i++)
            indices.add(i);

        for (int i = 0; i < numSets; i++) {
            Collections.shuffle(indices);
            shuffledIndices.addAll(indices);
        }
    }

    @Override
    public Vector2 nextSampleUnitSquare() {
        if (count % numSamples == 0) {
            jump = RND.nextInt(numSets) * numSamples;
        }

        return samples.get(jump + shuffledIndices.get((jump + count++) % numSamples));
    }

    @Override
    public int getNumSamples() {
        return numSamples;
    }
}
