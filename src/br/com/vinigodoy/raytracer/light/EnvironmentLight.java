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

import static br.com.vinigodoy.raytracer.math.Vector3.cross;

public class EnvironmentLight extends AbstractLight {
    private Sampler sampler;
    private EmissiveMaterial material;
    private UVW uvw;
    private Vector3 wi;

    public EnvironmentLight(EmissiveMaterial material, Sampler sampler) {
        this.sampler = sampler;
        this.material = material;
    }

    @Override
    public Vector3 getDirection(ShadeRec sr) {
        Vector3 w = sr.normal;
        Vector3 v = cross(new Vector3(0.0034f, 1.0000f, 0.0071f), w).normalize();
        Vector3 u = cross(v, w);

        uvw = new UVW(u, v, w);
        return wi = uvw.transform(sampler.nextSampleHemisphere());
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
