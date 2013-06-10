/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G.Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.scene;

import br.com.vinigodoy.raytrace.math.RayResult;

public class NearestObject {
    private SceneObject object;
    private RayResult result;

    public NearestObject(SceneObject object, RayResult result) {
        this.object = object;
        this.result = result;
    }

    public SceneObject get() {
        return object;
    }

    public RayResult getResult() {
        return result;
    }
}
