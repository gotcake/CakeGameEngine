/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.bakery;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Aaron
 */
public class ImageDisplay extends JPanel {

    private Image img;

    public void setImage(Image img) {
        this.img = img;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (img != null)
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }

}
