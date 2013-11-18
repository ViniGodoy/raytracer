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
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class World {
    private volatile Thread renderThread;
    private String name;
    private Vector3 backgroundColor;
    private Tracer tracer;
    private Camera camera;

    private List<GeometricObject> objects = new ArrayList<GeometricObject>();
    private List<WorldListener> listeners = new ArrayList<WorldListener>();

    private Light ambientLight = new AmbientLight(0.5f, new Vector3(1.0f, 1.0f, 1.0f));

    private List<Light> lights = new ArrayList<Light>();

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

    public void add(GeometricObject obj) {
        objects.add(obj);
    }

    public void add(Light light) {
        lights.add(light);
    }

    public void render(final ViewPlane vp) {
        if (renderThread != null)
            throw new IllegalStateException("Cannot render two images at the same time!");

        renderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                fireTraceStarted(vp);
                long before = System.currentTimeMillis();
                camera.render(World.this, vp);
                long renderTime = (System.currentTimeMillis() - before);
                fireTraceFinished(renderTime);
                renderThread = null;
            }
        }, "Raytracer Render");
        renderThread.setDaemon(true);
        renderThread.start();
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
            FloatRef fr = new FloatRef();
            if (obj.hit(ray, sr, fr) && fr.value < tMin) {
                sr.hitAnObject = true;
                tMin = fr.value;
                sr.material = obj.getMaterial();
                normal = sr.normal;
                localHitPoint = sr.hitPoint;
            }
        }

        if (sr.hitAnObject) {
            sr.normal = normal;
            sr.hitPoint = localHitPoint;
        }
        return sr;
    }

    public boolean shadowHit(Ray ray, float d) {
        FloatRef t = new FloatRef();
        for (GeometricObject obj : objects)
            if (obj.shadow_hit(ray, t) && t.value < d)
                return true;

        return false;
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

    private void fireTraceStarted(ViewPlane vp) {
        for (WorldListener listener : listeners) {
            listener.traceStarted(this, vp.getHRes(), vp.getVRes());
        }
    }

    public void drawPixel(int x, int y, Vector3 color) {
        for (WorldListener listener : listeners) {
            listener.pixelTraced(this, x, y, color);
        }
    }

    private void fireTraceFinished(long renderTime) {
        for (WorldListener listener : new ArrayList<WorldListener>(listeners)) {
            listener.traceFinished(this, renderTime);
        }

    }

    public void addListener(WorldListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WorldListener listener) {
        listeners.add(listener);
    }
}
