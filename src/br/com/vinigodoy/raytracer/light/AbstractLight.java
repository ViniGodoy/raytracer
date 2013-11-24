/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/
package br.com.vinigodoy.raytracer.light;


import br.com.vinigodoy.raytracer.utility.ShadeRec;

public abstract class AbstractLight implements Light {
    private boolean castShadows = true;

    public AbstractLight() {
    }

    @Override
    public float G(ShadeRec sr) {
        return 1.0f;
    }

    @Override
    public float pdf(ShadeRec sr) {
        return 1.0f;
    }

    @Override
    public boolean castShadows() {
        return castShadows;
    }

    public void setCastShadows(boolean castShadows) {
        this.castShadows = castShadows;
    }

    public abstract Light clone();
}
