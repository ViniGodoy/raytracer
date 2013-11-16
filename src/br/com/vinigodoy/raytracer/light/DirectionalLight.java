/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.light;

import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;
import static br.com.vinigodoy.raytracer.math.Vector3.normalize;

/**
 * Represents light comming from a given direction in the entire scene. This is not a real light source, but it is
 * usually a good simplification for really far light sources, such as the sun light.
 */
public class DirectionalLight extends AbstractLight {
    private float ls;
    private Vector3 color;
    private Vector3 direction;

    /**
     * Creates a new directional light.
     *
     * @param ls        Radiance scale (light power)
     * @param color     Light color
     * @param direction Direction of the light beams.
     */
    public DirectionalLight(float ls, Vector3 color, Vector3 direction) {
        this.ls = ls;
        this.color = color;
        this.direction = normalize(direction);
    }

    @Override
    public Vector3 getDirection(ShadeRec sr) {
        return direction;
    }

    @Override
    public Vector3 L(ShadeRec sr) {
        return multiply(color, ls);
    }

    @Override
    public DirectionalLight clone() {
        return new DirectionalLight(ls, color.clone(), direction.clone());
    }

    /**
     * @return The Radiance scale (light power)
     */
    public float getLs() {
        return ls;
    }

    public void setLs(float ls) {
        this.ls = ls;
    }

    /**
     * @return This light color
     */
    public Vector3 getColor() {
        return color;
    }

    /**
     * Changes this light color
     *
     * @param color The new color
     */
    public void setColor(Vector3 color) {
        this.color = color;
    }

    /**
     * @param direction Changes this light direction.
     */
    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

    @Override
    public boolean inShadow(Ray ray, ShadeRec sr) {
        return sr.world.shadowHit(ray, Float.MAX_VALUE);
    }
}
