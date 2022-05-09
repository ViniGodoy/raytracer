/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.camera;


import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector2;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.sampler.Sampler;
import br.com.vinigodoy.raytracer.scene.ViewPlane;
import br.com.vinigodoy.raytracer.scene.World;
import br.com.vinigodoy.raytracer.utility.UVW;

import static br.com.vinigodoy.raytracer.math.Vector2.multiply;
import static br.com.vinigodoy.raytracer.math.Vector3.add;
import static br.com.vinigodoy.raytracer.math.Vector3.multiply;

public class ThinLensCamera extends AbstractCamera {
    private float zoom;
    private float viewPlaneDistance;
    private float focalDistance;
    private float lensRadius;

    private Sampler sampler;

    public ThinLensCamera(Vector3 eyePosition, Vector3 lookPoint, Vector3 upDirection, float viewPlaneDistance,
                          float focalDistance, float lensRadius, Sampler sampler) {
        super(eyePosition, lookPoint, upDirection);
        this.viewPlaneDistance = viewPlaneDistance;
        this.focalDistance = focalDistance;
        this.lensRadius = lensRadius;
        this.zoom = 1.0f;
        this.sampler = sampler;
    }

    Vector3 getDirection(Vector2 pixel, Vector2 lensPoint, UVW uvw) {
        var p = multiply(pixel, focalDistance / viewPlaneDistance).subtract(lensPoint);
        return uvw.transform(p, -focalDistance).normalize();
    }

    @Override
    public void render(World world, ViewPlane vp) {
        var uvw = computeUVW();
        var s = vp.getS() / zoom;

        for (var pixel : vp.getPixels()) {
            var c = pixel.x();
            var r = pixel.y();

            var L = new Vector3();

            for (var i = 0; i < vp.getSampler().getNumSamples(); i++) {
                var sp = vp.getSampler().nextSampleSquare();

                var pp = new Vector2(
                    s * (c - 0.5f * vp.getHRes() + sp.getX()),
                    s * (r - 0.5f * vp.getVRes() + sp.getY())
                );

                var dp = sampler.nextSampleDisk();
                var lp = multiply(dp, lensRadius);
                var o = add(eye, multiply(uvw.u(), lp.getX()))
                        .add(multiply(uvw.v(), lp.getY()));

                var ray = new Ray(o, getDirection(pp, lp, uvw));
                L.add(world.getTracer().trace(world, ray, 0));
            }

            L.divide(vp.getSampler().getNumSamples()).multiply(exposureTime);
            drawPixel(world, vp, c, r, L);
        }
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getViewPlaneDistance() {
        return viewPlaneDistance;
    }

    public void setViewPlaneDistance(float viewPlaneDistance) {
        this.viewPlaneDistance = viewPlaneDistance;
    }

    public float getFocalDistance() {
        return focalDistance;
    }

    public void setFocalDistance(float focalDistance) {
        this.focalDistance = focalDistance;
    }

    public float getLensRadius() {
        return lensRadius;
    }

    public void setLensRadius(float lensRadius) {
        this.lensRadius = lensRadius;
    }

    public Sampler getSampler() {
        return sampler;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }
}
