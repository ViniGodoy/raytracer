package br.com.vinigodoy.raytracer.scene;

/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
public class ViewPlane {
    private int hRes;
    private int vRes;
    private float s;
    private float gamma;

    public ViewPlane(int hRes, int vRes) {
        this(hRes, vRes, 1.0f, 1.0f);
    }

    public ViewPlane(int hRes, int vRes, float pixelSize, float gamma) {
        this.hRes = hRes;
        this.vRes = vRes;
        this.s = pixelSize;
        this.gamma = gamma;
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
}
