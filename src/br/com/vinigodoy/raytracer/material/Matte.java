/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.material;

import br.com.vinigodoy.raytracer.brdf.Lambertian;
import br.com.vinigodoy.raytracer.light.Light;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.mul;
import static br.com.vinigodoy.raytracer.math.Vector3.negate;

public class Matte implements Material {
    private Lambertian ambient = new Lambertian();
    private Lambertian diffuse = new Lambertian();

    public Matte(float ka, float kd, Vector3 color) {
        ambient.setKd(ka);
        ambient.setCd(color);

        diffuse.setKd(kd);
        diffuse.setCd(color);
    }

    public void setKa(float k) {
        ambient.setKd(k);
    }

    public void setKd(float k) {
        diffuse.setKd(k);
    }

    public void setCd(Vector3 color) {
        ambient.setCd(color);
        diffuse.setCd(color);
    }

    @Override
    public Vector3 shade(ShadeRec sr) {
        Vector3 wo = negate(sr.ray.getDirection());
        Vector3 L = mul(ambient.rho(sr, wo), sr.world.getAmbientLight().L(sr));
        for (Light light : sr.world.getLights()) {
            Vector3 wi = light.getDirection(sr);

            float ndotwi = sr.normal.dot(wi);
            if (ndotwi > 0.0f)
                L.add(mul(diffuse.f(sr, wo, wi), light.L(sr)).multiply(ndotwi));
        }
        return L;
    }

    @Override
    public Matte clone() {
        return new Matte(ambient.getKd(), diffuse.getKd(), ambient.getCd().clone());
    }
}
