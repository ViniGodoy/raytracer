/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G.Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.scene;

import br.com.vinigodoy.raytrace.math.Vector3;

public class Material {
    private Vector3 color;
    private float diffuse;
    private float reflection;
    private float refractionIndex;

    public Material(Vector3 color, float diffuse, float reflection) {
        this.color = color;
        this.diffuse = diffuse;
        this.reflection = reflection;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }

    public float getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(float diffuse) {
        this.diffuse = diffuse;
    }

    public float getSpecular() {
        return 1.0f - diffuse;
    }

    public float getReflection() {
        return reflection;
    }

    public void setReflection(float reflection) {
        this.reflection = reflection;
    }

    public boolean isTransparent() {
        return refractionIndex > 0;
    }

    public float getRefractionIndex() {
        return refractionIndex;
    }

    public void setRefractionIndex(float refractionIndex) {
        this.refractionIndex = refractionIndex;
    }
}
