package br.com.vinigodoy.scene;

import br.com.vinigodoy.math.Plane;
import br.com.vinigodoy.math.Sphere;
import br.com.vinigodoy.math.Vector3;

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
public class SceneBuilder {
    private Scene scene = new Scene();

    public SceneBuilder addPlane(String name, Vector3 normal, float distance, Material material)
    {
        scene.add(new SceneObject(name, new Plane(normal, distance), material));
        return this;
    }

    public SceneBuilder addSphere(String name, Vector3 center, float radius, Material material)
    {
        scene.add(new SceneObject(name, new Sphere(center, radius), material));
        return this;
    }

    public SceneBuilder addLight(String name, Vector3 center, Vector3 color)
    {
        scene.add(new SceneObject(name, new Sphere(center, 0.01f), new Material(color, 1, 0), true));
        return this;
    }

    public Scene getScene()
    {
        return scene;
    }
}
