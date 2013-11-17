/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.scene;

import br.com.vinigodoy.raytracer.math.Vector3;

public interface WorldListener {

    /**
     * Indicate the scene rendering begun.
     *
     * @param world  World, source of the event
     * @param width  Viewport width
     * @param height Viewport height
     */
    void traceStarted(World world, int width, int height);

    /**
     * Indicate that a pixel was traced.
     *
     * @param world World, source of the event
     * @param x     Pixel x position
     * @param y     Pixel y position
     * @param color Pixel color
     */
    void pixelTraced(World world, int x, int y, Vector3 color);

    /**
     * Indicate that the image was fully rendered.
     *
     * @param world      World, source of the event
     * @param renderTime Time of rendering, in milliseconds.
     */
    void traceFinished(World world, long renderTime);
}
