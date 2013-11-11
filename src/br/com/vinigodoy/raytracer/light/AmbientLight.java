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

/**
 * Defines an uniform global ambient light. Ambient lights leaves no shading and are usually used to represent the
 * "maximum darkness" of a scene. Use ambient lights to see object contours in almost absolute darkness.
 */
public class AmbientLight implements Light {
    private float ls;
    private Vector3 color;

    /**
     * Creates a new ambient light
     *
     * @param ls    Radiance scale (light power)
     * @param color Light color.
     */
    public AmbientLight(float ls, Vector3 color) {
        this.ls = ls;
        this.color = color;
    }

    /**
     * Creates a new ambient light with radiance scale = 1.0f.
     *
     * @param color Light color.
     */
    public AmbientLight(Vector3 color) {
        this(1.0f, color);
    }

    public float getLs() {
        return ls;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setRadianceScale(float ls) {
        this.ls = ls;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }

    @Override
    public AmbientLight clone() {
        return new AmbientLight(ls, color.clone());
    }

    @Override
    public Vector3 getDirection(ShadeRec sr) {
        return new Vector3();
    }

    @Override
    public Vector3 L(ShadeRec sr) {
        return Vector3.multiply(color, ls);
    }
}