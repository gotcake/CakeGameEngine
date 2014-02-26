/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import org.cake.game.geom.Vector2;

/**
 * A viewport metrics which can resize with the window, but keeps the origin in the center.
 * @author Aaron Cake
 */
public class CenterOriginViewportMetrics implements iViewportMetrics {

    private int width, height;
    
    
    public CenterOriginViewportMetrics() {
        width = 800;
        height = 600;
    }
    
    public CenterOriginViewportMetrics(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void apply(Game game, GLAutoDrawable drawable, int width, int height) {
        this.width = width;
        this.height = height;
        float w2 = width / 2;
        float h2 = height / 2;
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-w2, w2, h2, -h2);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public Vector2 transformPoint(Vector2 p) {
        return p.sum(-width/2, -height/2);
    }

    @Override
    public Vector2 inverseTransformPoint(Vector2 p) {
        return p.sum(width/2, height/2);
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
        return new Vector2(width / -2.0f, height / -2.0f);
    }

    @Override
    public Vector2 getVirtualBottomRight() {
        return new Vector2(width / 2.0f, height / 2.0f);
    }

    @Override
    public Vector2 getVirtualSize() {
        return new Vector2(width, height);
    }

}
