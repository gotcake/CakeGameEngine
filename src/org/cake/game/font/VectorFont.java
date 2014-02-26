/*
    
    This file is part of CakeGame.
    Copyright (C) 2013  Aaron Cake

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.cake.game.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.media.opengl.GL2;
import org.cake.collections.SoftCache;
import org.cake.collections.iCache;
import org.cake.game.collision.BoundingBox;
import org.cake.game.geom.GeomUtil;
import org.cake.game.geom.Shape;

/**
 *
 * @author Aaron
 */
public class VectorFont implements iFont {
    
     public static final int PLAIN = java.awt.Font.PLAIN;
    public static final int ITALIC = java.awt.Font.ITALIC;
    public static final int BOLD = java.awt.Font.BOLD;
    
    private Font font;
    private FontRenderContext frc;
    private float scale, size, height, ascent, descent;
    private iCache<String, Shape> glyphCache;
    private boolean inUse;

    public VectorFont(String name) {
        this(name, PLAIN, 20);
    }

    public VectorFont(String name, float size) {
        this(name, PLAIN, size);
    }

    public VectorFont(String name, int style, float size) {
        glyphCache = new SoftCache();
        int isize = (int)size;
        this.size = size;
        scale = size / isize;
        font = new Font(name, style, isize);
        frc = new FontRenderContext(new AffineTransform(), false, true);
        Rectangle2D bounds = font.getMaxCharBounds(frc);
        height = (float)bounds.getHeight();
        ascent = (float)-bounds.getMinY();
        descent = (float)bounds.getMaxY();
        inUse = false;
    }

    @Override
    public void draw(GL2 gl, Object string, float x, float y) {
        String str = string.toString();
        Shape s = glyphCache.get(str);
        if (s == null) {
            GlyphVector glyph = font.createGlyphVector(frc, str);
            java.awt.Shape outline = glyph.getOutline(0, 0);
            s = GeomUtil.javaShapeToGameShape(outline, 2);
            glyphCache.put(str, s);
        }
        gl.glTranslatef(x, y, 0);
        s.draw(gl);
        gl.glTranslatef(-x, -y, 0);
    }

    @Override
    public void beginDrawing(GL2 gl) {
        
    }

    @Override
    public void endDrawing(GL2 gl) {
        
    }

    @Override
    public BoundingBox getStringBounds(String str) {
         Rectangle2D b = font.getStringBounds(str, frc);
         return new BoundingBox((float)b.getMinX() * scale, (float)b.getMinX() * scale, (float)b.getMaxX() * scale, (float)b.getMaxY() * scale);
    }

    @Override
    public float getSize() {
        return size;
    }

    @Override
    public void setSize(float size) {
        scale = scale / this.size * size;
        this.size = size;
    }

    @Override
    public float getLineHight() {
        return height * scale;
    }

    @Override
    public float getAscent() {
        return ascent * scale;
    }

    @Override
    public float getDescent() {
        return descent * scale;
    }

}
