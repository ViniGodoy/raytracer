/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.material;

import br.com.vinigodoy.raytracer.brdf.Lambertian;
import br.com.vinigodoy.raytracer.light.Light;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.mul;

public class Matte extends AbstractMaterial {

    private Lambertian diffuse;

    public Matte(float ka, float kd, Vector3 color) {
        super(ka, color);
        diffuse = new Lambertian(kd, color);
    }


    public void setKd(float k) {
        diffuse.setKd(k);
    }

    public float getKd() {
        return diffuse.getKd();
    }

    @Override
    public void setCd(Vector3 color) {
        ambient.setCd(color);
        diffuse.setCd(color);
    }

    @Override
    protected Vector3 processLight(ShadeRec sr, Vector3 wo, Light light, Vector3 wi, float ndotwi) {
        return mul(diffuse.f(sr, wo, wi), light.L(sr)).multiply(ndotwi);
    }

    @Override
    protected Vector3 processAreaLight(ShadeRec sr, Vector3 wo, Light light, Vector3 wi, float ndotwi) {
        return processLight(sr, wo, light, wi, ndotwi).multiply(light.G(sr) / light.pdf(sr));
    }

    @Override
    public Matte clone() {
        return new Matte(ambient.getKd(), diffuse.getKd(), ambient.getCd().clone());
    }
}
