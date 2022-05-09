/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.gui;

import br.com.vinigodoy.raytracer.camera.PinholeCamera;
import br.com.vinigodoy.raytracer.camera.ThinLensCamera;
import br.com.vinigodoy.raytracer.light.AmbientOccludedLight;
import br.com.vinigodoy.raytracer.light.AreaLight;
import br.com.vinigodoy.raytracer.light.PointLight;
import br.com.vinigodoy.raytracer.material.Emissive;
import br.com.vinigodoy.raytracer.material.Matte;
import br.com.vinigodoy.raytracer.material.Phong;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.compound.Compounds;
import br.com.vinigodoy.raytracer.math.geometry.primitive.*;
import br.com.vinigodoy.raytracer.sampler.Sampler;
import br.com.vinigodoy.raytracer.scene.World;
import br.com.vinigodoy.raytracer.scene.WorldListener;
import br.com.vinigodoy.raytracer.tracer.AreaLightTracer;
import br.com.vinigodoy.raytracer.tracer.Raycasting;

public enum WorldMaker {
    BALLS {
        @Override
        public World createScene(int numSamples, float zoom, WorldListener listener) {
            var camera = new PinholeCamera(
                    new Vector3(0, 0, 800),
                    new Vector3(0, 0, 0),
                    new Vector3(0, 1, 0),
                    2000);

            camera.setZoom(zoom);

            var world = new World(toString(), new Raycasting(), new Vector3(), camera).addListener(listener);

            // colors
            var lightGreen = new Vector3(0.65f, 1.0f, 0.30f);
            var green = new Vector3(0.0f, 0.6f, 0.3f);
            var darkGreen = new Vector3(0.0f, 0.41f, 0.41f);

            var yellow = new Vector3(1.0f, 1.0f, 0.0f);
            var darkYellow = new Vector3(0.61f, 0.61f, 0.0f);

            var lightPurple = new Vector3(0.65f, 0.3f, 1.0f);
            var darkPurple = new Vector3(0.5f, 0.0f, 1.0f);

            var brown = new Vector3(0.71f, 0.40f, 0.16f);
            var orange = new Vector3(1.0f, 0.75f, 0.0f);

            //Lights
            world.add(new PointLight(3.0f, new Vector3(1.0f, 1.0f, 1.0f), new Vector3(100.0f, 100.0f, 200.0f)));
                // spheres
            return world.addAll(
                new Plane(new Vector3(0, -85, 0), new Vector3(0, 1, 0), new Matte(0.2f, 0.5f, new Vector3(1, 1, 1))),
                new Sphere(new Vector3(5, 3, 0), 30, yellow),
                new Sphere(new Vector3(45, -7, -60), 20, brown),
                new Sphere(new Vector3(40, 43, -100), 17, darkGreen),
                new Sphere(new Vector3(-20, 28, -15), 20, orange),
                new Sphere(new Vector3(-25, -7, -35), 27, green),

                new Sphere(new Vector3(20, -27, -35), 25, lightGreen),
                new Sphere(new Vector3(35, 18, -35), 22, green),
                new Sphere(new Vector3(-57, -17, -50), 15, brown),
                new Sphere(new Vector3(-47, 16, -80), 23, lightGreen),
                new Sphere(new Vector3(-15, -32, -60), 22, darkGreen),

                new Sphere(new Vector3(-35, -37, -80), 22, darkYellow),
                new Sphere(new Vector3(10, 43, -80), 22, darkYellow),
                new Sphere(new Vector3(30, -7, -80), 10, darkYellow), //(hidden)
                new Sphere(new Vector3(-40, 48, -110), 18, darkGreen),
                new Sphere(new Vector3(-10, 53, -120), 18, brown),

                new Sphere(new Vector3(-55, -52, -100), 10, lightPurple),
                new Sphere(new Vector3(5, -52, -100), 15, brown),
                new Sphere(new Vector3(-20, -57, -120), 15, darkPurple),
                new Sphere(new Vector3(55, -27, -100), 17, darkGreen),
                new Sphere(new Vector3(50, -47, -120), 15, brown),

                new Sphere(new Vector3(70, -42, -150), 10, lightPurple),
                new Sphere(new Vector3(5, 73, -130), 12, lightPurple),
                new Sphere(new Vector3(66, 21, -130), 13, darkPurple),
                new Sphere(new Vector3(72, -12, -140), 12, lightPurple),
                new Sphere(new Vector3(64, 5, -160), 11, green),

                new Sphere(new Vector3(55, 38, -160), 12, lightPurple),
                new Sphere(new Vector3(-73, -2, -160), 12, lightPurple),
                new Sphere(new Vector3(30, -62, -140), 15, darkPurple),
                new Sphere(new Vector3(25, 63, -140), 15, darkPurple),
                new Sphere(new Vector3(-60, 46, -140), 15, darkPurple),

                new Sphere(new Vector3(-30, 68, -130), 12, lightPurple),
                new Sphere(new Vector3(58, 56, -180), 11, green),
                new Sphere(new Vector3(-63, -39, -180), 11, green),
                new Sphere(new Vector3(46, 68, -200), 10, lightPurple),
                new Sphere(new Vector3(-3, -72, -130), 12, lightPurple)
            );
        }
    },
    BILLIARD {
        private static final float BALL_CM = 5.715f / 2.0f;

        private Sphere createCarom(float x, float z) {
            var caromCm = 6.15f / 2.0f;
            return new Sphere(new Vector3(x, caromCm, z), caromCm,
                    new Phong(0.2f, 0.65f, 0.4f, 64.00f, new Vector3(1, 1, 1)));
        }

        private Sphere createBall(float x, float z, Vector3 color) {
            var material = new Phong(0.2f, 0.65f, 0.4f, 64.00f, color);
            material.setCs(new Vector3(1, 1, 1));

            return new Sphere(new Vector3(x, BALL_CM, z), BALL_CM,
                    material);
        }

        private void createLamp(World world, float w, float y, float z, int numSamples) {
            var hw = w / 2.0f;

            var shape = new Rectangle(
                new Vector3(-hw, y, -hw + z),
                new Vector3(w, 0, 0),
                new Vector3(0, 0, w),
                new Vector3(0, -1, 0),
                new Emissive(40000, new Vector3(1, 1, 1)), Sampler.newDefault(numSamples)
            );

            world.add(new AreaLight(shape)).add(shape);
        }

        @Override
        public World createScene(int numSamples, float zoom, WorldListener listener) {
            var camera = new ThinLensCamera(
                    new Vector3(10, 10, 190),
                    new Vector3(0, 0, 0),
                    new Vector3(0, 1, 0),
                    2000, 190 + 86, 0.25f, Sampler.newDefault(numSamples));

            /*
            Camera over the table
            var camera = new ThinLensCamera(
                    new Vector3(0, 760, 0),
                    new Vector3(0, 0, 0),
                    new Vector3(0, 1, 0),
                    2000, 700, 0.6f, Sampler.newDefault(numSamples));*/


            camera.setZoom(zoom);

            var world = new World(toString(), new AreaLightTracer(), new Vector3(0f, 0f, 0f), camera)
                .addListener(listener);

            // colors
            var one = new Vector3(1.0f, 1.0f, 0.0f);            //Yellow
            var two = new Vector3(0.0f, 0.0f, 1.0f);            //Blue
            var three = new Vector3(1.0f, 0.0f, 0.0f);          //Red
            var four = new Vector3(0.29f, 0.0f, 0.5f);          //Purple
            var five = new Vector3(1.0f, 0.5f, 0.0f);           //Orange
            var six = new Vector3(0.0f, 0.41f, 0.41f);          //Green
            var seven = new Vector3(0.545f, 0.27f, 0.074f);     //Brown
            var eight = new Vector3(0.1f, 0.1f, 0.1f);            //Black

            var nine = new Vector3(1.0f, 1.0f, 0.0f);           //Yellow and White
            var ten = new Vector3(0.0f, 0.0f, 1.0f);            //Blue and white
            var eleven = new Vector3(1.0f, 0.0f, 0.0f);         //Red and white
            var twelve = new Vector3(0.29f, 0.0f, 0.5f);        //Purple and white
            var thirteen = new Vector3(1.0f, 0.5f, 0.0f);       //Orange and white
            var fourteen = new Vector3(0.0f, 0.41f, 0.41f);     //Green and white
            var fifteen = new Vector3(0.545f, 0.27f, 0.074f);   //Brown and white

            //Vector3 table = new Vector3(0.188f, 0.5f, 0.14f);       //Green
            var table = Vector3.fromRGB(10, 108, 3);

            //Lights
            createLamp(world, 20, 100, -60, numSamples);
            createLamp(world, 20, 100, 60, numSamples);
            // Table
            return world
                .setAmbientLight(new AmbientOccludedLight(1.5f, new Vector3(1.0f, 1.0f, 1.0f),
            0.4f, Sampler.newDefault(numSamples))
                ).add(
                    new Rectangle(
                        new Vector3(-71, 0, -142), new Vector3(142, 0, 0),
                        new Vector3(0, 0, 284), new Vector3(0, 1, 0),
                        new Matte(0.2f, 0.5f, table)
                    )
                ).addAll(// Balls
                    createCarom(0, 122),
                    createBall(0, -86, one),

                    createBall(-3, -92.0f, two),
                    createBall(3, -92.0f, three),

                    createBall(-6, -98.0f, four),
                    createBall(0, -98.0f, five),
                    createBall(6, -98.0f, six),

                    createBall(-9, -104.0f, seven),
                    createBall(-3, -104.0f, eight),
                    createBall(3, -104.0f, nine),
                    createBall(9, -104.0f, ten),

                    createBall(-12, -110.0f, eleven),
                    createBall(-6, -110.0f, twelve),
                    createBall(0, -110.0f, thirteen),
                    createBall(6, -110.0f, fourteen),
                    createBall(12, -110.0f, fifteen)
                );
        }
    },
    OBJECTS {
        private void createLamp(World world, float w, float y, float z, int numSamples) {
            var hw = w / 2.0f;

            var shape = new Rectangle(
                    new Vector3(-hw, y, -hw + z),
                    new Vector3(w, 0, 0),
                    new Vector3(0, 0, w),
                    new Vector3(0, -1, 0),
                    new Emissive(40000, new Vector3(1, 1, 1)), Sampler.newDefault(numSamples));

            world.add(new AreaLight(shape)).add(shape);
        }

        @Override
        public World createScene(int numSamples, float zoom, WorldListener listener) {
            var camera = new ThinLensCamera(
                new Vector3(-50, 80, 210),
                new Vector3(0, 0, 0),
                new Vector3(0, 1, 0),
                400, 110, 0.25f, Sampler.newDefault(numSamples)
            );

            camera.setZoom(zoom);

            var world = new World(toString(), new Raycasting(), new Vector3(), camera).addListener(listener);

            //Colors
            world.getBackgroundColor().set(0.7f, 0.7f, 0.7f);
            var blue = new Phong(0.2f, 0.65f, 0.4f, 64.0f, new Vector3(0.0f, 0.0f, 1.0f));
            var yellow = new Phong(0.2f, 0.65f, 0.4f, 64.0f, new Vector3(1.0f, 1.0f, 0.0f));
            var black = new Phong(0.2f, 0.65f, 0.4f, 64.0f, new Vector3(0.1f, 0.1f, 0.1f));
            var green = new Phong(0.2f, 0.65f, 0.4f, 64.0f, new Vector3(0.0f, 1.0f, 0.0f));
            var red = new Phong(0.2f, 0.65f, 0.4f, 64.0f, new Vector3(1.0f, 0.0f, 0.0f));

            //Lights
            createLamp(world, 30, 100, -70, numSamples);
            createLamp(world, 30, 100, 70, numSamples);

            world.setAmbientLight(
                new AmbientOccludedLight(1.0f, new Vector3(1.0f, 1.0f, 1.0f), 0.4f,
                Sampler.newDefault(numSamples))
            );

            //Objects
            var torus = new Torus(15, 3f, blue);
            var angle1 = (float) Math.toRadians(90);
            var angle2 = (float) Math.toRadians(80);

            world.addInstance(torus).rotateX(angle1).translate(-40, 60, 0);
            world.addInstance(torus, yellow).rotateX(angle2).translate(-20, 50, 0);
            world.addInstance(torus, black).rotateX(angle1).translate(-0, 60, 0);
            world.addInstance(torus, green).rotateX(angle2).translate(20, 50, 0);
            world.addInstance(torus, red).rotateX(angle1).translate(40, 60, 0);

            world.addInstance(Compounds.newRoundBox(true, 0.05f, new Phong(0.2f, 0.65f, 0.4f, 64.0f, new Vector3(0.0f, 0.5f, 1.0f))))
                    .scale(60)
                    .translate(180, -50, 30);

            return world.addAll(
                new Disk(new Vector3(0, -20, 0), new Vector3(0, 1, 0), 60, new Matte(0.2f, 0.65f, new Vector3(1.0f, 0.3f, 0.3f))),
                new OpenCylinder(-20, 10, 60, new Matte(0.2f, 0.65f, new Vector3(1.0f, 0.3f, 0.3f))),
                Compounds.newBowl(true, 75, 80, (float) Math.toRadians(90),
                    new Phong(0.2f, 0.65f, 0.4f, 64.00f, new Vector3(1.0f, 0.6f, 0.2f))),
                new Plane(new Vector3(0, -80, 0), new Vector3(0, 1, 0), new Matte(0.2f, 0.5f, new Vector3(1, 1, 1)))
            );
        }
    };

    public abstract World createScene(int numSamples, float zoom, WorldListener listener);

    @Override
    public String toString() {
        var name = super.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
