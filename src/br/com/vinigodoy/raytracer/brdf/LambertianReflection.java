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
import br.com.vinigodoy.raytracer.sampler.Sampler;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

/**
 * Represents perfect diffuse reflection, where incident radiance is scattered equally in all directions.
 */
public class LambertianReflection extends AbstractBRDF {
    private static final float INVPI = (float) (1.0 / Math.PI);

    private float kd;
    private Vector3 cd;

    /**
     * @param sampler Sampler to use.
     * @param kd      Diffuse reflection coefficient.
     * @param cd      Diffuse color
     */
    public LambertianReflection(Sampler sampler, float kd, Vector3 cd) {
        super(sampler);
        this.kd = kd;
        this.cd = cd;
    }

    @Override
    public Vector3 f(ShadeRec sr, Vector3 wo, Vector3 wi) {
        return cd.multiply(kd * INVPI);
    }

    @Override
    public Vector3 rho(ShadeRec sr, Vector3 wo) {
        return cd.multiply(kd);
    }

    public float getKd() {
        return kd;
    }

    public Vector3 getCd() {
        return cd;
    }

    public void setKd(float kd) {
        this.kd = kd;
    }

    public void setCd(Vector3 cd) {
        this.cd = cd;
    }
}
