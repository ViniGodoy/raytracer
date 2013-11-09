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

import static br.com.vinigodoy.raytracer.utility.Rnd.rndInt;

/**
 * Generate several sample sets of a given sample type. Also, contains methods to map samples into a disk and a
 * hemisphere.
 */
public final class Sampler {
    public static final int DEFAULT_NUM_SETS = 83;

    private List<Vector2> samples = new ArrayList<>();

    private List<Integer> shuffledIndices = new ArrayList<>();
    private Sample sample;
    private int numSamples;
    private int numSets;

    private int count = 0;
    private int jump = 0;

    public Sampler(Sample sample, int numSamples, int numSets) {
        this.sample = sample;
        this.numSamples = numSamples <= 0 ? 1 : numSamples;
        this.numSets = numSets <= 0 ? 1 : numSamples;
        createSamples(numSamples, numSets);
    }

    public Sampler(Sample sample, int numSamples) {
        this(sample, numSamples, DEFAULT_NUM_SETS);
    }

    public static Sampler newDefault(int numSamples) {
        numSamples = Math.max(numSamples, 1);

        Sample sample = Samples.Regular;
        if (numSamples > 1) {
            int n = (int) Math.sqrt(numSamples);
            sample = n * n == numSamples ? Samples.MultiJittered : Samples.NRooks;
        }

        return new Sampler(sample, numSamples);
    }

    private void createSamples(int numSamples, int numSets) {
        samples.clear();
        for (int j = 0; j < numSets; j++)
            samples.addAll(sample.createSamples(numSamples));
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

    public Vector2 nextSampleUnitSquare() {
        if (count % numSamples == 0) {
            jump = rndInt(numSets) * numSamples;
        }

        return samples.get(jump + shuffledIndices.get(jump + (count++ % numSamples)));
    }

    public int getNumSamples() {
        return numSamples;
    }

    protected int getNumSets() {
        return numSets;
    }
}
