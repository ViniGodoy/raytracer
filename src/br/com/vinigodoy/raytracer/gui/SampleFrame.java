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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class SampleFrame extends JFrame {
    private static final String VERSION = "1.7b";
    private static final String RENDER_INFO = "Java Raytracer v" + VERSION +
            " - Scene: %s - Render time: %s";
    private static final String COMPLETE_RENDER_INFO = RENDER_INFO + " - https://github.com/ViniGodoy/raytracer";

    private static final int SAMPLES = 100;

    private boolean renderToScreen = true;

    private JLabel output = new JLabel("");
    private JButton btnDraw = new JButton("Draw");
    private JButton btnSave = new JButton("Save in full HD");

    private JComboBox<WorldMaker> cmbScene = new JComboBox<WorldMaker>();
    private JComboBox<DrawOrder> cmbDrawOrder = new JComboBox<DrawOrder>();

    private JFileChooser chooser = new JFileChooser();
    private JProgressBar pbProgress = new JProgressBar();

    private WorldWaiter waiter;

    public SampleFrame() {
        super("Java Ray Tracer v" + VERSION + " demo. Click in the button to draw.");
        Locale.setDefault(Locale.US);

        waiter = new WorldWaiter();

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        output.setPreferredSize(new Dimension(800, 450));

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

        for (WorldMaker wm : WorldMaker.values()) {
            cmbScene.addItem(wm);
        }
        cmbScene.setSelectedItem(WorldMaker.OBJECTS);

        for (DrawOrder drawOrder : DrawOrders.values()) {
            cmbDrawOrder.addItem(drawOrder);
        }
        cmbDrawOrder.setSelectedItem(DrawOrders.INTERLACED2);
        pbProgress.setString("");
        pbProgress.setStringPainted(true);

        JPanel pnlButtons = new JPanel(new FlowLayout());
        pnlButtons.add(new JLabel("Scene:"));
        pnlButtons.add(cmbScene);

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

    private String formatRenderTime(long renderTime) {
        if (renderTime < 60000)
            return String.format("%.2f seconds", (renderTime / 1000.0));

        renderTime /= 1000;
        int s = (int) (renderTime % 60);
        renderTime /= 60;
        int m = (int) (renderTime % 60);

        String strMin = m == 1 ? "minute" : "minutes";
        String strSec = s == 1 ? "second" : "seconds";
        return String.format("%d %s %d %s", m, strMin, s, strSec);
    }

    public void saveFile(World world, long renderTime) {
        File fileName = new File("Java Raytracer-v" + VERSION.replace(".", "_") + "_" + world.getName() + ".png");
        chooser.setSelectedFile(fileName);

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            Graphics2D g2d = waiter.getImage().createGraphics();
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

    public static void main(String[] args) {
        new SampleFrame().setVisible(true);
    }

    public void renderToScreen() {
        renderToScreen = true;

        ViewPlane vp = new ViewPlane(800, 450, SAMPLES);
        vp.setDrawOrder((DrawOrder) cmbDrawOrder.getSelectedItem());
        World world = ((WorldMaker) cmbScene.getSelectedItem()).createScene(SAMPLES, 1.0f, waiter);
        world.render(vp);
    }

    private void renderToFile() {
        renderToScreen = false;

        World world = ((WorldMaker) cmbScene.getSelectedItem()).createScene(SAMPLES, 2.4f, waiter);
        world.render(new ViewPlane(1920, 1080, SAMPLES));
    }

    public class WorldWaiter implements WorldListener {
        private int count;
        private BufferedImage image;
        private Graphics2D g2d;
        private long lastTimePainted;

        @Override
        public void traceStarted(World world, int width, int height) {
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
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    btnSave.setText("Save in full HD");
                    btnSave.setEnabled(true);
                    btnDraw.setText("Draw");
                    btnDraw.setEnabled(true);
                    pbProgress.setValue(0);
                    pbProgress.setString("Done!");
                    if (!renderToScreen) {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                saveFile(world, renderTime);
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