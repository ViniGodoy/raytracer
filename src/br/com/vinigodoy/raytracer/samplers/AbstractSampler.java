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
    private static final Random RND = new Random();

    public static final int DEFAULT_NUM_SETS = 83;

    private List<Vector2> samples = new ArrayList<>();

    private List<Integer> shuffledIndices = new ArrayList<>();
    private int numSamples;
    private int numSets;

    private int count = 0;
    private int jump = 0;

    public AbstractSampler(int numSamples, int numSets) {
        this.numSamples = numSamples <= 0 ? 1 : numSamples;
        this.numSets = numSets <= 0 ? 1 : numSamples;
    }

    public AbstractSampler(int numSamples) {
        this(numSamples, DEFAULT_NUM_SETS);
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


    protected abstract void createSamples();

    public void init() {
        samples.clear();
        shuffledIndices.clear();

        createSamples();
        shuffleIndices();
    }

    public boolean isInit() {
        return !samples.isEmpty();
    }

    @Override
    public Vector2 nextSampleUnitSquare() {
        if (!isInit())
            init();
        if (count % numSamples == 0) {
            jump = RND.nextInt(numSets) * numSamples;
        }

        return samples.get(jump + shuffledIndices.get(jump + (count++ % numSamples)));
    }

    protected static float randomFloat() {
        return (float) (RND.nextInt(Integer.MAX_VALUE) / (double) (Integer.MAX_VALUE - 1));
    }

    protected static int randomInt(int max) {
        return RND.nextInt(max);
    }

    @Override
    public int getNumSamples() {
        return numSamples;
    }

    protected int getNumSets() {
        return numSets;
    }

    protected void addSample(Vector2 sample) {
        samples.add(sample);
    }

    protected Vector2 getSample(int sample) {
        return samples.get(sample);
    }
}
