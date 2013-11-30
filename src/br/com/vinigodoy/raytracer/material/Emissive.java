/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.material;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;
import static br.com.vinigodoy.raytracer.math.Vector3.negate;

/**
 * Represents a material that can emit light.
 */
public class Emissive implements EmissiveMaterial {
    private float ls;
    private Vector3 ce;

    public Emissive(float ls, Vector3 color) {
        this.ls = ls;
        this.ce = color;
    }

    public Vector3 getLe(ShadeRec sr) {
        return multiply(ce, ls);
    }

    @Override
    public Vector3 shade(ShadeRec sr) {
        return negate(sr.normal).dot(sr.ray.getDirection()) > 0 ?
                multiply(ce, ls) : new Vector3();
    }

    @Override
    public Vector3 areaLightShade(ShadeRec sr) {
        return shade(sr);
    }

    @Override
    public Emissive clone() {
        return new Emissive(ls, ce.clone());
    }
}
