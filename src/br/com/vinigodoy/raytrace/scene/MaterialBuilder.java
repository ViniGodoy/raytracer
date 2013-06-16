/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.
 
This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.scene;

import br.com.vinigodoy.raytrace.math.Vector3;

public class MaterialBuilder {
    private Vector3 color;
    private float diffuse;
    private float specular;
    private float reflection;
    private float refractionIndex;

    public MaterialBuilder() {
        this.color = new Vector3(0.2f, 0.2f, 0.2f);
        this.diffuse = 0.2f;
        this.specular = 0.8f;
        this.reflection = 0.0f;
        this.refractionIndex = 0.0f;
    }

    public MaterialBuilder withDiffuse(float diffuse) {
        this.diffuse = diffuse;
        return this;
    }

    public MaterialBuilder withColor(float r, float g, float b) {
        color.set(r, g, b);
        return this;
    }

    public MaterialBuilder withSpecular(float specular) {
        this.specular = specular;
        return this;
    }

    public MaterialBuilder withReflection(float reflection) {
        this.reflection = reflection;
        return this;
    }

    public MaterialBuilder withRefraction(float refraction) {
        this.refractionIndex = refraction;
        return this;
    }

    public Material make() {
        return new Material(color, diffuse, specular, reflection, refractionIndex);
    }
}
