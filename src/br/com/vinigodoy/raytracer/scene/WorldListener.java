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
     * @param backgroundColor Background color.
     */
    void traceStarted(int width, int height, Vector3 backgroundColor);

    /**
     * Indicate that a pixel was traced.
     *
     * @param x     Pixel x position
     * @param y     Pixel y position
     * @param color Pixel color
     */
    void pixelTraced(int x, int y, Vector3 color);

    /**
     * Indicate that the image was fully rendered.
     */
    void traceFinished(double renderTime);
}
