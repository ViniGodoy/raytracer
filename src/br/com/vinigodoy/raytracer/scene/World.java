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
import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.math.geometry.Instance;
import br.com.vinigodoy.raytracer.tracer.Tracer;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class World {
    private Thread renderThread;
    private String name;
    private final Vector3 backgroundColor;
    private final Tracer tracer;
    private final Camera camera;

    private final List<GeometricObject> objects = new ArrayList<>();
    private final List<WorldListener> listeners = new ArrayList<>();

    private Light ambientLight = new AmbientLight(0.5f, new Vector3(1.0f, 1.0f, 1.0f));

    private final List<Light> lights = new ArrayList<>();

    public World(String name, Tracer tracer, Vector3 backgroundColor, Camera camera) {
        this.name = name;
        this.tracer = tracer;
        this.backgroundColor = backgroundColor;
        this.camera = camera;
    }

    public World(Tracer tracer, Vector3 backgroundColor, Camera camera) {
        this(String.format("%1$tF", Calendar.getInstance()), tracer, backgroundColor, camera);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World add(GeometricObject obj) {
        objects.add(obj);
        return this;
    }

    public World addAll(GeometricObject ... objs) {
        for (var obj : objs) add(obj);
        return this;
    }

    public Instance addInstance(GeometricObject obj) {
        final var instance = new Instance(obj);
        objects.add(instance);
        return instance;
    }

    public Instance addInstance(GeometricObject obj, Material mtrl) {
        final var instance = new Instance(obj, mtrl);
        objects.add(instance);
        return instance;
    }

    public World add(Light light) {
        lights.add(light);
        return this;
    }

    public World addAll(Light ... lights) {
        for (var light : lights) add(light);
        return this;
    }

    public void render(final ViewPlane vp) {
        if (renderThread != null) throw new IllegalStateException("Cannot render two images at the same time!");

        renderThread = new Thread(() -> {
            fireTraceStarted(vp);
            final var before = System.currentTimeMillis();
            camera.render(World.this, vp);
            final var renderTime = (System.currentTimeMillis() - before);
            fireTraceFinished(renderTime);
            renderThread = null;
        }, "Raytracer Render");
        renderThread.setDaemon(true);
        renderThread.start();
    }

    public Vector3 getBackgroundColor() {
        return backgroundColor;
    }

    public ShadeRec hit(Ray ray) {
        final var sr = new ShadeRec(this);

        Vector3 normal = null;
        Vector3 hitPoint = null;
        var tMin = Float.MAX_VALUE;

        for (var obj : objects) {
            final var fr = new FloatRef();
            if (obj.hit(ray, sr, fr) && fr.value < tMin) {
                sr.hitAnObject = true;
                tMin = fr.value;
                sr.material = obj.getMaterial();
                normal = sr.normal;
                hitPoint = sr.worldHitPoint;
            }
        }

        if (sr.hitAnObject) {
            sr.normal = normal;
            sr.worldHitPoint = hitPoint;
        }
        return sr;
    }

    public boolean shadowHit(Ray ray, float d) {
        final var t = new FloatRef();
        return objects.stream().anyMatch(obj -> obj.shadow_hit(ray, t) && t.value < d);
    }

    public Tracer getTracer() {
        return tracer;
    }

    public Light getAmbientLight() {
        return ambientLight;
    }

    public World setAmbientLight(Light ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public List<Light> getLights() {
        return Collections.unmodifiableList(lights);
    }

    private void fireTraceStarted(ViewPlane vp) {
        listeners.forEach(l -> l.traceStarted(this, vp.getHRes(), vp.getVRes()));
    }

    public void drawPixel(int x, int y, Vector3 color) {
        listeners.forEach(l -> l.pixelTraced(this, x, y, color));
    }

    private void fireTraceFinished(long renderTime) {
        new ArrayList<>(listeners).forEach(l -> l.traceFinished(this, renderTime));
    }

    public World addListener(WorldListener listener) {
        listeners.add(listener);
        return this;
    }

    public World removeListener(WorldListener listener) {
        listeners.add(listener);
        return this;
    }
}
