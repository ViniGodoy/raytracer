package br.com.vinigodoy.raytrace.sample;

import br.com.vinigodoy.raytrace.math.Vector3;
import br.com.vinigodoy.raytrace.scene.Material;
import br.com.vinigodoy.raytrace.scene.Scene;
import br.com.vinigodoy.raytrace.scene.SceneBuilder;

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
        scene = createScene();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        output.setPreferredSize(new Dimension(1024, 768));
        btnDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long time = System.currentTimeMillis();
                BufferedImage img = new BufferedImage(output.getWidth(), output.getHeight(), BufferedImage.TYPE_INT_RGB);
                scene.draw(img);
                output.setIcon(new ImageIcon(img));
                double diff = (System.currentTimeMillis() - time) / 1000.f;
                setTitle(String.format("Time to draw %.2f seconds", diff));
            }
        });

        add(output, BorderLayout.CENTER);
        add(btnDraw, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnDraw);
        pack();
        setVisible(true);
    }


    private Scene createScene() {
        return new SceneBuilder()
                .addPlane("Plane1", new Vector3(1, 0, -1), 15f, new Material(new Vector3(0.4f, 0.3f, 0.3f), 1.0f, 0f))
                .addPlane("Plane2", new Vector3(0, 1, 0), 4.4f, new Material(new Vector3(0.4f, 0.3f, 0.3f), 1.0f, 0f))
                .addSphere("Big Sphere", new Vector3(1, -0.8f, 3), 2.5f, new Material(new Vector3(0.7f, 0.7f, 0.7f), 0.6f, 0.6f))
                .addSphere("Small Sphere", new Vector3(-5.5f, -0.5f, 7.0f), 2.0f, new Material(new Vector3(0.7f, 0.7f, 1.0f), 0.1f, 1.0f))
                .addSphere("Small Sphere", new Vector3(5.0f, -0.5f, 7.0f), 2.0f, new Material(new Vector3(0.6f, 0.6f, 0.6f), 0.3f, 1.0f))
                .addLight("Light1", new Vector3(0, 5, 5), new Vector3(1.0f, 1.0f, 1.0f))
                .addLight("Light2", new Vector3(2, 5, 1), new Vector3(0.7f, 0.7f, 0.9f))
                .getScene();
    }

    public static void main(String[] args) {
        new SampleFrame().setVisible(true);
    }
}
