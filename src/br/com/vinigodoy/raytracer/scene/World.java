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
import br.com.vinigodoy.raytracer.light.AmbientLight;
import br.com.vinigodoy.raytracer.light.Light;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.tracer.Tracer;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.com.vinigodoy.raytracer.math.geometry.GeometricObject.HitResult;

public class World {
    private Vector3 backgroundColor;
    private Tracer tracer;
    private Camera camera;

    private List<GeometricObject> objects = new ArrayList<>();

    private Light ambientLight = new AmbientLight(1.0f, new Vector3(1.0f, 1.0f, 1.0f));

    private List<Light> lights = new ArrayList<>();

    public World(Tracer tracer, Vector3 backgroundColor, Camera camera) {
        this.tracer = tracer;
        this.backgroundColor = backgroundColor;
        this.camera = camera;
    }

    public void add(GeometricObject obj) {
        objects.add(obj);
    }

    public void add(Light light) {
        lights.add(light);
    }

    public BufferedImage render(ViewPlane vp) {
        return camera.render(this, vp);
    }

    public Vector3 getBackgroundColor() {
        return backgroundColor;
    }

    public ShadeRec hit(Ray ray) {
        ShadeRec sr = new ShadeRec(this);

        Vector3 normal = null;
        Vector3 localHitPoint = null;
        float tMin = Float.MAX_VALUE;

        for (GeometricObject obj : objects) {
            HitResult hr = obj.hit(ray, sr);
            if (hr.isHit() && hr.getT() < tMin) {
                sr.hitAnObject = true;
                tMin = hr.getT();
                sr.material = obj.getMaterial();
                normal = sr.normal;
                localHitPoint = sr.localHitPoint;
            }
        }

        if (sr.hitAnObject) {
            sr.normal = normal;
            sr.localHitPoint = localHitPoint;
        }
        return sr;
    }

    public Tracer getTracer() {
        return tracer;
    }

    public Light getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Light ambientLight) {
        this.ambientLight = ambientLight;
    }

    public List<Light> getLights() {
        return Collections.unmodifiableList(lights);
    }
}
