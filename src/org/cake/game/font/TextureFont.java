/*
    This file is part of CakeGame engine.

    CakeGame engine is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CakeGame engine is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CakeGame engine.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cake.game.font;

import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import javax.media.opengl.GL2;
import org.cake.game.Color;
import org.cake.game.Game;
import org.cake.game.collision.BoundingBox;
import org.cake.game.iInvalidationListener;
import org.cake.game.reflect.Reflect;

/**
 * A basic font that uses textures that are drawn on by Java2D to draw text.
 * @author Aaron Cake
 */
public class TextureFont implements iFont {

    public static final int PLAIN = java.awt.Font.PLAIN;
    public static final int ITALIC = java.awt.Font.ITALIC;
    public static final int BOLD = java.awt.Font.BOLD;

    private TextRenderer renderer;
    private Font font;
    private float scale, size, height, ascent, descent;
    private iInvalidationListener invalidation;
    private boolean inUse;

    public TextureFont(String name) {
        this(name, PLAIN, 20);
    }

    public TextureFont(String name, float size) {
        this(name, PLAIN, size);
    }

    public TextureFont(String name, int style, float size) {
        int isize = (int)size;
        this.size = size;
        scale = size / isize;
        font = new Font(name, style, isize);
        renderer = new TextRenderer(font, true, true, new TextRenderer.DefaultRenderDelegate(), true);
        Rectangle2D bounds = renderer.getFont().getMaxCharBounds(renderer.getFontRenderContext());
        height = (float)bounds.getHeight();
        ascent = (float)-bounds.getMinY();
        descent = (float)bounds.getMaxY();
        inUse = false;
        invalidation = Reflect.createProxy(iInvalidationListener.class, "invalidated", this, "invalidated");
        Game.currentGame().addListener(invalidation);
    }

    private void invalidated(Game g) {
        renderer = new TextRenderer(font, true, true, new TextRenderer.DefaultRenderDelegate(), true);
    }

    @Override
    public void draw(GL2 gl, Object str, float x, float y) {
        if (!inUse) {
            Color.glPushColor(gl);
            renderer.begin3DRendering();
            Color.glPopColor(gl);
            gl.glPushMatrix();
            gl.glScalef(1.0f, -1.0f, 1.0f);
        }
        renderer.draw3D(str.toString(), x, -y, 0, scale);
        if (!inUse) {
            renderer.flush();
            gl.glPopMatrix();
            renderer.end3DRendering();
        }
    }

    @Override
    public void beginDrawing(GL2 gl) {
        if (!inUse) {
            Color.glPushColor(gl);
            renderer.begin3DRendering();
            Color.glPopColor(gl);
            gl.glPushMatrix();
            gl.glScalef(1.0f, -1.0f, 1.0f);
            inUse = true;

        }
    }

    @Override
    public void endDrawing(GL2 gl) {
        if (inUse) {
            renderer.flush();
            gl.glPopMatrix();
            renderer.end3DRendering();
            inUse = false;
        }
    }

    @Override
    public BoundingBox getStringBounds(String str) {
        Rectangle2D bounds = renderer.getBounds(str);
        return new BoundingBox((float)bounds.getMinX(), (float)bounds.getMinY(), (float)bounds.getMaxX(), (float)bounds.getMaxY());
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

    @Override
    public void finalize() {
        Game.currentGame().removeListener(invalidation);
    }




}
