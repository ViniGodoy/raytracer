package br.com.vinigodoy.raytrace.math;

/**
 * ***************************************************************************
 * <p/>
 * COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.
 * <p/>
 * This software cannot be copied, stored, distributed without
 * Vinícius G.Mendonça prior authorization.
 * <p/>
 * This file was made available on https://github.com/ViniGodoy and it
 * is free to be redistributed or used under Creative Commons license 2.5 br:
 * http://creativecommons.org/licenses/by-sa/2.5/br/
 * <p/>
 * *****************************************************************************
 */
public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    public Ray(Vector3 position, Vector3 direction) {
        this.origin = position;
        this.direction = Vector3.normalize(direction);
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3 origin) {
        this.origin = origin.clone();
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = Vector3.normalize(direction);
    }
}