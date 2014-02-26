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
import org.cake.game.geom.Transform2;
import org.cake.game.geom.Vector2;

/**
 * A ViewportMetrics for a fixed orthographic projection to be scaled to fill the window.
 * @author Aaron Cake
 */
public class StretchViewportMetrics implements iViewportMetrics {

    private float vwidth, vheight;
    private int dwidth, dheight;
    private boolean needsLaidOut = false;
    private Transform2 transform, transformInvert;

    public StretchViewportMetrics(float width, float height) {
        this.vwidth = width;
        this.vheight = height;
        transform = new Transform2();
        transformInvert = new Transform2();
    }

    @Override
    public void apply(Game game, GLAutoDrawable drawable, int width, int height) {
        dwidth = width;
        dheight = height;
        layout(drawable);
    }

    private void layout(GLAutoDrawable drawable) {
        needsLaidOut = false;

        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();
        gl.glViewport(0, 0, dwidth, dheight);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, vwidth, vheight, 0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        transform = new Transform2();
        transform.scaleLocal(vwidth / dwidth, vheight / dheight);
        transformInvert = transform.inverse();
    }

    @Override
    public Vector2 transformPoint(Vector2 p) {
        return transform.transform(p);
    }

    @Override
    public Vector2 inverseTransformPoint(Vector2 p) {
        return transformInvert.transform(p);
    }

    @Override
    public Vector2 clipPoint(Vector2 p) {
        return p;
    }

    @Override
    public boolean isDirty() {
        return needsLaidOut;
    }

    public void setSize(float w, float h) {
        w = (w > 0 ? w : 1);
        h = (h > 0 ? h : 1);
        if (w != vwidth || h != vheight)
            needsLaidOut = true;
        vwidth = w;
        vheight = h;
    }

    @Override
    public float getVirtualWidth() {
        return vwidth;
    }

    @Override
    public float getVirtualHeight() {
        return vheight;
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
        return dwidth;
    }

    @Override
    public int getDisplayHeight() {
        return dheight;
    }

    @Override
    public Vector2 getVirtualTopLeft() {
        return new Vector2(0, 0);
    }

    @Override
    public Vector2 getVirtualBottomRight() {
        return new Vector2(vwidth, vheight);
    }

    @Override
    public Vector2 getVirtualSize() {
        return new Vector2(vwidth, vheight);
    }


}
