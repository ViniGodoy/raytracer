package br.com.vinigodoy.raytracer.tracer;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.scene.World;

/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
public class AreaLightTracer implements Tracer {
    @Override
    public Vector3 trace(World world, Ray ray, int depth) {
        final var sr = world.hit(ray).clone();
        if (!sr.hitAnObject) return world.getBackgroundColor();
        sr.ray = ray;
        return sr.material.shade(sr);
    }
}
