/*===========================================================================
COPYRIGHT Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytrace.sample;

import br.com.vinigodoy.raytrace.math.Sphere;
import br.com.vinigodoy.raytrace.math.Vector3;
import br.com.vinigodoy.raytrace.scene.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SampleFrame extends JFrame {
    private JLabel output = new JLabel("");
    private JButton btnDraw = new JButton("Draw");
    private Scene scene;

    public SampleFrame() {
        super("Ray tracing demo. Click in the button to draw.");
        scene = createScene();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        output.setPreferredSize(new Dimension(800, 600));
        btnDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long time = System.currentTimeMillis();
                BufferedImage img = new BufferedImage(output.getWidth(), output.getHeight(), BufferedImage.TYPE_INT_RGB);
                scene.draw(img);
                output.setIcon(new ImageIcon(img));
                double diff = (System.currentTimeMillis() - time) / 1000.f;
                setTitle(String.format("Ray tracing demo. Time to draw %.2f seconds", diff));
            }
        });

        add(output, BorderLayout.CENTER);
        add(btnDraw, BorderLayout.NORTH);
        getRootPane().setDefaultButton(btnDraw);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private Scene createScene() {
        Material groundMtrl = new MaterialBuilder()
                .withColor(0.4f, 0.3f, 0.3f)
                .withDiffuse(1.0f)
                .make();

        Material bigSphereMtrl = new MaterialBuilder()
                .withColor(0.7f, 0.7f, 1.0f)
                .withReflection(0.2f)
                .withRefraction(1.5f)
                .make();

        Material smallSphereMtrl = new MaterialBuilder()
                .withColor(0.7f, 0.7f, 1.0f)
                .withReflection(0.5f)
                .withDiffuse(0.1f)
                .withSpecular(0.8f).make();

        Material extraSphereMtrl = new MaterialBuilder()
                .withColor(1.0f, 0.4f, 0.4f)
                .withRefraction(1.5f)
                .withDiffuse(0.2f)
                .withSpecular(0.8f).make();

        Material wallMtrl = new MaterialBuilder()
                .withColor(0.5f, 0.3f, 0.5f)
                .withReflection(0.0f)
                .withDiffuse(0.6f)
                .withSpecular(0.0f).make();

        Material ceilingMtrl = new MaterialBuilder()
                .withColor(0.4f, 0.7f, 0.7f)
                .withReflection(0.0f)
                .withDiffuse(0.5f)
                .withSpecular(0.0f).make();

        Scene scene = new SceneBuilder()
                .addLight("Light 1", new Vector3(0, 5, 5), new Vector3(0.4f, 0.4f, 0.4f))
                .addLight("Light 2", new Vector3(-3, 5, 1), new Vector3(0.7f, 0.7f, 0.9f))
                .addPlane("Ground", new Vector3(0, 1, 0), 4.4f, groundMtrl)
                .addSphere("Big sphere", new Vector3(2.0f, 0.8f, 3), 2.5f, bigSphereMtrl)
                .addSphere("Small sphere", new Vector3(-5.5f, -0.5f, 7.0f), 2.0f, smallSphereMtrl)
                .addSphere("Front sphere", new Vector3(-1.5f, -3.8f, 1), 1.5f, extraSphereMtrl)
                .addPlane("Wall", new Vector3(0.4f, 0.0f, -1.0f), 12.0f, wallMtrl)
                .addPlane("Ceiling", new Vector3(0.0f, -1.0f, 0.0f), 7.4f, ceilingMtrl)
                .getScene();

        Material gridMaterial = new MaterialBuilder()
                .withColor(1.0f, 1.0f, 1.0f)
                .withSpecular(0.6f)
                .withDiffuse(0.9f).make();

        for (int x = 1; x < 8; x++)
            for (int y = 1; y < 7; y++) {
                Sphere sphere = new Sphere(new Vector3(-4.5f + x * 1.5f, -4.3f + y * 1.5f, 10), 0.3f);
                scene.add(new SceneObject(String.format("Grid[%d,%d]", x, y), sphere, gridMaterial));
            }

        return scene;
    }

    public static void main(String[] args) {
        new SampleFrame().setVisible(true);
    }
}
