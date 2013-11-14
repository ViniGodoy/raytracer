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
import br.com.vinigodoy.raytracer.light.PointLight;
import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.math.geometry.Sphere;
import br.com.vinigodoy.raytracer.scene.*;
import br.com.vinigodoy.raytracer.tracer.Raycasting;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SampleFrame extends JFrame {
    private boolean renderToScreen = true;
    private static final String version = "1.4b";

    private static final int SAMPLES = 4;

    private JLabel output = new JLabel("");
    private JButton btnDraw = new JButton("Draw");
    private JButton btnSave = new JButton("Save in full HD");
    private JComboBox<DrawOrder> cmbDrawOrder = new JComboBox<>();

    private JFileChooser chooser = new JFileChooser();
    private JProgressBar pbProgress = new JProgressBar();

    private World world;
    private WorldWaiter waiter;

    private PinholeCamera camera;
    float deg = 0;

    public SampleFrame() {
        super("Java Ray Tracer v" + version + " demo. Click in the button to draw.");
        chooser.setSelectedFile(new File("JavaTracer-v" + version.replace(".", "_") + ".png"));
        camera = new PinholeCamera(
                new Vector3(0, 0, 800),
                new Vector3(0, 0, 0),
                new Vector3(0, 1, 0),
                2000);

        world = createScene();
        waiter = new WorldWaiter();
        world.addListener(waiter);

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        output.setPreferredSize(new Dimension(800, 600));

        btnDraw.setPreferredSize(new Dimension(200, 25));
        btnDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderToScreen();
            }
        });

        btnSave.setPreferredSize(new Dimension(200, 25));
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderToFile();
            }
        });

        for (DrawOrder drawOrder : DrawOrders.values()) {
            cmbDrawOrder.addItem(drawOrder);
        }
        cmbDrawOrder.setSelectedItem(DrawOrders.RANDOM);

        JPanel pnlButtons = new JPanel(new FlowLayout());
        pnlButtons.add(new JLabel("Order:"));
        pnlButtons.add(cmbDrawOrder);
        pnlButtons.add(btnDraw);
        pnlButtons.add(btnSave);

        add(output, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.NORTH);
        add(pbProgress, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnDraw);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private World createScene() {
        world = new World(new Raycasting(), new Vector3(), camera);

        //Lights
        world.add(new PointLight(3.0f, new Vector3(1.0f, 1.0f, 1.0f), new Vector3(100.0f, 100.0f, 150.0f)));

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

    public void saveFile(double renderTime) {
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            Graphics2D g2d = waiter.getImage().createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, 1920, 25);
            g2d.setColor(Color.WHITE);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(String.format("Java Raytracer v%s - Render time: %.2f seconds - https://github.com/ViniGodoy/raytracer", version, renderTime), 20, 17);
            g2d.dispose();

            ImageIO.write(waiter.getImage(), "png", new FileOutputStream(chooser.getSelectedFile()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to save file: " + chooser.getSelectedFile().getName());
        }
    }

    public static void main(String[] args) {
        new SampleFrame().setVisible(true);
    }

    public void renderToScreen() {
        renderToScreen = true;
        camera.setZoom(1.0f);

        ViewPlane vp = new ViewPlane(800, 600, SAMPLES);
        vp.setDrawOrder((DrawOrder) cmbDrawOrder.getSelectedItem());

        world.render(vp);
    }

    private void renderToFile() {
        renderToScreen = false;
        camera.setZoom(1080.0f / 600.0f);
        world.render(new ViewPlane(1920, 1080, SAMPLES));
    }

    public class WorldWaiter implements WorldListener {
        private int count;
        private BufferedImage image;
        private Graphics2D g2d;
        private long lastTimePainted;

        @Override
        public void traceStarted(int width, int height, Vector3 backgroundColor) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            pbProgress.setMinimum(0);
            pbProgress.setMaximum(width * height);
            count = 0;
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    btnDraw.setEnabled(false);
                    btnSave.setEnabled(false);
                    if (renderToScreen) {
                        btnDraw.setText("Drawing...");
                        output.setIcon(new ImageIcon(image));
                    } else {
                        btnSave.setText("Drawing...");
                    }
                }
            });


            g2d = image.createGraphics();
            g2d.setColor(backgroundColor.toColor());
            g2d.fillRect(0, 0, width, height);
            lastTimePainted = System.currentTimeMillis();
        }

        @Override
        public void pixelTraced(int x, int y, Vector3 color) {
            image.setRGB(x, y, color.toColor().getRGB());
            count++;

            if (System.currentTimeMillis() - lastTimePainted > 500) {
                pbProgress.setValue(count);
                lastTimePainted = System.currentTimeMillis();
                repaint();
            }
        }

        @Override
        public void traceFinished(final double renderTime) {
            setTitle(String.format("Java Raytracer v%s demo. Time to draw %.2f seconds", version, renderTime));
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    btnSave.setText("Save in full HD");
                    btnSave.setEnabled(true);
                    btnDraw.setText("Draw");
                    btnDraw.setEnabled(true);
                    pbProgress.setValue(0);
                    if (!renderToScreen) {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                saveFile(renderTime);
                            }
                        });
                    }
                }
            });
            repaint();
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}