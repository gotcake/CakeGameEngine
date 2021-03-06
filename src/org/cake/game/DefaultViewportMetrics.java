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

package org.cake.game;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import org.cake.game.geom.Vector2;

/**
 * A "null" 2D orthographic iViewportMetrics that matches the internal coordinates with external coordinates.
 * @author Aaron Cake
 */
public class DefaultViewportMetrics implements iViewportMetrics {

    private int width, height;

    @Override
    public void apply(Game game, GLAutoDrawable drawable, int width, int height) {
        this.width = width;
        this.height = height;
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, width, height, 0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public Vector2 transformPoint(Vector2 p) {
        return p;
    }

    @Override
    public Vector2 inverseTransformPoint(Vector2 p) {
        return p;
    }

    @Override
    public Vector2 clipPoint(Vector2 p) {
        return p;
    }

    @Override
    public float getVirtualWidth() {
        return width;
    }

    @Override
    public float getVirtualHeight() {
        return height;
    }

    @Override
    public int getDisplayOffsetX() {
        return 0;
    }

    @Override
    public int getDisplayOffsetY() {
        return 0;
    }

    @Override
    public int getDisplayWidth() {
        return width;
    }

    @Override
    public int getDisplayHeight() {
        return height;
    }

    @Override
    public Vector2 getVirtualTopLeft() {
        return new Vector2(0, 0);
    }

    @Override
    public Vector2 getVirtualBottomRight() {
        return new Vector2(width, height);
    }

    @Override
    public Vector2 getVirtualSize() {
        return new Vector2(width, height);
    }



}
