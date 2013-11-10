/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.cameras;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector2;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.scene.ViewPlane;
import br.com.vinigodoy.raytracer.scene.World;

import java.awt.*;
import java.awt.image.BufferedImage;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;

public class PinholeCamera extends AbstractCamera {
    private float d;
    private float zoom;

    public PinholeCamera(Vector3 eyePosition, Vector3 lookPoint, Vector3 upDirection, float d) {
        super(eyePosition, lookPoint, upDirection);
        this.d = d;
        this.zoom = 1.0f;
    }

    public float getD() {
        return d;
    }

    public float getZoom() {
        return zoom;
    }

    public void setD(float d) {
        this.d = d;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    @Override
    public BufferedImage render(World w, ViewPlane vp) {
        BufferedImage img = new BufferedImage(vp.getHRes(), vp.getVRes(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        UVW uvw = computeUVW();


        int n = (int) Math.sqrt(vp.getSampler().getNumSamples());
        float s = vp.getS() / zoom;

        for (int r = 0; r < vp.getVRes(); r++)
            for (int c = 0; c < vp.getHRes(); c++) {
                Vector3 L = new Vector3();

                for (int p = 0; p < n; p++)
                    for (int q = 0; q < n; q++) {
                        Vector2 pp = new Vector2(
                                s * (c - 0.5f * vp.getHRes() + (q + 0.5f) / n),
                                s * (r - 0.5f * vp.getVRes() + (p + 0.5f) / n));
                        Ray ray = new Ray(eye, getDirection(pp, uvw));
                        L.add(w.getTracer().trace(w, ray, 0));
                    }

                L.divide(vp.getSampler().getNumSamples()).multiply(exposureTime);

                drawPixel(g2d, vp, r, c, L);
            }
        g2d.dispose();
        return img;
    }

    public Vector3 getDirection(Vector2 p, UVW uvw) {
        return multiply(uvw.getU(), p.getX())
                .add(multiply(uvw.getV(), p.getY()))
                .add(multiply(uvw.getW(), -d)).normalize();
    }
}
