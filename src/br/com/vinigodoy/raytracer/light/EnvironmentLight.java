/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.light;

import br.com.vinigodoy.raytracer.material.EmissiveMaterial;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.sampler.Sampler;
import br.com.vinigodoy.raytracer.utility.ShadeRec;
import br.com.vinigodoy.raytracer.utility.UVW;

public class EnvironmentLight extends AbstractLight {
    private final Sampler sampler;
    private final EmissiveMaterial material;

    public EnvironmentLight(EmissiveMaterial material, Sampler sampler) {
        this.sampler = sampler;
        this.material = material;
    }

    @Override
    public Vector3 getDirection(ShadeRec sr) {
        return UVW.from(sr.normal, new Vector3(0.0034f, 1.0000f, 0.0071f))
                .transform(sampler.nextSampleHemisphere());
    }

    @Override
    public Vector3 L(ShadeRec sr) {
        return material.getLe(sr);
    }

    @Override
    public boolean inShadow(Ray ray, ShadeRec sr) {
        return sr.world.shadowHit(ray, Float.MAX_VALUE);
    }

    @Override
    public EnvironmentLight clone() {
        return new EnvironmentLight(material.clone(), sampler.clone());

    }
}
