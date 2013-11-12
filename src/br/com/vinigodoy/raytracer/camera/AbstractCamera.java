/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.camera;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.scene.ViewPlane;

import java.awt.*;

import static br.com.vinigodoy.raytracer.math.Vector3.*;

public abstract class AbstractCamera implements Camera {
    protected Vector3 eye;
    protected Vector3 look;
    protected Vector3 up;

    protected float roll;
    protected float exposureTime;

    public AbstractCamera(Vector3 eyePosition, Vector3 lookPoint, Vector3 upDirection) {
        this.eye = eyePosition;
        this.look = lookPoint;
        this.up = upDirection;

        this.roll = 0;
        this.exposureTime = 1.0f;
    }

    public Vector3 getEye() {
        return eye;
    }

    public Vector3 getLook() {
        return look;
    }

    public Vector3 getUp() {
        return up;
    }

    public float getRoll() {
        return roll;
    }

    public float getExposureTime() {
        return exposureTime;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setExposureTime(float exposureTime) {
        this.exposureTime = exposureTime;
    }

    protected UVW computeUVW() {
        Vector3 w = subtract(eye, look).normalize();

        Vector3 up = rotate(this.up, w, roll);
        // take care of the singularity by hardwiring in specific camera orientations
        if (eye.getX() == look.getX() && eye.getZ() == look.getZ() && eye.getY() > look.getY()) { // camera looking vertically down
            return new UVW(
                    new Vector3(0, 0, 1),
                    new Vector3(1, 0, 0),
                    new Vector3(0, 1, 0));
        }

        if (eye.getX() == look.getX() && eye.getZ() == look.getZ() && eye.getY() < look.getY()) { // camera looking vertically up
            return new UVW(
                    new Vector3(1, 0, 0),
                    new Vector3(0, 0, 1),
                    new Vector3(0, -1, 0));
        }

        //Otherwise, calculate UVW
        Vector3 u = cross(up, w).normalize();
        Vector3 v = cross(w, u);

        return new UVW(u, v, w);
    }

    protected static class UVW {
        private Vector3 u;
        private Vector3 v;
        private Vector3 w;

        public UVW(Vector3 u, Vector3 v, Vector3 w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        public Vector3 getU() {
            return u;
        }

        public Vector3 getV() {
            return v;
        }

        public Vector3 getW() {
            return w;
        }
    }

    public void drawPixel(Graphics2D g2d, ViewPlane vp, int row, int col, Vector3 color) {
        if (vp.getGamma() != 1.0f)
            color.pow(vp.invGamma());

        g2d.setColor(saturate(color).toColor());
        int invR = vp.getVRes() - row - 1;
        g2d.drawLine(col, invR, col, invR);
    }
}
