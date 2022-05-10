package br.com.vinigodoy.raytracer.utility;

import br.com.vinigodoy.raytracer.math.Vector2;
import br.com.vinigodoy.raytracer.math.Vector3;

import static br.com.vinigodoy.raytracer.math.Vector3.cross;
import static br.com.vinigodoy.raytracer.math.Vector3.multiply;

/*
===========================================================================
COPYRIGHT 2022 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
public record UVW(Vector3 u, Vector3 v, Vector3 w) {
    public static UVW from(Vector3 w, Vector3 approxV) {
        final var v = cross(approxV, w).normalize();
        return new UVW(cross(v, w), v, w);
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