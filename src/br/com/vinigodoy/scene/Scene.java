package br.com.vinigodoy.scene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class Scene implements Iterable<SceneObject> {
    private List<SceneObject> objects = new ArrayList<>();

    public void add(SceneObject obj)
    {
        objects.add(obj);
    }

    public void remove(SceneObject obj)
    {
        objects.remove(obj);
    }

    public void draw(Graphics2D canvas, int width, int height)
    {
        new Raytracer(this, canvas, width, height).render();
    }

    public void draw(BufferedImage img)
    {
        Graphics2D g2d = img.createGraphics();
        draw(g2d, img.getWidth(), img.getHeight());
        g2d.dispose();
    }

    @Override
    public Iterator<SceneObject> iterator() {
        return objects.iterator();
    }
}
