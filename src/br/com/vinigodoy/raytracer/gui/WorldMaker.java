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
            PinholeCamera camera = new PinholeCamera(
                    new Vector3(0, 0, 800),
                    new Vector3(0, 0, 0),
                    new Vector3(0, 1, 0),
                    2000);

            camera.setZoom(zoom);

            World world = new World(toString(), new Raycasting(), new Vector3(), camera);
            world.setAmbientLight(new AmbientOccludedLight(1.5f, new Vector3(1.0f, 1.0f, 1.0f), 0.4f,
                    Sampler.newDefault(numSamples)));
            world.addListener(listener);

            //Lights
            world.add(new PointLight(3.0f, new Vector3(1.0f, 1.0f, 1.0f), new Vector3(100.0f, 100.0f, 200.0f)));

            // colors
            Vector3 lightGreen = new Vector3(0.65f, 1.0f, 0.30f);
            Vector3 green = new Vector3(0.0f, 0.6f, 0.3f);
            Vector3 darkGreen = new Vector3(0.0f, 0.41f, 0.41f);

            Vector3 yellow = new Vector3(1.0f, 1.0f, 0.0f);
            Vector3 darkYellow = new Vector3(0.61f, 0.61f, 0.0f);

            Vector3 lightPurple = new Vector3(0.65f, 0.3f, 1.0f);
            Vector3 darkPurple = new Vector3(0.5f, 0.0f, 1.0f);

            Vector3 brown = new Vector3(0.71f, 0.40f, 0.16f);
            Vector3 orange = new Vector3(1.0f, 0.75f, 0.0f);

            // spheres
            world.add(new Plane(new Vector3(0, -85, 0), new Vector3(0, 1, 0), new Matte(0.2f, 0.5f, new Vector3(1, 1, 1))));
            world.add(new Sphere(new Vector3(5, 3, 0), 30, yellow));
            world.add(new Sphere(new Vector3(45, -7, -60), 20, brown));
            world.add(new Sphere(new Vector3(40, 43, -100), 17, darkGreen));
            world.add(new Sphere(new Vector3(-20, 28, -15), 20, orange));
            world.add(new Sphere(new Vector3(-25, -7, -35), 27, green));

            world.add(new Sphere(new Vector3(20, -27, -35), 25, lightGreen));
            world.add(new Sphere(new Vector3(35, 18, -35), 22, green));
            world.add(new Sphere(new Vector3(-57, -17, -50), 15, brown));
            world.add(new Sphere(new Vector3(-47, 16, -80), 23, lightGreen));
            world.add(new Sphere(new Vector3(-15, -32, -60), 22, darkGreen));

            world.add(new Sphere(new Vector3(-35, -37, -80), 22, darkYellow));
            world.add(new Sphere(new Vector3(10, 43, -80), 22, darkYellow));
            world.add(new Sphere(new Vector3(30, -7, -80), 10, darkYellow)); //(hidden)
            world.add(new Sphere(new Vector3(-40, 48, -110), 18, darkGreen));
            world.add(new Sphere(new Vector3(-10, 53, -120), 18, brown));

            world.add(new Sphere(new Vector3(-55, -52, -100), 10, lightPurple));
            world.add(new Sphere(new Vector3(5, -52, -100), 15, brown));
            world.add(new Sphere(new Vector3(-20, -57, -120), 15, darkPurple));
            world.add(new Sphere(new Vector3(55, -27, -100), 17, darkGreen));
            world.add(new Sphere(new Vector3(50, -47, -120), 15, brown));

            world.add(new Sphere(new Vector3(70, -42, -150), 10, lightPurple));
            world.add(new Sphere(new Vector3(5, 73, -130), 12, lightPurple));
            world.add(new Sphere(new Vector3(66, 21, -130), 13, darkPurple));
            world.add(new Sphere(new Vector3(72, -12, -140), 12, lightPurple));
            world.add(new Sphere(new Vector3(64, 5, -160), 11, green));

            world.add(new Sphere(new Vector3(55, 38, -160), 12, lightPurple));
            world.add(new Sphere(new Vector3(-73, -2, -160), 12, lightPurple));
            world.add(new Sphere(new Vector3(30, -62, -140), 15, darkPurple));
            world.add(new Sphere(new Vector3(25, 63, -140), 15, darkPurple));
            world.add(new Sphere(new Vector3(-60, 46, -140), 15, darkPurple));

            world.add(new Sphere(new Vector3(-30, 68, -130), 12, lightPurple));
            world.add(new Sphere(new Vector3(58, 56, -180), 11, green));
            world.add(new Sphere(new Vector3(-63, -39, -180), 11, green));
            world.add(new Sphere(new Vector3(46, 68, -200), 10, lightPurple));
            world.add(new Sphere(new Vector3(-3, -72, -130), 12, lightPurple));

            return world;
        }
    },
    BILLIARD {
        private static final float BALL_CM = 5.715f / 2.0f;

        private Sphere createCarom(float x, float z) {
            float caromCm = 6.15f / 2.0f;
            return new Sphere(new Vector3(x, caromCm, z), caromCm,
                    new Phong(0.2f, 0.65f, 0.4f, 64.00f, new Vector3(1, 1, 1)));
        }

        private Sphere createBall(float x, float z, Vector3 color) {
            Phong material = new Phong(0.2f, 0.65f, 0.4f, 64.00f, color);
            material.setCs(new Vector3(1, 1, 1));

            return new Sphere(new Vector3(x, BALL_CM, z), BALL_CM,
                    material);
        }

        private void createLamp(World world, float w, float y, float z, int numSamples) {
            float hw = w / 2.0f;

            Rectangle shape = new Rectangle(
                    new Vector3(-hw, y, -hw + z),
                    new Vector3(w, 0, 0),
                    new Vector3(0, 0, w),
                    new Vector3(0, -1, 0),
                    new Emissive(40000, new Vector3(1, 1, 1)), Sampler.newDefault(numSamples));

            world.add(new AreaLight(shape));
            world.add(shape);
        }

        @Override
        public World createScene(int numSamples, float zoom, WorldListener listener) {
            ThinLensCamera camera = new ThinLensCamera(
                    new Vector3(10, 10, 190),
                    new Vector3(0, 0, 0),
                    new Vector3(0, 1, 0),
                    2000, 190 + 86, 0.25f, Sampler.newDefault(numSamples));

            /*
            Camera over the table
            ThinLensCamera camera = new ThinLensCamera(
                    new Vector3(0, 760, 0),
                    new Vector3(0, 0, 0),
                    new Vector3(0, 1, 0),
                    2000, 700, 0.6f, Sampler.newDefault(numSamples));*/


            camera.setZoom(zoom);
            World world = new World(toString(), new AreaLightTracer(), new Vector3(0f, 0f, 0f), camera);
            world.setAmbientLight(new AmbientOccludedLight(1.5f, new Vector3(1.0f, 1.0f, 1.0f), 0.4f,
                    Sampler.newDefault(numSamples)));

            world.addListener(listener);

            //Lights
            createLamp(world, 20, 100, -60, numSamples);
            createLamp(world, 20, 100, 60, numSamples);

            // colors
            Vector3 one = new Vector3(1.0f, 1.0f, 0.0f);            //Yellow
            Vector3 two = new Vector3(0.0f, 0.0f, 1.0f);            //Blue
            Vector3 three = new Vector3(1.0f, 0.0f, 0.0f);          //Red
            Vector3 four = new Vector3(0.29f, 0.0f, 0.5f);          //Purple
            Vector3 five = new Vector3(1.0f, 0.5f, 0.0f);           //Orange
            Vector3 six = new Vector3(0.0f, 0.41f, 0.41f);          //Green
            Vector3 seven = new Vector3(0.545f, 0.27f, 0.074f);     //Brown
            Vector3 eight = new Vector3(0.1f, 0.1f, 0.1f);            //Black

            Vector3 nine = new Vector3(1.0f, 1.0f, 0.0f);           //Yellow and White
            Vector3 ten = new Vector3(0.0f, 0.0f, 1.0f);            //Blue and white
            Vector3 eleven = new Vector3(1.0f, 0.0f, 0.0f);         //Red and white
            Vector3 twelve = new Vector3(0.29f, 0.0f, 0.5f);        //Purple and white
            Vector3 thirteen = new Vector3(1.0f, 0.5f, 0.0f);       //Orange and white
            Vector3 fourteen = new Vector3(0.0f, 0.41f, 0.41f);     //Green and white
            Vector3 fifteen = new Vector3(0.545f, 0.27f, 0.074f);   //Brown and white

            //Vector3 table = new Vector3(0.188f, 0.5f, 0.14f);       //Green
            Vector3 table = Vector3.fromRGB(10, 108, 3);

            // Table
            world.add(new Rectangle(new Vector3(-71, 0, -142), new Vector3(142, 0, 0), new Vector3(0, 0, 284), new Vector3(0, 1, 0),
                    new Matte(0.2f, 0.5f, table)));

            // Balls
            world.add(createCarom(0, 122));
            world.add(createBall(0, -86, one));

            world.add(createBall(-3, -92.0f, two));
            world.add(createBall(3, -92.0f, three));

            world.add(createBall(-6, -98.0f, four));
            world.add(createBall(0, -98.0f, five));
            world.add(createBall(6, -98.0f, six));

            world.add(createBall(-9, -104.0f, seven));
            world.add(createBall(-3, -104.0f, eight));
            world.add(createBall(3, -104.0f, nine));
            world.add(createBall(9, -104.0f, ten));

            world.add(createBall(-12, -110.0f, eleven));
            world.add(createBall(-6, -110.0f, twelve));
            world.add(createBall(0, -110.0f, thirteen));
            world.add(createBall(6, -110.0f, fourteen));
            world.add(createBall(12, -110.0f, fifteen));

            return world;
        }
    },
    OBJECTS {
        @Override
        public World createScene(int numSamples, float zoom, WorldListener listener) {
            PinholeCamera camera = new PinholeCamera(
                    new Vector3(30, 60, 100),
                    new Vector3(0, 0, 0),
                    new Vector3(0, 1, 0),
                    200);

            camera.setZoom(zoom);

            World world = new World(toString(), new Raycasting(), new Vector3(), camera);
            world.addListener(listener);

            //Lights
            world.setAmbientLight(new AmbientOccludedLight(1.5f, new Vector3(1.0f, 1.0f, 1.0f), 0.4f,
                    Sampler.newDefault(numSamples)));
            world.add(new PointLight(3.0f, new Vector3(1.0f, 1.0f, 1.0f), new Vector3(100.0f, 100.0f, 200.0f)));


            //Objects
            world.add(new Torus(45, 6f, new Phong(0.2f, 0.65f, 0.4f, 64.00f, new Vector3(1.0f, 0.8f, 0.3f))));
            world.add(new Disk(new Vector3(0, -20, 0), new Vector3(0, 1, 0), 60, new Phong(0.2f, 0.65f, 0.4f, 64.00f, new Vector3(1.0f, 0.3f, 0.3f))));
            world.add(new OpenCylinder(-20, 10, 60, new Phong(0.2f, 0.65f, 0.4f, 64.00f, new Vector3(1.0f, 0.3f, 0.3f))));
            world.add(new Box(-15, 15, -15, 15, -15, 15, new Phong(0.2f, 0.65f, 0.4f, 64.00f, new Vector3(0.3f, 0.3f, 1.0f))));
            return world;
        }
    };

    public abstract World createScene(int numSamples, float zoom, WorldListener listener);

    @Override
    public String toString() {
        String name = super.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
