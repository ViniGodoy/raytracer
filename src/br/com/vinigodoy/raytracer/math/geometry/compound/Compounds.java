/*
===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.math.geometry.compound;

import br.com.vinigodoy.raytracer.material.Material;
import br.com.vinigodoy.raytracer.math.BBox;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.part.ConvexPartSphere;
import br.com.vinigodoy.raytracer.math.geometry.primitive.*;

public class Compounds {
    public static Compound newCylinder(float bevelRadius, Material mtrl) {
        return newCylinder(-0.5f, 0.5f, 1.0f, bevelRadius, mtrl);
    }

    public static Compound newCylinder(float bottom, float top, float radius, float bevelRadius, Material mtrl) {
        var c = new Compound();
        var rad = radius - bevelRadius;
        c.add(new Disk(new Vector3(0, bottom, 0), new Vector3(0, -1, 0), rad, mtrl));
        c.add(new OpenCylinder(bottom + bevelRadius, top - bevelRadius, radius, mtrl));
        c.add(new Disk(new Vector3(0, top, 0), new Vector3(0, 1, 0), rad, mtrl));

        if (bevelRadius > 0) {
            var torus = new Torus(radius - bevelRadius, bevelRadius, mtrl);
            c.addInstance(torus).translate(0, bottom + bevelRadius, 0);
            c.addInstance(torus).translate(0, top - bevelRadius, 0);
        }

        c.setBounds(new BBox(-radius, radius, bottom, top, -radius, radius));
        return c;
    }

    public static Compound newRoundBox(boolean faces, float bevelRadius, Material mtrl) {
        return newRoundBox(faces, new Vector3(-0.5f, -0.5f, -0.5f), new Vector3(0.5f, 0.5f, 0.5f), bevelRadius, mtrl);
    }

    public static Compound newRoundBox(boolean faces, Vector3 minCorner, Vector3 maxCorner, float bevelRadius, Material mtrl) {

        // edges
        // since the cylinders have to be defined initially in the vertical direction, it's easiest to use -(...)/2, +(...)/2 for
        // y0 and y1 in the constructors, and then rotate them about their centers.
        var c = new Compound();

        if (bevelRadius <= 0) {
            //If the user does not want bevels, returns just a common box.
            c.add(new Box(minCorner.getX(), minCorner.getY(), minCorner.getZ(), maxCorner.getX(), maxCorner.getY(), maxCorner.getZ(), mtrl));
            return c;
        }

        var topFrontBack = new OpenCylinder(
                -(maxCorner.getX() - minCorner.getX() - 2.0f * bevelRadius) / 2.0f,
                (maxCorner.getX() - minCorner.getX() - 2.0f * bevelRadius) / 2,
                bevelRadius, mtrl);

        // top front  (+ve y)
        c.addInstance(topFrontBack)
                .rotateZ((float) Math.toRadians(90.0f))
                .translate((minCorner.getX() + maxCorner.getX()) / 2, maxCorner.getY() - bevelRadius, maxCorner.getZ() - bevelRadius);

        // top back (-ve z)
        c.addInstance(topFrontBack)
                .rotateZ((float) Math.toRadians(90.0f))
                .translate((minCorner.getX() + maxCorner.getX()) / 2.0f, maxCorner.getY() - bevelRadius, minCorner.getZ() + bevelRadius);


        var topLeftRight = new OpenCylinder(
                -(maxCorner.getZ() - minCorner.getZ() - 2.0f * bevelRadius) / 2.0f,
                (maxCorner.getZ() - minCorner.getZ() - 2.0f * bevelRadius) / 2.0f,
                bevelRadius, mtrl);

        // top right (+ve x)
        c.addInstance(topLeftRight)
                .rotateX((float) Math.toRadians(90.0f))
                .translate(maxCorner.getX() - bevelRadius, maxCorner.getY() - bevelRadius, (minCorner.getZ() + maxCorner.getZ()) / 2.0f);

        // top left (-ve x)
        c.addInstance(topLeftRight)
                .rotateX((float) Math.toRadians(90.0f))
                .translate(minCorner.getX() + bevelRadius, maxCorner.getY() - bevelRadius, (minCorner.getZ() + maxCorner.getZ()) / 2.0f);


        var bottomFrontBack = new OpenCylinder(
                -(maxCorner.getX() - minCorner.getX() - 2.0f * bevelRadius) / 2.0f,
                (maxCorner.getX() - minCorner.getX() - 2.0f * bevelRadius) / 2.0f,
                bevelRadius, mtrl);

        c.addInstance(bottomFrontBack)
                .rotateZ((float) Math.toRadians(90))
                .translate((minCorner.getX() + maxCorner.getX()) / 2.0f, minCorner.getY() + bevelRadius, maxCorner.getZ() - bevelRadius);

        c.addInstance(bottomFrontBack)
                .rotateZ((float) Math.toRadians(90))
                .translate((minCorner.getX() + maxCorner.getX()) / 2, minCorner.getY() + bevelRadius, minCorner.getZ() + bevelRadius);

        // bottom right (-ve x, -ve y)
        var bottomLeftRight = new OpenCylinder(
                -(maxCorner.getZ() - minCorner.getZ() - 2.0f * bevelRadius) / 2.0f,
                (maxCorner.getZ() - minCorner.getZ() - 2.0f * bevelRadius) / 2.0f,
                bevelRadius, mtrl);

        c.addInstance(bottomLeftRight)
                .rotateX((float) Math.toRadians(90))
                .translate(maxCorner.getX() - bevelRadius, minCorner.getY() + bevelRadius, (minCorner.getZ() + maxCorner.getZ()) / 2.0f);

        // bottom left (-ve x, -ve y)
        c.addInstance(bottomLeftRight)
                .rotateX((float) Math.toRadians(90))
                .translate(minCorner.getX() + bevelRadius, minCorner.getY() + bevelRadius, (minCorner.getZ() + maxCorner.getZ()) / 2.0f);

        // vertical right front  (+ve x, +ve z)
        var verticalRightFront = new OpenCylinder(minCorner.getY() + bevelRadius, maxCorner.getY() - bevelRadius, bevelRadius, mtrl);
        c.addInstance(verticalRightFront)
                .translate(maxCorner.getX() - bevelRadius, 0, maxCorner.getZ() - bevelRadius);

        // vertical left front  (-ve x, +ve z)
        c.addInstance(verticalRightFront)
                .translate(minCorner.getX() + bevelRadius, 0, maxCorner.getZ() - bevelRadius);

        // vertical left back  (-ve x, -ve z)
        var verticalLeftBack = new OpenCylinder(minCorner.getY() + bevelRadius, maxCorner.getY() - bevelRadius, bevelRadius, mtrl);
        c.addInstance(verticalLeftBack)
                .translate(minCorner.getX() + bevelRadius, 0, minCorner.getZ() + bevelRadius);

        // vertical right back  (+ve x, -ve z)
        c.addInstance(verticalLeftBack)
                .translate(maxCorner.getX() - bevelRadius, 0, minCorner.getZ() + bevelRadius);
        // corner spheres

        // top right front
        c.addInstance(new Sphere(new Vector3(maxCorner.getX() - bevelRadius, maxCorner.getY() - bevelRadius, maxCorner.getZ() - bevelRadius), bevelRadius, mtrl));

        // top left front  (-ve x)
        c.addInstance(new Sphere(new Vector3(minCorner.getX() + bevelRadius, maxCorner.getY() - bevelRadius, maxCorner.getZ() - bevelRadius), bevelRadius, mtrl));

        // top left back
        c.addInstance(new Sphere(new Vector3(minCorner.getX() + bevelRadius, maxCorner.getY() - bevelRadius, minCorner.getZ() + bevelRadius), bevelRadius, mtrl));

        // top right back
        c.addInstance(new Sphere(new Vector3(maxCorner.getX() - bevelRadius, maxCorner.getY() - bevelRadius, minCorner.getZ() + bevelRadius), bevelRadius, mtrl));

        // bottom right front
        c.addInstance(new Sphere(new Vector3(maxCorner.getX() - bevelRadius, minCorner.getY() + bevelRadius, maxCorner.getZ() - bevelRadius), bevelRadius, mtrl));

        // bottom left front
        c.addInstance(new Sphere(new Vector3(minCorner.getX() + bevelRadius, minCorner.getY() + bevelRadius, maxCorner.getZ() - bevelRadius), bevelRadius, mtrl));

        // bottom left back
        c.addInstance(new Sphere(new Vector3(minCorner.getX() + bevelRadius, minCorner.getY() + bevelRadius, minCorner.getZ() + bevelRadius), bevelRadius, mtrl));

        // bottom right back
        c.addInstance(new Sphere(new Vector3(maxCorner.getX() - bevelRadius, minCorner.getY() + bevelRadius, minCorner.getZ() + bevelRadius), bevelRadius, mtrl));

        if (!faces) return c;

        // the faces

        // bottom face: -ve y
        c.add(new Rectangle(new Vector3(minCorner.getX() + bevelRadius, minCorner.getY(), minCorner.getY() + bevelRadius),
                new Vector3(0, 0, (maxCorner.getZ() - bevelRadius) - (minCorner.getZ() + bevelRadius)),
                new Vector3((maxCorner.getX() - bevelRadius) - (minCorner.getX() + bevelRadius), 0, 0),
                new Vector3(0, -1, 0), mtrl));

        // bottom face: +ve y
        c.add(new Rectangle(new Vector3(minCorner.getX() + bevelRadius, maxCorner.getY(), minCorner.getZ() + bevelRadius),
                new Vector3(0, 0, (maxCorner.getZ() - bevelRadius) - (minCorner.getZ() + bevelRadius)),
                new Vector3((maxCorner.getX() - bevelRadius) - (minCorner.getX() + bevelRadius), 0, 0),
                new Vector3(0, 1, 0), mtrl));

        // back face: -ve z
        c.add(new Rectangle(new Vector3(minCorner.getX() + bevelRadius, minCorner.getY() + bevelRadius, minCorner.getZ()),
                new Vector3((maxCorner.getX() - bevelRadius) - (minCorner.getX() + bevelRadius), 0, 0),
                new Vector3(0, (maxCorner.getY() - bevelRadius) - (minCorner.getY() + bevelRadius), 0),
                new Vector3(0, 0, -1), mtrl));

        // front face
        c.add(new Rectangle(new Vector3(minCorner.getX() + bevelRadius, minCorner.getY() + bevelRadius, maxCorner.getZ()),
                new Vector3((maxCorner.getX() - bevelRadius) - (minCorner.getX() + bevelRadius), 0, 0),
                new Vector3(0, (maxCorner.getY() - bevelRadius) - (minCorner.getY() + bevelRadius), 0),
                new Vector3(0, 0, 1), mtrl));

        // left face: -ve x
        c.add(new Rectangle(new Vector3(minCorner.getX(), minCorner.getY() + bevelRadius, minCorner.getZ() + bevelRadius),
                new Vector3(0, 0, (maxCorner.getZ() - bevelRadius) - (minCorner.getZ() + bevelRadius)),
                new Vector3(0, (maxCorner.getY() - bevelRadius) - (minCorner.getY() + bevelRadius), 0),
                new Vector3(-1, 0, 0), mtrl));

        // right face: +ve x
        c.add(new Rectangle(new Vector3(maxCorner.getX(), minCorner.getY() + bevelRadius, minCorner.getZ() + bevelRadius),
                new Vector3(0, 0, (maxCorner.getZ() - bevelRadius) - (minCorner.getZ() + bevelRadius)),
                new Vector3(0, (maxCorner.getY() - bevelRadius) - (minCorner.getY() + bevelRadius), 0),
                new Vector3(1, 0, 0), mtrl));

        c.setBounds(new BBox(minCorner, maxCorner));
        return c;
    }

    public static Compound newBowl(boolean round, float innerRadius, float outerRadius, float openAngle, Material mtrl) {
        var c = new Compound();

        var PI = (float) Math.PI;
        var TWOPI = (float) (2 * Math.PI);

        c.add(new ConvexPartSphere(new Vector3(0, 0, 0), innerRadius, 0, TWOPI, openAngle, PI, mtrl));
        c.add(new ConvexPartSphere(new Vector3(0, 0, 0), outerRadius, 0, TWOPI, openAngle, PI, mtrl));

        var avgRadius = (innerRadius + outerRadius) / 2.0f;

        if (round) {
            var a = avgRadius * (float) Math.sin(openAngle);
            var b = (outerRadius - innerRadius) / 2.0f;
            c.addInstance(new Torus(a, b, mtrl))
                    .translate(0, avgRadius * (float) Math.cos(openAngle), 0);
        } else {
            var inner = (float) (innerRadius * Math.sin(openAngle));
            var outer = (float) (outerRadius * Math.sin(openAngle));
            c.add(new Annulus(new Vector3(0, avgRadius * (float) Math.cos(openAngle), 0), new Vector3(0, 1, 0),
                    inner, outer, mtrl));
        }

        c.setBounds(new BBox(-outerRadius, outerRadius, -outerRadius, outerRadius, -outerRadius, outerRadius));
        return c;
    }

}
