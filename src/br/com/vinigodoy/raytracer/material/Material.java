/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.material;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.utility.ShadeRec;

public interface Material extends Cloneable {

    /**
     * Provides shading information indefinite area lights, such as Ambient, Point and Directional lights.
     *
     * @param sr The shade rect
     * @return The shading color
     */
    Vector3 shade(ShadeRec sr);

    /**
     * Provides shading information area lights
     *
     * @param sr The shade rect
     * @return The shading color
     */
    Vector3 areaLightShade(ShadeRec sr);

    Material clone();
}
