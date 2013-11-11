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
import br.com.vinigodoy.raytracer.utility.RaytraceException;

import java.util.ArrayList;
import java.util.List;

import static br.com.vinigodoy.raytracer.utility.Rnd.rndFloat;
import static br.com.vinigodoy.raytracer.utility.Rnd.rndInt;


/**
 * Enumeration of the default sampling algorithms.
 */
public enum Samples implements Sample {
    /**
     * Generate samples equally spaced around the center.
     */
    Regular {
        @Override
        protected void fillSamples(List<Vector2> samples, int numSamples) {
            int n = (int) Math.sqrt(numSamples);
            for (int p = 0; p < n; p++)
                for (int q = 0; q < n; q++)
                    samples.add(new Vector2((q + 0.5f) / n, (p + 0.5f) / n));
        }
    },
    /**
     * Generate samples circularly distributed around the center.
     */
    Hammersley {
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

        @Override
        protected void fillSamples(List<Vector2> samples, int numSamples) {
            for (int j = 0; j < numSamples; j++) {
                samples.add(new Vector2((float) j / numSamples, phi(j)));
            }
        }
    },
    /**
     * Generate pure randomly distributed pixels
     */
    Random {
        @Override
        protected void fillSamples(List<Vector2> samples, int numSamples) {
            for (int i = 0; i < numSamples; i++)
                samples.add(new Vector2(rndFloat(), rndFloat()));
        }
    },
    /**
     * Breaks the area into clusters and generate random pixels inside the clusters.
     * This will result in a better distribution than a pure random algorithm.
     *
     * @see Samples#Random
     */
    Jittered {
        @Override
        protected void fillSamples(List<Vector2> samples, int numSamples) {
            int n = (int) Math.sqrt(numSamples);

            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++) {
                    samples.add(new Vector2((k + rndFloat()) / n, (j + rndFloat()) / n));
                }
        }
    },
    /**
     * Create samples based in the n-rooks condition.
     */
    NRooks {
        @Override
        protected void fillSamples(List<Vector2> samples, int numSamples) {
            //Create samples satisfying the n-rooks condition
            for (int j = 0; j < numSamples; j++) {
                samples.add(new Vector2((j + rndFloat()) / numSamples, (j + rndFloat()) / numSamples));
            }

            //Shuffle x coordinates
            for (int i = 0; i < numSamples - 1; i++) {
                int max = numSamples - 1 - i;
                int ind = rndInt(max);
                float temp = samples.get(max).getX();
                samples.get(max).setX(samples.get(ind).getX());
                samples.get(ind).setX(temp);

            }

            ////Shuffle y coordinates
            for (int i = 0; i < numSamples - 1; i++) {
                int max = numSamples - 1 - i;
                int ind = rndInt(max);
                float temp = samples.get(max).getY();
                samples.get(max).setY(samples.get(ind).getY());
                samples.get(ind).setY(temp);
            }
        }
    },
    /**
     * Creates a very good distribution by combining the n-rooks condition with a jittered sampling. This technique can
     * only be used with a perfect square number of samples.
     */
    MultiJittered {
        @Override
        protected void fillSamples(List<Vector2> samples, int numSamples) {
            int n = (int) Math.sqrt((float) numSamples);

            if (n * n != numSamples) {
                throw new RaytraceException("Number of samples are %d but must be a perfect square like %d!",
                        numSamples, n * n);
            }

            float subcellWidth = 1.0f / ((float) numSamples);

            // fill the samples array with dummy points
            for (int j = 0; j < numSamples; j++)
                samples.add(new Vector2());

            // distribute points in the initial patterns
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) {
                    samples.get(i * n + j).setX((i * n + j) * subcellWidth + rndFloat(0.0f, subcellWidth));
                    samples.get(i * n + j).setY((j * n + i) * subcellWidth + rndFloat(0.0f, subcellWidth));
                }

            // shuffle x coordinates
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) {
                    int k = rndInt(j, n - 1);
                    float t = samples.get(i * n + j).getX();
                    samples.get(i * n + j).setX(samples.get(i * n + k).getX());
                    samples.get(i * n + k).setX(t);
                }

            // shuffle y coordinates
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) {
                    int k = rndInt(j, n - 1);
                    float t = samples.get(i * n + j).getY();
                    samples.get(i * n + j).setY(samples.get(i * n + k).getY());
                    samples.get(i * n + k).setY(t);
                }
        }
    };

    protected List<Vector2> createList(int numSamples) {
        if (numSamples < 0)
            throw new IllegalArgumentException("Number of samples cannot be smaller than zero! numSamples=" + numSamples);
        return new ArrayList<>(numSamples);

    }

    @Override
    public List<Vector2> createSamples(int numSamples) {
        if (numSamples < 1)
            throw new RaytraceException("Number of samples cannot be smaller than one! numSamples = %d", numSamples);
        List<Vector2> samples = new ArrayList<>(numSamples);
        fillSamples(samples, numSamples);
        return samples;
    }

    protected abstract void fillSamples(List<Vector2> samples, int numSamples);
}
