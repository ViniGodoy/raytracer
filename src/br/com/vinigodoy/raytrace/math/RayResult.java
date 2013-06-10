/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G.Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.math;

public class RayResult {
    public enum Type {
        Miss, Hit, HitInside;
    }

    public static final RayResult MISS = new RayResult(Type.Miss, 0);

    private Type type;
    private float distance;

    public RayResult(Type type, float distance) {
        this.type = type;
        this.distance = distance;
    }

    public Type getType() {
        return type;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isMissed() {
        return getType() == Type.Miss;
    }

    public boolean isHit() {
        return !isMissed();
    }

    public boolean isInside() {
        return getType() == Type.HitInside;
    }
}
