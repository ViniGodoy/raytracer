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
import br.com.vinigodoy.raytracer.scene.World;

public interface Camera {
    /**
     * Renders the scene using this camera.
     *
     * @param world World to render
     * @param vp    View plane
     */
    void render(World world, ViewPlane vp);

    /**
     * @return The camera location.
     */
    Vector3 getEye();

    /**
     * @return The place where the camera looks at.
     */
    Vector3 getLook();

    /**
     * @return A vector pointing up
     */
    Vector3 getUp();
}
