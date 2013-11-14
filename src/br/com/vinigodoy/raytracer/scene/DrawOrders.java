/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.scene;

import br.com.vinigodoy.raytracer.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum DrawOrders implements DrawOrder {
    NORMAL {
        @Override
        public List<Vector2> getPixels(int w, int h) {
            List<Vector2> pixels = new ArrayList<>(w * h);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pixels.add(new Vector2(x, y));
                }
            }
            return pixels;
        }
    },
    INTERLACED {
        @Override
        public List<Vector2> getPixels(int w, int h) {
            List<Vector2> pixels = new ArrayList<>(w * h);
            //Add even lines
            for (int y = 0; y < h; y += 2) {
                for (int x = 0; x < w; x++) {
                    pixels.add(new Vector2(x, y));
                }
            }

            //Add even columns
            for (int y = 1; y < h; y += 2) {
                for (int x = 0; x < w; x += 2) {
                    pixels.add(new Vector2(x, y));
                }
            }

            //Add odd lines and columns
            for (int y = 1; y < h; y += 2) {
                for (int x = 1; x < w; x += 2) {
                    pixels.add(new Vector2(x, y));
                }
            }

            return pixels;
        }
    },
    RANDOM {
        @Override
        public List<Vector2> getPixels(int w, int h) {
            List<Vector2> pixels = new ArrayList<>(w * h);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pixels.add(new Vector2(x, y));
                }
            }
            Collections.shuffle(pixels);
            return pixels;
        }
    };

    @Override
    public String toString() {
        String name = super.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
