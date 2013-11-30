/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.math.geometry.primitive;


import br.com.vinigodoy.raytracer.material.EmissiveMaterial;
import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector2;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.EmissiveObject;
import br.com.vinigodoy.raytracer.math.geometry.GeometricObject;
import br.com.vinigodoy.raytracer.sampler.Sampler;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.*;

public class Rectangle implements GeometricObject, EmissiveObject {

    private Vector3 p0;            // corner vertex
    private Vector3 a;              // side
    private double aLenSquared;
    private Vector3 b;                // side
    private double bLenSquared;

    private Vector3 normal;

    private float invArea;
    private Sampler sampler;

    private Material material;

    public Rectangle(Vector3 p0, Vector3 a, Vector3 b, Vector3 normal, Material material) {
        this.p0 = p0;

        this.a = a;
        this.aLenSquared = a.sizeSqr();

        this.b = b;
        this.bLenSquared = b.sizeSqr();

        this.invArea = 1.0f / a.size() * b.size();

        this.normal = normalize(normal);
        this.material = material;
    }

    public Rectangle(Vector3 p0, Vector3 a, Vector3 b, Material material) {
        this(p0, a, b, cross(a, b).normalize(), material);
    }

    public Rectangle(Material material) {
        this.p0 = new Vector3(-1, 0, 1);

        this.a = new Vector3(0, 0, 2);
        this.aLenSquared = 4;

        this.b = new Vector3(2, 0, 0);
        this.bLenSquared = 4;

        this.invArea = 0.25f;

        this.normal = new Vector3(0, 1, 0);
        this.material = material;
    }

    public Rectangle(Vector3 p0, Vector3 a, Vector3 b, Vector3 normal, EmissiveMaterial material, Sampler sampler) {
        this(p0, a, b, normal, material);
        this.sampler = sampler;
    }

    public Rectangle(Vector3 p0, Vector3 a, Vector3 b, EmissiveMaterial material, Sampler sampler) {
        this(p0, a, b, material);
        this.sampler = sampler;
    }

    public Rectangle(EmissiveMaterial material, Sampler sampler) {
        this(material);
        this.sampler = sampler;
    }

    public Vector3 getP0() {
        return p0;
    }

    public Vector3 getA() {
        return a;
    }

    public Vector3 getB() {
        return b;
    }

    @Override
    public Vector3 sample() {
        Vector2 sp = sampler.nextSampleSquare();
        return multiply(a, sp.getX()).add(multiply(b, sp.getY())).add(p0);
    }

    @Override
    public float pdf(ShadeRec sr) {
        return invArea;
    }

    @Override
    public Vector3 getNormal(Vector3 p) {
        return normal;
    }

    @Override
    public Rectangle clone() {
        Rectangle rect = new Rectangle(p0.clone(), a.clone(), b.clone(), normal.clone(), material.clone());
        rect.sampler = sampler == null ? null : sampler.clone();
        return rect;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        float t = subtract(p0, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);

        if (t <= K_EPSILON)
            return false;

        Vector3 p = ray.pointAt(t);
        Vector3 d = subtract(p, p0);

        double ddota = d.dot(a);
        if (ddota < 0.0 || ddota > aLenSquared)
            return false;

        double ddotb = d.dot(b);
        if (ddotb < 0.0 || ddotb > bLenSquared)
            return false;

        tmin.value = t;
        sr.normal = normal;
        sr.hitPoint = p;
        return true;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        if (material instanceof EmissiveMaterial)
            return false;

        float t = subtract(p0, ray.getOrigin()).dot(normal) / ray.getDirection().dot(normal);

        if (t <= K_EPSILON)
            return false;

        Vector3 p = ray.pointAt(t);
        Vector3 d = subtract(p, p0);

        double ddota = d.dot(a);
        if (ddota < 0.0 || ddota > aLenSquared)
            return false;

        double ddotb = d.dot(b);
        if (ddotb < 0.0 || ddotb > bLenSquared)
            return false;

        tmin.value = t;
        return true;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public EmissiveMaterial getEmissiveMaterial() {
        return (EmissiveMaterial) material;
    }
}
