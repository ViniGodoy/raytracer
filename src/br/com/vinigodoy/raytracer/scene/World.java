/* ===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.scene;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.tracers.Tracer;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static br.com.vinigodoy.raytracer.math.geometry.GeometricObject.HitResult;

public class World {
    private Vector3 backgroundColor;
    private Tracer tracer;

    private List<GeometricObject> objects = new ArrayList<>();

    public World(Tracer tracer, Vector3 backgroundColor) {
        this.tracer = tracer;
        this.backgroundColor = backgroundColor;

    }

    public void add(GeometricObject obj) {
        objects.add(obj);
    }

    public BufferedImage render(ViewPlane vp) {
        BufferedImage img = new BufferedImage(vp.getHRes(), vp.getVRes(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        final Vector3 DIRECTION = new Vector3(0, 0, -1);
        final float ZW = 100.0f;

        for (int row = 0; row < vp.getVRes(); row++) {
            for (int col = 0; col <= vp.getHRes(); col++) {
                float x = vp.getS() * (col - 0.5f * (vp.getHRes() - 1));
                float y = vp.getS() * (row - 0.5f * (vp.getVRes() - 1));
                Ray ray = new Ray(new Vector3(x, y, ZW), DIRECTION);
                Vector3 color = tracer.trace(this, ray);

                //Displays the pixels
                if (vp.getGamma() != 1.0f)
                    color.pow(vp.invGamma());

                g2d.setColor(color.toColor());
                int invR = vp.getVRes() - row - 1;
                g2d.drawLine(col, invR, col, invR);
            }
        }
        g2d.dispose();
        return img;
    }

    public Vector3 getBackgroundColor() {
        return backgroundColor;
    }

    public ShadeRec hit(Ray ray) {
        ShadeRec sr = new ShadeRec(this);
        float tMin = Float.MAX_VALUE;

        for (GeometricObject obj : objects) {
            HitResult hr = obj.hit(ray, sr);
            if (hr.isHit() && hr.getT() < tMin) {
                sr.hitAnObject = true;
                tMin = hr.getT();
                sr.color = obj.getColor();
            }
        }
        return sr;
    }
}
