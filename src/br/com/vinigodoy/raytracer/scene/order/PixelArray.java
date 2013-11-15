/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.scene.order;

import br.com.vinigodoy.raytracer.utility.Rnd;

import java.util.Iterator;

/**
 * Represents all screen pixels. The pixels can be reordered in a specific drawing order.
 */
public class PixelArray implements Iterable<PixelArray.Pixel> {
    private int[] pixels;
    private int width;

    public PixelArray(int w, int h) {
        this.pixels = new int[w * h];
        this.width = w;
    }

    /**
     * Enforces natural order in all pixels.
     * That is, the pixels are displaced in x order followed by y order.
     */
    public PixelArray setNaturalOrder() {
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = i;
        return this;
    }

    /**
     * Sets the order of a given pixel coordinate.
     *
     * @param order Order of the pixel. If another pixel is in that position, it will be overwritten.
     * @param x     The x coordinate
     * @param y     The y coordinate.
     */
    public void setPixel(int order, int x, int y) {
        pixels[order] = x + y * width;
    }

    /**
     * Returns the nth pixel.
     *
     * @param index Number of pixel to return
     * @return The pixel coordinates.
     */
    public Pixel getPixel(int index) {
        final int x = pixels[index] % width;
        final int y = pixels[index] / width;
        return new Pixel(x, y);
    }

    /**
     * @return The width of pixel area.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the pixel area.
     */
    public int getHeight() {
        return pixels.length / width;
    }

    /**
     * @return The number of pixels.
     */
    public int getCount() {
        return pixels.length;
    }

    /**
     * Shuflles the entire pixel array
     */
    public void shuffle() {
        for (int i = 0; i < pixels.length - 1; i++) {
            int max = pixels.length - i - 1;
            int ind = Rnd.rndInt(max);

            int aux = pixels[ind];
            pixels[ind] = pixels[max];
            pixels[max] = aux;
        }
    }

    @Override
    public Iterator<Pixel> iterator() {
        return new PixelIterator();
    }

    /**
     * Represents one pixel.
     */
    public static class Pixel {
        private int x;
        private int y;

        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * @return The pixel x position
         */
        public int getX() {
            return x;
        }

        /**
         * @return The pixel y position
         */
        public int getY() {
            return y;
        }
    }

    public class PixelIterator implements Iterator<Pixel> {
        private int count = 0;

        @Override
        public boolean hasNext() {
            return count != pixels.length;
        }

        @Override
        public Pixel next() {
            return getPixel(count++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
