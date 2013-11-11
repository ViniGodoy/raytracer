/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.brdf;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.sampler.Sampler;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

public abstract class AbstractBRDF implements BRDF {
    private Sampler sampler;

    public AbstractBRDF(Sampler sampler) {
        this.sampler = sampler;
    }

    public Sampler getSampler() {
        return sampler;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public Vector3 sample_f(ShadeRec sr, Vector3 wo, Vector3 wi) {
        return new Vector3();
    }

    @Override
    public Vector3 sample_f(ShadeRec sr, Vector3 wo, Vector3 wi, FloatRef pdf) {
        return new Vector3();
    }

    @Override
    public Vector3 rho(ShadeRec sr, Vector3 wo) {
        return new Vector3();
    }
}
