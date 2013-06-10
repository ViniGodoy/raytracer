/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G.Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.scene;

import br.com.vinigodoy.raytrace.math.Ray;
import br.com.vinigodoy.raytrace.math.Sphere;
import br.com.vinigodoy.raytrace.math.Vector3;

import java.awt.*;

import static br.com.vinigodoy.raytrace.math.Vector3.*;


public class Raytracer {
    private static final int MAX_DEPTH = 6;

    private final int width;
    private final int height;
    private float wx1, wy1;
    private float wx2, wy2;
    private float dx, dy;
    private Scene scene;
    private Graphics2D target;

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

    private void draw(int x, int y, Vector3 color) {
        target.setColor(color.saturate().toColor());
        target.drawLine(x, y, x, y);
    }

    public Vector3 raytrace(Ray ray) {
        return raytrace(ray, 0);
    }

    private Vector3 raytrace(Ray ray, int depth) {
        //Find the nearest intersection
        NearestObject nearest = scene.findNearest(ray);

        //no hit?
        if (nearest == null) return new Vector3();

        //Hit directly into the light source?
        if (nearest.get().isLight()) return nearest.get().getMaterial().getColor();

        // determine color at point of intersection
        Vector3 intersectionPoint = ray.getPointAt(nearest.getResult().getDistance());
        Vector3 normal = nearest.get().getShape().getNormal(intersectionPoint);
        Vector3 color = new Vector3(0, 0, 0);
        //Trace lights
        for (SceneObject light : scene.getLights()) {
            Sphere lamp = (Sphere) (light.getShape());
            // handle point light source
            boolean shade = true;
            Vector3 l = subtract(lamp.getCenter(), intersectionPoint);
            float lSize = l.size();
            l.multiply(1.0f / lSize); //normalization

            //See if it's in shadow
            Ray shadowRay = new Ray(deviate(intersectionPoint, l), l);
            for (SceneObject o : scene) {
                if ((o != light) && ((o.getShape().intersects(shadowRay, lSize)).isHit())) {
                    shade = false;
                    break;
                }
            }
            //If it is, there's no point in calculating lighting.
            if (!shade) continue;

            //Calculate diffuse light
            float dot = normal.dot(l);
            if (dot > 0) {
                float diff = nearest.get().getMaterial().getDiffuse() * dot;
                color.add(mul(nearest.get().getMaterial().getColor(), light.getMaterial().getColor()).multiply(diff));
            }

            // determine specular component
            if (nearest.get().getMaterial().getSpecular() > 0) {
                // point light source: sample once for specular highlight
                Vector3 v = ray.getDirection();
                Vector3 r = subtract(l, multiply(normal, 2.0f * l.dot(normal)));
                dot = v.dot(r);
                if (dot > 0) {
                    float spec = (float) Math.pow(dot, 20) * nearest.get().getMaterial().getSpecular();
                    // add specular component to ray color
                    color.add(multiply(light.getMaterial().getColor(), spec));
                }
            }
        }

        //If reached the maximum depth, there's no need to cast secondary rays.
        if (depth >= MAX_DEPTH) return color;

        //Calculate reflection
        float refl = nearest.get().getMaterial().getReflection();
        if (refl > 0.0f) {
            Vector3 r = reflect(ray.getDirection(), normal);
            Vector3 rcol = raytrace(new Ray(deviate(intersectionPoint, r), r), depth + 1);
            color.add(mul(nearest.get().getMaterial().getColor(), rcol).multiply(refl));
        }
        return color;
    }

    public void render() {
        // render scene
        Vector3 o = new Vector3(0, 0, -5);

        float sy = wy1;
        for (int y = 0; y < height; y++) {
            float sx = wx1;
            for (int x = 0; x < width; x++) {
                //Fire the primary ray
                Vector3 dir = new Vector3(sx, sy, 0).subtract(o).normalize();
                draw(x, height - y, raytrace(new Ray(o, dir)));
                sx += dx;
            }
            sy += dy;
        }
    }

}


