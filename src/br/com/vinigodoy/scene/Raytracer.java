package br.com.vinigodoy.scene;

import br.com.vinigodoy.math.Ray;
import br.com.vinigodoy.math.RayResult;
import br.com.vinigodoy.math.Sphere;
import br.com.vinigodoy.math.Vector3;

import java.awt.*;

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
public class Raytracer {
    private static final int MAX_DEPTH = 6;
    private static final float EPSILON = 0.0001f;

    private final int width;
    private final int height;

    private float wx1, wy1;
    private float wx2, wy2;
    private float dx, dy;

    private Scene scene;
    private Graphics2D target;

    private void draw(int x, int y, Vector3 color) {
        target.setColor(color.saturate().toColor());
        target.drawLine(x, y, x, y);
    }

    public Raytracer(Scene scene, Graphics2D target, int width, int height) {
        this.width = width;
        this.height = height;
        this.scene = scene;
        this.target = target;


        //Defines the screen plane
        wx1 = -(width / 200.0f);
        wx2 = -wx1;
        wy1 = -(height / 200.0f);
        wy2 = -wy1;

        // calculate deltas for interpolation
        dx = (wx2 - wx1) / width;
        dy = (wy2 - wy1) / height;
    }

    public Vector3 raytrace(Ray ray, int depth)
    {
        if (depth > MAX_DEPTH)
            return new Vector3(0,0,0);
        //Find the nearest intersection
        float dist = 1000000.0f;
        SceneObject nearest = null;

        for (SceneObject obj : scene) {
            RayResult res = obj.getShape().intersects(ray, dist);
            if (res.isMissed())
                continue;

            if (nearest == null || res.getDist() < dist) {
                nearest = obj;
                dist = res.getDist();
            }
        }

        //no hit?
        if (nearest == null) return new Vector3();

        //Hit directly into the light source?
        if (nearest.isLight()) return nearest.getMaterial().getColor();

        // determine color at point of intersection
        Vector3 pi = Vector3.multiply(ray.getDirection(), dist).add(ray.getOrigin());
        Vector3 n = nearest.getShape().getNormal(pi);
        Vector3 color = new Vector3(0,0,0);
        //Trace lights
        for (SceneObject light : scene) {
            if (!light.isLight() || !(light.getShape() instanceof Sphere) || light.getMaterial().getDiffuse() <= 0)
                continue;

            Sphere lamp = (Sphere)(light.getShape());
            // handle point light source
            boolean shade = true;
            Vector3 l = Vector3.subtract(lamp.getCenter(), pi);
            float lSize = l.size();
            l.multiply(1.0f / lSize); //normalization

            //See if it's in shadow
            Ray shadowRay = new Ray( Vector3.add(pi, Vector3.multiply(l, EPSILON)), l);
            for (SceneObject o : scene) {
                if ((o != light) && ((o.getShape().intersects(shadowRay, lSize)).isHit()))
                {
                    shade = false;
                    break;
                }
            }
            //If it is, there's no point in calculating lighting.
            if (!shade) continue;

            //Calculate diffuse light
            float dot = n.dot(l);
            if (dot > 0)
            {
                float diff = nearest.getMaterial().getDiffuse() * dot;
                color.add(Vector3.mul(nearest.getMaterial().getColor(), light.getMaterial().getColor()).multiply(diff));
            }

            // determine specular component
            if (nearest.getMaterial().getSpecular() > 0)
            {
                // point light source: sample once for specular highlight
                Vector3 v = ray.getDirection();
                Vector3 r = Vector3.subtract(l, Vector3.multiply(n, 2.0f * l.dot(n)));
                dot = v.dot(r);
                if (dot > 0)
                {
                    float spec = (float)Math.pow(dot, 20) * nearest.getMaterial().getSpecular();
                    // add specular component to ray color
                    color.add(Vector3.multiply(light.getMaterial().getColor(), spec));
                }
            }


        }

        //Calculate reflection
        float refl = nearest.getMaterial().getReflection();
        if (refl > 0.0f)
        {
            Vector3 r = Vector3.subtract(
                    ray.getDirection(),
                    Vector3.multiply(n, 2.0f * ray.getDirection().dot(n)));
            if (depth < MAX_DEPTH)
            {
                Vector3 rcol = raytrace(new Ray(Vector3.add(pi, r.multiply(EPSILON)), r), depth+1);
                color.add(Vector3.mul(nearest.getMaterial().getColor(), rcol).multiply(refl));
            }
        }
        return color;
    }

    public void render()
    {
        // render scene
        Vector3 o = new Vector3(0, 0, -5);

        float sy = wy1;
        for (int y = 0; y < height; y++)
        {
            float sx = wx1;
            for (int x = 0; x < width; x++) {
                //Fire the primary ray
                Vector3 dir = new Vector3(sx, sy, 0).subtract(o).normalize();
                draw(x,height-y,raytrace(new Ray(o, dir), 0));
                sx += dx;
            }
            sy += dy;
        }
    }

}


