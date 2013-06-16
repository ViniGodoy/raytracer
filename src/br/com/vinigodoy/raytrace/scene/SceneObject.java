/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.scene;

import br.com.vinigodoy.raytrace.math.Shape;

public class SceneObject {
    private String name;
    private Shape shape;
    private Material material;
    private boolean light;

    public SceneObject(String name, Shape shape, Material material, boolean isLight) {
        this.name = name;
        this.shape = shape;
        this.material = material;
        this.light = isLight;
    }

    public SceneObject(String name, Shape shape, Material material) {
        this(name, shape, material, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }
}
