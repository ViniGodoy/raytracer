/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.scene;

import br.com.vinigodoy.raytracer.samplers.RegularSampler;
import br.com.vinigodoy.raytracer.samplers.Sampler;

public class ViewPlane {
    private int hRes;
    private int vRes;
    private float s;
    private float gamma;
    private Sampler sampler;


    public ViewPlane(int hRes, int vRes, int numSamples) {
        this(hRes, vRes, 1.0f, 1.0f, new RegularSampler(numSamples));
    }

    public ViewPlane(int hRes, int vRes, float pixelSize, float gamma, Sampler sampler) {
        this.hRes = hRes;
        this.vRes = vRes;
        this.s = pixelSize;
        this.gamma = gamma;
        this.sampler = sampler;
    }

    public int getHRes() {
        return hRes;
    }

    public int getVRes() {
        return vRes;
    }

    public float getS() {
        return s;
    }

    public float getGamma() {
        return gamma;
    }

    public float invGamma() {
        return 1.0f / gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public Sampler getSampler() {
        return sampler;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    public void setSamples(int num) {
        sampler = new RegularSampler(num);
    }
}
