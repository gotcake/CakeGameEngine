/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import javax.media.opengl.GL2;
import org.cake.game.geom.Line2;
import org.cake.game.geom.Vector2;

/**
 * A basic 2d gradient that projects a 1d gradient along a line
 * @author Aaron Cake
 */
public class LinearGradient implements iGradient2 {

    private iGradient1 internal;
    private Line2 line;

    public LinearGradient(Vector2 p1, Vector2 p2, iGradient1 gradient) {
        line = new Line2(p1, p2);
        internal = gradient;
    }

    public LinearGradient(float x1, float y1, float x2, float y2, iGradient1 gradient) {
        line = new Line2(x1, y1, x2, y2);
        internal = gradient;
    }

    public LinearGradient(Line2 line, iGradient1 gradient) {
        this.line = line;
        internal = gradient;
    }

    public LinearGradient(Line2 line, iColor a, iColor b, boolean repeat) {
        this.line = line;
        internal = new BiColorGradient(a, b, repeat);
    }

    public LinearGradient(Vector2 p1, Vector2 p2, iColor a, iColor b, boolean repeat) {
        this.line = new Line2(p1, p2);
        internal = new BiColorGradient(a, b, repeat);
    }

    public LinearGradient(float x1, float y1, float x2, float y2, iColor a, iColor b, boolean repeat) {
        line = new Line2(x1, y1, x2, y2);
        internal = new BiColorGradient(a, b, repeat);
    }

    @Override
    public iColor getColor(Vector2 pos) {
        return internal.getColor(line.projectPoint(pos));
    }

    @Override
    public void point(GL2 gl, Vector2 pt) {
        internal.getColor(line.projectPoint(pt)).glBindColor(gl);
    }

}
