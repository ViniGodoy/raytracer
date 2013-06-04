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
public class RayResult {
    public enum Type {
        Miss, Hit, HitInside;
    }

    public static RayResult MISS = new RayResult(Type.Miss, 0);

    private Type type;
    private float dist;

    public RayResult(Type type, float dist)
    {
        this.type = type;
        this.dist = dist;
    }

    public Type getType() {
        return type;
    }

    public float getDist() {
        return dist;
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
