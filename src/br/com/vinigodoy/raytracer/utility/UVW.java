package br.com.vinigodoy.raytracer.utility;

import br.com.vinigodoy.raytracer.math.Vector2;
import br.com.vinigodoy.raytracer.math.Vector3;

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
public class UVW {
    private Vector3 u;
    private Vector3 v;
    private Vector3 w;

    public UVW(Vector3 u, Vector3 v, Vector3 w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public Vector3 getU() {
        return u;
    }

    public Vector3 getV() {
        return v;
    }

    public Vector3 getW() {
        return w;
    }

    public Vector3 transform(Vector3 vec) {
        return multiply(u, vec.getX())
                .add(multiply(v, vec.getY()))
                .add(multiply(w, vec.getZ()));
    }

    public Vector3 transform(Vector2 vec, float z) {
        return multiply(u, vec.getX())
                .add(multiply(v, vec.getY()))
                .add(multiply(w, z));
    }

}