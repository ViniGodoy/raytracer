/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.material;

import br.com.vinigodoy.raytracer.brdf.GlossySpecular;
import br.com.vinigodoy.raytracer.brdf.Lambertian;
import br.com.vinigodoy.raytracer.light.Light;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.mul;

public class Phong extends AbstractMaterial {
    private final Lambertian diffuse;
    private final GlossySpecular specular;

    public Phong(float ka, float kd, float ks, float exp, Vector3 color) {
        super(ka, color);
        diffuse = new Lambertian(kd, color);
        specular = new GlossySpecular(ks, color, exp);
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

    public void setKs(float k) {
        specular.setKs(k);
    }

    public float getKs() {
        return specular.getKs();
    }

    public void setExp(float exp) {
        specular.setExp(exp);
    }

    public float getExp() {
        return specular.getExp();
    }


    public void setCs(Vector3 color) {
        specular.setCs(color);
    }

    public Vector3 getCs() {
        return specular.getCs();
    }

    protected Vector3 processLight(ShadeRec sr, Vector3 wo, Light light, Vector3 wi, float ndotwi) {
        return mul(diffuse.f(sr, wo, wi).add(specular.f(sr, wo, wi)), light.L(sr)).multiply(ndotwi);
    }

    @Override
    public Phong clone() {
        var p = new Phong(getKa(), getKd(), getKs(), getExp(), getCd().clone());
        p.setCs(getCs().clone());
        return p;
    }
}