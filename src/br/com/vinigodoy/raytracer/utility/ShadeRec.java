/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.utility;


import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.scene.World;

public class ShadeRec {
    public boolean hitAnObject = false;
    public Vector3 localHitPoint = null;
    public Vector3 normal = null;
    public Vector3 color;
    public World world;

    public ShadeRec(World world) {
        this.world = world;
        this.color = world.getBackgroundColor();
    }
}
