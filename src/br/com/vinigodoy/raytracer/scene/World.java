/* ===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.scene;

import br.com.vinigodoy.raytracer.camera.Camera;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.tracer.Tracer;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static br.com.vinigodoy.raytracer.math.geometry.GeometricObject.HitResult;

public class World {
    private Vector3 backgroundColor;
    private Tracer tracer;
    private Camera camera;

    private List<GeometricObject> objects = new ArrayList<>();

    public World(Tracer tracer, Vector3 backgroundColor, Camera camera) {
        this.tracer = tracer;
        this.backgroundColor = backgroundColor;
        this.camera = camera;
    }

    public void add(GeometricObject obj) {
        objects.add(obj);
    }

    public BufferedImage render(ViewPlane vp) {
        return camera.render(this, vp);
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

    public Tracer getTracer() {
        return tracer;
    }
}
