/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.gui;

import br.com.vinigodoy.raytracer.math.Vector3;
import br.com.vinigodoy.raytracer.scene.ViewPlane;
import br.com.vinigodoy.raytracer.scene.World;
import br.com.vinigodoy.raytracer.scene.WorldListener;
import br.com.vinigodoy.raytracer.scene.order.DrawOrder;
import br.com.vinigodoy.raytracer.scene.order.DrawOrders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class SampleFrame extends JFrame {
    private static final String VERSION = "1.9b";
    private static final String RENDER_INFO = "Java Raytracer v" + VERSION +
            " - Scene: %s - Render time: %s";
    private static final String COMPLETE_RENDER_INFO = RENDER_INFO + " - https://github.com/ViniGodoy/raytracer";

    private boolean renderToScreen = true;

    private final JLabel output = new JLabel("");
    private final JButton btnDraw = new JButton("Draw");
    private final JButton btnSave = new JButton("Save in full HD");

    private final JComboBox<WorldMaker> cmbScene = new JComboBox<>();
    private final JComboBox<DrawOrder> cmbDrawOrder = new JComboBox<>();
    private final JComboBox<Quality> cmbQuality = new JComboBox<>();

    private final JFileChooser chooser = new JFileChooser();
    private final JProgressBar pbProgress = new JProgressBar();

    private final WorldWaiter waiter;

    public SampleFrame() {
        super("Java Ray Tracer v" + VERSION + " demo. Click in the button to draw.");
        Locale.setDefault(Locale.US);

        waiter = new WorldWaiter();

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        output.setPreferredSize(new Dimension(800, 450));

        pbProgress.setString("");
        pbProgress.setStringPainted(true);

        var pnlButtons = new JPanel(new FlowLayout());

        //Scene
        pnlButtons.add(new JLabel("Scene:"));
        for (var wm : WorldMaker.values()) {
            cmbScene.addItem(wm);
        }
        cmbScene.setSelectedItem(WorldMaker.OBJECTS);
        pnlButtons.add(cmbScene);

        //Draw order
        pnlButtons.add(new JLabel("Order:"));
        for (var drawOrder : DrawOrders.values()) {
            cmbDrawOrder.addItem(drawOrder);
        }
        cmbDrawOrder.setSelectedItem(DrawOrders.INTERLACED2);
        pnlButtons.add(cmbDrawOrder);

        //Quality
        pnlButtons.add(new JLabel("Quality:"));
        for (var quality : Quality.values()) {
            cmbQuality.addItem(quality);
        }
        cmbQuality.setSelectedItem(Quality.LOW);
        pnlButtons.add(cmbQuality);

        //Buttons
        btnDraw.addActionListener(e -> renderToScreen());
        pnlButtons.add(btnDraw);

        btnSave.addActionListener(e -> renderToFile());
        pnlButtons.add(btnSave);

        add(output, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.NORTH);
        add(pbProgress, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnDraw);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SampleFrame().setVisible(true);
    }

    private String formatRenderTime(long renderTime) {
        if (renderTime < 60000)
            return String.format("%.2f seconds", (renderTime / 1000.0));

        renderTime /= 1000;
        var s = (int) (renderTime % 60);
        renderTime /= 60;
        var m = (int) (renderTime % 60);

        var strMin = m == 1 ? "minute" : "minutes";
        var strSec = s == 1 ? "second" : "seconds";
        return String.format("%d %s %d %s", m, strMin, s, strSec);
    }

    public void saveFile(World world, long renderTime) {
        var fileName = new File("Java Raytracer-v" + VERSION.replace(".", "_") + "_" + world.getName() + ".png");
        chooser.setSelectedFile(fileName);

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            var g2d = waiter.getImage().createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, 1920, 25);
            g2d.setColor(Color.WHITE);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(String.format(COMPLETE_RENDER_INFO, world.getName(), formatRenderTime(renderTime)), 20, 17);
            g2d.dispose();

            ImageIO.write(waiter.getImage(), "png", new FileOutputStream(chooser.getSelectedFile()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to save file: " + chooser.getSelectedFile().getName());
        }
    }

    public void renderToScreen() {
        renderToScreen = true;

        var samples = ((Quality) cmbQuality.getSelectedItem()).getSamples();
        var world = ((WorldMaker) cmbScene.getSelectedItem()).createScene(samples, 1.0f, waiter);
        var drawOrder = (DrawOrder) cmbDrawOrder.getSelectedItem();

        var vp = new ViewPlane(800, 450, samples);
        vp.setDrawOrder(drawOrder);
        world.render(vp);
    }

    private void renderToFile() {
        renderToScreen = false;
        var samples = ((Quality) cmbQuality.getSelectedItem()).getSamples();
        var world = ((WorldMaker) cmbScene.getSelectedItem()).createScene(samples, 2.4f, waiter);
        world.render(new ViewPlane(1920, 1080, samples));
    }

    private enum Quality {
        LOW(16), MEDIUM(49), HIGH(100), ULTRA(144);

        private final int samples;

        Quality(int samples) {
            this.samples = samples;
        }

        public int getSamples() {
            return samples;
        }

        @Override
        public String toString() {
            var name = super.toString();
            return name.charAt(0) + name.substring(1).toLowerCase();

        }
    }

    public class WorldWaiter implements WorldListener {
        private int count;
        private BufferedImage image;
        private long lastTimePainted;

        @Override
        public void traceStarted(World world, int width, int height) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            pbProgress.setMinimum(0);
            pbProgress.setMaximum(width * height);
            count = 0;
            EventQueue.invokeLater(() -> {
                btnDraw.setEnabled(false);
                btnSave.setEnabled(false);
                if (renderToScreen) {
                    btnDraw.setText("Drawing...");
                    output.setIcon(new ImageIcon(image));
                } else {
                    btnSave.setText("Drawing...");
                }
            });


            var g2d = image.createGraphics();
            g2d.setColor(new Color(world.getBackgroundColor().toRGB()));
            g2d.fillRect(0, 0, width, height);
            lastTimePainted = System.currentTimeMillis();
        }

        @Override
        public void pixelTraced(World world, int x, int y, Vector3 color) {
            image.setRGB(x, y, color.toRGB());
            count++;

            if (System.currentTimeMillis() - lastTimePainted > 500) {
                pbProgress.setValue(count);
                pbProgress.setString(String.format("Drawing %s: %.2f%% - pixel %d of %d",
                        world.getName(), count * 100.0 / pbProgress.getMaximum(), count, pbProgress.getMaximum()));

                lastTimePainted = System.currentTimeMillis();
                repaint();
            }
        }

        @Override
        public void traceFinished(final World world, final long renderTime) {
            setTitle(String.format(RENDER_INFO, world.getName(), formatRenderTime(renderTime)));
            world.removeListener(this);
            EventQueue.invokeLater(() -> {
                btnSave.setText("Save in full HD");
                btnSave.setEnabled(true);
                btnDraw.setText("Draw");
                btnDraw.setEnabled(true);
                pbProgress.setValue(0);
                pbProgress.setString("Done!");
                if (!renderToScreen) {
                    saveFile(world, renderTime);
                }
            });
            repaint();
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}