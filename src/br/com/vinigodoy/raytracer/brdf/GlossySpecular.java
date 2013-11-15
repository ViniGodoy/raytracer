/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.brdf;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;

/**
 * Models a glossy specular light distribution
 */
public class GlossySpecular extends AbstractBRDF {

    private float ks;
    private Vector3 cs;
    private float exp;

    /**
     * Creates a new Glossy Specular BRDF.
     *
     * @param ks  Specular reflection coefficient
     * @param cs  Specular color
     * @param exp Specular power
     */
    public GlossySpecular(float ks, Vector3 cs, float exp) {
        this.ks = ks;
        this.cs = cs;
        this.exp = exp;
    }

    @Override
    public Vector3 f(ShadeRec sr, Vector3 wo, Vector3 wi) {
        Vector3 L = new Vector3();

        Vector3 r = Vector3.reflect(wi, sr.normal);
        float rDotWo = r.dot(wo);
        if (rDotWo > 0.0f)
            L = multiply(cs, ks * (float) Math.pow(rDotWo, exp));
        return L;
    }

    /**
     * @return The specular reflection coefficient
     */
    public float getKs() {
        return ks;
    }

    /**
     * Changes the specular reflection coefficient
     *
     * @param ks The new coefficient
     */
    public void setKs(float ks) {
        this.ks = ks;
    }

    /**
     * @return The specular reflection color
     */
    public Vector3 getCs() {
        return cs;
    }

    /**
     * Sets the specular reflection color
     *
     * @param cs the new color
     */
    public void setCs(Vector3 cs) {
        this.cs = cs;
    }

    /**
     * @return The specular power
     */
    public float getExp() {
        return exp;
    }

    /**
     * Changes the specular power
     *
     * @param exp The new power
     */
    public void setExp(float exp) {
        this.exp = exp;
    }
}
