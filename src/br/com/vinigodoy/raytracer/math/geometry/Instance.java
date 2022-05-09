/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math.geometry;

import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.Matrix4;
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.FloatRef;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

public class Instance implements GeometricObject {
    private GeometricObject object;
    private Material material;
    private Matrix4 invTransform = Matrix4.newIdentity();

    public Instance(GeometricObject object) {
        this(object, null);
    }

    public Instance(GeometricObject object, Material material) {
        this.object = object;
        this.material = material;
    }

    public void setObject(GeometricObject object) {
        this.object = object;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr, FloatRef tmin) {
        var inv_ray = invTransform.transformRay(ray);

        if (object.hit(inv_ray, sr, tmin)) {
            var transform = Matrix4.inverse(invTransform);
            sr.worldHitPoint = transform.transformPoint(sr.worldHitPoint);
            sr.normal = invTransform.transformNormal(sr.normal);
            return true;
        }

        return false;
    }

    @Override
    public boolean shadow_hit(Ray ray, FloatRef tmin) {
        return object.shadow_hit(invTransform.transformRay(ray), tmin);
    }

    @Override
    public Material getMaterial() {
        return material != null ? material : object.getMaterial();
    }

    public Instance translate(float x, float y, float z) {
        invTransform.multiply(Matrix4.newInvTranslation(x, y, z));
        return this;
    }

    public Instance translate(Vector3 position) {
        invTransform.multiply(Matrix4.newInvTranslation(position));
        return this;
    }

    public Instance rotateX(float angle) {
        invTransform.multiply(Matrix4.newInvRotationX(angle));
        return this;
    }

    public Instance rotateY(float angle) {
        invTransform.multiply(Matrix4.newInvRotationY(angle));
        return this;
    }

    public Instance rotateZ(float angle) {
        invTransform.multiply(Matrix4.newInvRotationZ(angle));
        return this;
    }

    public Instance scale(float x, float y, float z) {
        invTransform.multiply(Matrix4.newInvScale(x, y, z));
        return this;
    }

    public Instance scale(float scale) {
        invTransform.multiply(Matrix4.newInvScale(scale));
        return this;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Instance clone() {
        var instance = new Instance(object, material.clone());
        instance.invTransform = invTransform.clone();
        return instance;
    }
}
