package br.com.vinigodoy.raytracer.utility;

/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

import java.util.Random;

/**
 * Random number generator utilities
 */
public class Rnd {
    private static final Random RND = new Random();

    public synchronized static float rndFloat() {
        return RND.nextFloat();
    }

    public static float rndFloat(float min, float max) {
        return min + rndFloat() * (max - min);
    }

    public synchronized static int rndInt(int range) {
        return RND.nextInt(range);
    }

    public synchronized static int rndInt(int min, int max) {
        var range = max - min;
        return range == 0 ? min : min + RND.nextInt(range);
    }
}
