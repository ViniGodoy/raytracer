package br.com.vinigodoy.raytracer.light;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.sampler.Sampler;
import br.com.vinigodoy.raytracer.utility.ShadeRec;
import br.com.vinigodoy.raytracer.utility.UVW;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;

/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
public class AmbientOccludedLight extends AbstractLight {
    private float ls;
    private Vector3 color;

    private final float minAmount;
    private final Sampler sampler;
    private UVW uvw;

    public AmbientOccludedLight(float ls, Vector3 color, float minAmount, Sampler sampler) {
        this.ls = ls;
        this.color = color;
        this.minAmount = minAmount;
        this.sampler = sampler;
    }

    public float getLs() {
        return ls;
    }

    public void setLs(float ls) {
        this.ls = ls;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }

    @Override
    public Vector3 getDirection(ShadeRec sr) {
        return uvw.transform(sampler.nextSampleHemisphere());
    }

    @Override
    public boolean inShadow(Ray ray, ShadeRec sr) {
        return sr.world.shadowHit(ray, Float.MAX_VALUE);
    }

    @Override
    public Vector3 L(ShadeRec sr) {
        uvw = UVW.from(sr.normal, new Vector3(0.0072f, 1.0f, 0.0034f));

        var shadow_ray = new Ray(sr.worldHitPoint, getDirection(sr));
        return multiply(color, inShadow(shadow_ray, sr) ? ls * minAmount : ls);
    }

    @Override
    public AmbientOccludedLight clone() {
        return new AmbientOccludedLight(ls, color.clone(), minAmount, sampler.clone());
    }
}
