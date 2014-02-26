/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A image sheet implementation.
 * @author Aaron Cake
 */
public class ImageSheet implements iImageSet {

    private Image source;
    private List<Image> images;
    private int cols, rows;

    public ImageSheet(String img, int rows, int cols) {
        this(Image.get(img), rows, cols);
    }

    public ImageSheet(Image img, int rows, int cols) {
        this(img, rows, cols, img.getWidth() / cols, img.getHeight() / rows, -1);
    }

    public ImageSheet(String img, int rows, int cols, int limit) {
        this(Image.get(img), rows, cols, limit);
    }

    public ImageSheet(Image img, int rows, int cols, int limit) {
        this(img, rows, cols, img.getWidth() / cols, img.getHeight() / rows, limit);
    }

    public ImageSheet(Image img, int rows, int cols, float width, float height, int limit) {
        source = img;
        images = new ArrayList<>();
        this.cols = cols;
        this.rows = rows;
        for (int r = 0; r<rows; r++) {
            for (int c = 0; c<cols; c++) {
                if (limit > 0 && limit == images.size())
                    return;
                images.add(source.getSubImage(c * width, r * height, width, height));
            }
        }
    }

    public ImageSheet(String img, int rows, int cols, float width, float height, int limit) {
        this(Image.get(img), rows, cols, width, height, limit);
    }

    @Override
    public Image get(int i) {
        return images.get(i);
    }


    public Image get(int row, int col) {
        return images.get(row * cols + col);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return rows;
    }

    @Override
    public int getNumImages() {
        return images.size();
    }

    @Override
    public Iterator<Image> iterator() {
        return images.iterator();
    }

}
