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
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.mul;
import static br.com.vinigodoy.raytracer.math.Vector3.negate;

public abstract class AbstractMaterial implements Material {
    protected final Lambertian ambient;

    public AbstractMaterial(float ka, Vector3 color) {
        this.ambient = new Lambertian(ka, color);
    }

    private Vector3 shade(ShadeRec sr, boolean area) {
        final var wo = negate(sr.ray.getDirection());
        final var L = mul(ambient.rho(sr, wo), sr.world.getAmbientLight().L(sr));
        sr.world.getLights().forEach(light -> {
            final var wi = light.getDirection(sr);

            final var ndotwi = sr.normal.dot(wi);
            if (ndotwi > 0.0f) {
                var inShadow = false;
                if (light.castShadows()) {
                    final var shadowRay = new Ray(sr.worldHitPoint, wi);
                    inShadow = light.inShadow(shadowRay, sr);
                }

                if (!inShadow) {
                    L.add(area ? processLight(sr, wo, light, wi, ndotwi) : processAreaLight(sr, wo, light, wi, ndotwi));
                }
            }
        });
        return L;
    }

    @Override
    public Vector3 shade(ShadeRec sr) {
        return shade(sr, false);
    }

    @Override
    public Vector3 areaLightShade(ShadeRec sr) {
        return shade(sr, true);
    }

    public void setKa(float k) {
        ambient.setKd(k);
    }

    public void setCd(Vector3 color) {
        ambient.setCd(color);
    }

    public float getKa() {
        return ambient.getKd();
    }

    public Vector3 getCd() {
        return ambient.getCd();
    }


    public abstract Material clone();

    protected abstract Vector3 processLight(ShadeRec sr, Vector3 wo, Light light, Vector3 wi, float ndotwi);

    protected Vector3 processAreaLight(ShadeRec sr, Vector3 wo, Light light, Vector3 wi, float ndotwi) {
        return processLight(sr, wo, light, wi, ndotwi).multiply(light.G(sr) / light.pdf(sr));
    }
}
