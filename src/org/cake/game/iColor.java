/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import javax.media.opengl.GL2;

/**
 * An interface describing a color.
 * @author Aaron Cake
 */
public interface iColor {

    /**
     * Bind this color to the openGL context.
     * Equivalent to doing gl.glColor4f(iColor.getRed(), iColor.getGreen(), iColor.getBlue(), Color.getAlpha());
     * @param gl the GL context
     */
    public void glBindColor(GL2 gl);

    /**
     * Bind the opaque version of this color to the openGL context.
     * Equivalent to doing gl.glColor3f(iColor.getRed(), iColor.getGreen(), iColor.getBlue());
     * @param gl the GL context
     */
    public void glBindColorOpaque(GL2 gl);
    
    /**
     * Bind the this color multiplied by the given alpha mask to the openGL context.
     * Equivalent to doing gl.glColorff(iColor.getRed(), iColor.getGreen(), iColor.getBlue(), iColor.getAlpha() * alpha);
     * @param gl the GL context
     */
    public void glBindColorWithAlphaMask(GL2 gl, float alpha);

    /**
     * Gets the alpha component of the color (the opaqueness)
     * @return the alpha component (0.0 to 1.0)
     */
    public float getAlpha();

    /**
     * Gets the red component of the color
     * @return the red component (0.0 to 1.0)
     */
    public float getRed();

    /**
     * Gets the green component of the color
     * @return the green component (0.0 to 1.0)
     */
    public float getGreen();

    /**
     * Gets the blue component of the color
     * @return the blue component (0.0 to 1.0)
     */
    public float getBlue();

    /**
     * Make a 50/50 blend of this color with another color
     * @param other another color
     * @return the blended Color
     */
    public Color blend(iColor other);

    /**
     * Interpolate from this color to the given color by the amount t.
     *
     * @return the blended Color
     */
    /**
     * Interpolate from this color to the given color by the amount t.
     * @param other another color
     * @param t the amount to interpolate (0.0 to 1.0)
     * @return the interpolated Color
     */
    public Color interpolate(iColor other, float t);

    /**
     * Multiply this color with another color
     * @param other another color
     * @return the multiplied color
     */
    public Color multiply(iColor other);

    /**
     * Get an opaque version of this color, that is with an alpha value of 1.
     * @return the opaque version of this color.
     */
    public Color opaque();

    /**
     * Get an transparent version of this color, that is with an alpha value of 0.
     * @return the transparent version of this color.
     */
    public Color transparent();

    /**
     * Gets a mutable copy of this color.
     * @return mutable copy of this color
     */
    public Color copy();

    /**
     * Gets a basic string representation of this color
     * Defaults to the name of the color, if defined, else the hex string
     * @return
     */
    public String colorString();
    
    /**
     * Gets the integer representation of this color in the form RGBA
     * where R is the highest byte
     * @return 
     */
    public int toInt();
    
    /**
     * Gets a hex string representation of this color in the form #RRGGBB[AA]
     * or #RGB[A]
     * @return 
     */
    public String toHexString();

}
