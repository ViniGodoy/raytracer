/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.scene;

import br.com.vinigodoy.raytrace.math.Ray;
import br.com.vinigodoy.raytrace.math.RayResult;
import br.com.vinigodoy.raytrace.math.Sphere;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scene implements Iterable<SceneObject> {
    private float INFINITE = 1000000.0f;
    private List<SceneObject> objects = new ArrayList<>();

    public void add(SceneObject obj) {
        objects.add(obj);
    }

    public void remove(SceneObject obj) {
        objects.remove(obj);
    }

    public void draw(Graphics2D canvas, int width, int height) {
        new Raytracer(this, canvas, width, height).render();
    }

    public void draw(BufferedImage img) {
        Graphics2D g2d = img.createGraphics();
        draw(g2d, img.getWidth(), img.getHeight());
        g2d.dispose();
    }

    @Override
    public Iterator<SceneObject> iterator() {
        return objects.iterator();
    }

    /**
     * Finds the nearest ray intersection.
     *
     * @param ray A ray
     * @return The nearest intersection
     */
    public NearestObject findNearest(Ray ray) {
        return findNearest(ray, INFINITE);
    }

    /**
     * Finds the nearest ray intersection.
     *
     * @param ray         A ray
     * @param maxDistance The maximum traveling distance.
     * @return The nearest intersection
     */
    public NearestObject findNearest(Ray ray, float maxDistance) {
        //Find the nearest intersection
        NearestObject nearest = null;

        for (SceneObject obj : objects) {
            RayResult res = obj.getShape().intersects(ray, maxDistance);
            if (res.isMissed())
                continue;

            nearest = new NearestObject(obj, res);
            maxDistance = res.getDistance();
        }

        return nearest;
    }

    public List<SceneObject> getLights() {
        List<SceneObject> lights = new ArrayList<>();
        for (SceneObject object : objects) {
            if (!object.isLight() || !(object.getShape() instanceof Sphere) || object.getMaterial().getDiffuse() <= 0)
                continue;
            lights.add(object);
        }
        return lights;
    }


}
