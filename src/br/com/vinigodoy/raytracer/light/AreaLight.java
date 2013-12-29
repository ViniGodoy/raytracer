package br.com.vinigodoy.raytracer.light;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.EmissiveObject;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.negate;
import static br.com.vinigodoy.raytracer.math.Vector3.subtract;

/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
public class AreaLight extends AbstractLight {

    private EmissiveObject object;

    private Vector3 samplePoint;
    private Vector3 lightNormal;
    private Vector3 wi;

    public AreaLight(EmissiveObject object) {
        this.object = object;
    }

    @Override
    public Vector3 getDirection(ShadeRec sr) {
        samplePoint = object.sample();
        lightNormal = object.getNormal(samplePoint);
        wi = subtract(samplePoint, sr.worldHitPoint).normalize();
        return wi;
    }

    @Override
    public Vector3 L(ShadeRec sr) {
        return negate(lightNormal).dot(wi) > 0.0f ?
                object.getEmissiveMaterial().getLe(sr) : new Vector3();
    }

    @Override
    public float G(ShadeRec sr) {
        float ndotd = negate(lightNormal).dot(wi);
        float d2 = samplePoint.distanceSqr(sr.worldHitPoint);
        return ndotd / d2;
    }

    @Override
    public float pdf(ShadeRec sr) {
        return object.pdf(sr);
    }

    @Override
    public boolean inShadow(Ray ray, ShadeRec sr) {
        float ts = subtract(samplePoint, ray.getOrigin()).dot(ray.getDirection());
        return sr.world.shadowHit(ray, ts);
    }

    @Override
    public AreaLight clone() {
        return new AreaLight(object.clone());
    }
}
