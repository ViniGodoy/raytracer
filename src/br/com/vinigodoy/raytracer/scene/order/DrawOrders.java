/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.scene.order;

public enum DrawOrders implements DrawOrder {
    NORMAL {
        @Override
        public PixelArray getPixels(int w, int h) {
            return new PixelArray(w, h).setNaturalOrder();
        }
    },
    INTERLACED {
        @Override
        public PixelArray getPixels(int w, int h) {
            var pixels = new PixelArray(w, h);
            var count = 0;
            //Add even lines
            for (var y = 0; y < h; y += 2) {
                for (var x = 0; x < w; x++) {
                    pixels.setPixel(count++, x, y);
                }
            }

            //Add even columns
            for (var y = 1; y < h; y += 2) {
                for (var x = 0; x < w; x += 2) {
                    pixels.setPixel(count++, x, y);
                }
            }

            //Add odd lines and columns
            for (var y = 1; y < h; y += 2) {
                for (var x = 1; x < w; x += 2) {
                    pixels.setPixel(count++, x, y);
                }
            }

            return pixels;
        }
    },
    INTERLACED2 {
        @Override
        public PixelArray getPixels(int w, int h) {
            var pixels = new PixelArray(w, h);
            var count = 0;

            for (var y = 0; y < pixels.getHeight(); y += 4)
                for (var x = 0; x < pixels.getWidth(); x += 4) {
                    pixels.setPixel(count++, x, y);
                }

            for (var y = 1; y < pixels.getHeight(); y += 2)
                for (var x = 2; x < pixels.getWidth(); x += 4) {
                    pixels.setPixel(count++, x, y);
                }

            for (var y = 2; y < pixels.getHeight(); y += 4)
                for (var x = 0; x < pixels.getWidth(); x += 4) {
                    pixels.setPixel(count++, x, y);
                }

            for (var y = 0; y < pixels.getHeight(); y += 2)
                for (var x = 0; x < pixels.getWidth(); x += 4) {
                    if (x + 2 < pixels.getWidth())
                        pixels.setPixel(count++, x + 2, y);
                    if (y + 1 < pixels.getHeight())
                        pixels.setPixel(count++, x, y + 1);
                }

            for (var y = 0; y < pixels.getHeight(); y += 2)
                for (var x = 1; x < pixels.getWidth(); x += 2) {
                    pixels.setPixel(count++, x, y);
                }

            for (var y = 1; y < pixels.getHeight(); y += 2)
                for (var x = 1; x < pixels.getWidth(); x += 2) {
                    pixels.setPixel(count++, x, y);
                }

            return pixels;
        }
    },
    RANDOM {
        @Override
        public PixelArray getPixels(int w, int h) {
            var pixels = NORMAL.getPixels(w, h);
            pixels.shuffle();
            return pixels;
        }
    };

    @Override
    public String toString() {
        var name = super.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
