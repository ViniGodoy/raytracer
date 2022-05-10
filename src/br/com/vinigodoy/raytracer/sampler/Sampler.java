/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.sampler;

import br.com.vinigodoy.raytracer.math.Vector2;
import br.com.vinigodoy.raytracer.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.com.vinigodoy.raytracer.utility.Rnd.rndInt;
import static java.lang.Math.*;

/**
 * Generate several sample sets of a given sample type. Also, contains methods to map samples into a disk and a
 * hemisphere.
 */
public final class Sampler implements Cloneable {
    public static final int DEFAULT_NUM_SETS = 83;

    private final List<Vector2> samples;
    private List<Vector2> diskSamples;
    private List<Vector3> hemisphereSamples;

    private final List<Integer> shuffledIndices = new ArrayList<>();
    private final Sample sample;
    private final int numSamples;
    private final int numSets;

    private int count = 0;
    private int jump = 0;

    public Sampler(Sample sample, int numSamples, int numSets) {
        this.samples = new ArrayList<>(numSets * numSamples);
        this.sample = sample;
        this.numSamples = numSamples <= 0 ? 1 : numSamples;
        this.numSets = numSets <= 0 ? 1 : numSets;
        createSamples(numSamples, numSets);
    }

    public Sampler(Sample sample, int numSamples) {
        this(sample, numSamples, DEFAULT_NUM_SETS);
    }

    public static Sampler newDefault(int numSamples) {
        numSamples = max(numSamples, 1);

        var sample = Samples.Regular;
        if (numSamples > 1) {
            final var n = (int) sqrt(numSamples);
            sample = n * n == numSamples ? Samples.MultiJittered : Samples.NRooks;
        }

        return new Sampler(sample, numSamples);
    }

    private void createSamples(int numSamples, int numSets) {
        samples.clear();
        for (var j = 0; j < numSets; j++)
            samples.addAll(sample.createSamples(numSamples));
        shuffleIndices();
    }

    private void shuffleIndices() {
        shuffledIndices.clear();
        final var indices = new ArrayList<Integer>();
        for (var i = 0; i < numSamples; i++)
            indices.add(i);

        for (var i = 0; i < numSets; i++) {
            Collections.shuffle(indices);
            shuffledIndices.addAll(indices);
        }
    }

    public Vector2 nextSampleSquare() {
        if (count % numSamples == 0) {
            jump = rndInt(numSets) * numSamples;
        }

        return samples.get(jump + shuffledIndices.get(jump + (count++ % numSamples)));
    }

    public Vector2 nextSampleDisk() {
        if (diskSamples == null)
            mapToDisk();

        if (count % numSamples == 0) {
            jump = rndInt(numSets) * numSamples;
        }

        return diskSamples.get(jump + shuffledIndices.get(jump + (count++ % numSamples)));
    }

    public Vector3 nextSampleHemisphere() {
        if (hemisphereSamples == null)
            mapToHemisphere(1.0f);

        if (count % numSamples == 0) {
            jump = rndInt(numSets) * numSamples;
        }

        return hemisphereSamples.get(jump + shuffledIndices.get(jump + (count++ % numSamples)));
    }

    public int getNumSamples() {
        return numSamples;
    }

    private int getNumSets() {
        return numSets;
    }

    /**
     * Maps the sample configuration of this samples to a disk.
     */
    private void mapToDisk() {
        diskSamples = new ArrayList<>(samples.size());
        float r;
        float phi;

        for (var sample : samples) {
            final var sp = new Vector2(2 * sample.getX() - 1.0f, 2 * sample.getY() - 1.0f);

            if (sp.getX() > -sp.getY()) {               //Sector 1
                if (sp.getX() > sp.getY()) {
                    r = sp.getX();
                    phi = sp.getY() / sp.getX();
                } else {                                //Sector 2
                    r = sp.getY();
                    phi = 2 - sp.getX() / sp.getY();
                }
            } else {
                if (sp.getX() < sp.getY()) {            //Sector 3
                    r = -sp.getX();
                    phi = 4 + sp.getY() / sp.getX();
                } else {                                //Sector 4
                    r = -sp.getY();
                    phi = sp.getY() != 0.0f ? 6 - sp.getX() / sp.getY() : 0.0f;
                }
            }
            phi *= PI / 4.0f;
            diskSamples.add(new Vector2((float) (r * cos(phi)), (float) (r * sin(phi))));
        }
    }

    public void mapToHemisphere(float e) {
        hemisphereSamples = new ArrayList<>(samples.size());
        for (var sample : samples) {
            final var cosPhi = (float) cos(2.0 * PI * sample.getX());
            final var sinPhi = (float) sin(2.0 * PI * sample.getX());
            final var cosTheta = (float) pow(1.0 - sample.getY(), 1.0 / (e + 1.0));
            final var sinTheta = (float) sqrt(1.0 - cosTheta * cosTheta);
            final var pu = sinTheta * cosPhi;
            final var pv = sinTheta * sinPhi;
            hemisphereSamples.add(new Vector3(pu, pv, cosTheta));
        }
    }

    @Override
    public Sampler clone() {
        return new Sampler(sample, numSamples, numSets);
    }
}
