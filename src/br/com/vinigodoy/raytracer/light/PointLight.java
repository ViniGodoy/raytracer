/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.light;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

import static br.com.vinigodoy.raytracer.math.Vector3.multiply;
import static br.com.vinigodoy.raytracer.math.Vector3.subtract;

/**
 * Represents a point irradiating light. It's usually a reasonable simplification of lamps.
 */
public class PointLight implements Light {
    private float ls;
    private Vector3 color;
    private Vector3 position;

    /**
     * Creates a new PointLight.
     *
     * @param ls       Radiance scale (light power)
     * @param color    Light color
     * @param position Light position (in world coordinates).
     */
    public PointLight(float ls, Vector3 color, Vector3 position) {
        this.ls = ls;
        this.color = color;
        this.position = position;
    }

    @Override
    public Vector3 getDirection(ShadeRec sr) {
        return subtract(position, sr.hitPoint);
    }

    @Override
    public Vector3 L(ShadeRec sr) {
        return multiply(color, ls);
    }

    @Override
    public PointLight clone() {
        return new PointLight(ls, color.clone(), position.clone());
    }

    /**
     * @return The Radiance Scale (light power)
     */
    public float getLs() {
        return ls;
    }

    /**
     * Changes the Radiance Scale of this light.
     *
     * @param ls The new radiance scale (light power).
     */
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
     * @return The light position in world coordinates
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Sets the light position in world coordinates
     *
     * @param position The new position
     */
    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
