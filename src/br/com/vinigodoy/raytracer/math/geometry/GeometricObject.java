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
import br.com.vinigodoy.raytracer.math.Ray;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

public interface GeometricObject {
    public static final float K_EPSILON = 0.000001f;

    HitResult hit(Ray ray, ShadeRec sr);

    Material getMaterial();


    public static class HitResult {
        public static final HitResult MISS = new HitResult(0, false);
        private float t;
        private boolean hit;

        private HitResult(float t, boolean hit) {
            this.t = t;
            this.hit = hit;
        }

        public HitResult(float t) {
            this(t, true);
        }

        public float getT() {
            return t;
        }

        public boolean isHit() {
            return hit;
        }
    }
}
