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

public class RegularSampler extends AbstractSampler {
    public RegularSampler(int numSamples) {
        super(numSamples, 1);
    }

    @Override
    public void init() {
        int n = (int) Math.sqrt(numSamples);

        for (int j = 0; j < numSets; j++)
            for (int p = 0; p < n; p++)
                for (int q = 0; q < n; q++)
                    samples.add(new Vector2((q + 0.5f) / n, (p + 0.5f) / n));
    }


}
