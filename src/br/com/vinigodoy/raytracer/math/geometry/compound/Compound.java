/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math.geometry.compound;

import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.BBox;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.math.geometry.Instance;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import java.util.ArrayList;
import java.util.List;

public class Compound implements GeometricObject {
    private final List<GeometricObject> objects = new ArrayList<>();
    private BBox bounds;

    public Compound add(GeometricObject obj) {
        objects.add(obj);
        return this;
    }
    
    public Compound addAll(GeometricObject ... objs) {
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

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        if (bounds != null && !bounds.hit(ray))
            return false;

        Vector3 normal = null;
        Vector3 worldHitPoint = null;
        Vector3 localHitPoint = null;
        var tMin = Float.MAX_VALUE;
        var hit = false;

        for (var obj : objects) {
            final var fr = new FloatRef();
            if (obj.hit(ray, sr, fr) && fr.value < tMin) {
                hit = true;
                tMin = fr.value;
                //sr.material = obj.getMaterial();
                normal = sr.normal;
                worldHitPoint = sr.worldHitPoint;
                localHitPoint = sr.localHitPoint;
            }
        }

        if (hit) {
            tmin.value = tMin;
            sr.normal = normal;
            sr.worldHitPoint = worldHitPoint;
            sr.localHitPoint = localHitPoint;
        }
        return hit;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        if (bounds != null && !bounds.hit(ray)) return false;

        var tMin = Float.MAX_VALUE;
        var hit = false;

        for (var obj : objects) {
            final var fr = new FloatRef();
            if (obj.shadow_hit(ray, fr) && fr.value < tMin) {
                hit = true;
                tMin = fr.value;
            }
        }

        if (hit) tmin.value = tMin;

        return hit;
    }

    @Override
    public Material getMaterial() {
        return objects.get(0).getMaterial();
    }

    public void setBounds(BBox bounds) {
        this.bounds = bounds;
    }

    public BBox getBounds() {
        return bounds;
    }
}
