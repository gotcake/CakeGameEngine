/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL2;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;
import org.cake.game.exception.GameException;

/**
 * A regular n-gon
 * @author Aaron Cake
 */
public class Polygon implements iConvex, iBoundingBox, iBoundingCircle {

    private List<Vector2> points;
    private Vector2 center;
    private int n;
    private float top, right, bottom, left, radius;

    public Polygon(Vector2 center, int n, float radius) {
        if (n < 3) throw new GameException("Polygons must have at least 3 points.");
        this.center = center;
        this.n = n;
        this.radius = radius;
        points = new ArrayList<>(n);
        for (int i=0; i< n; i++) {
            points.add(new Vector2());
        }
        setPoints();
    }

    public Polygon(float centerX, float centerY, int n, float radius) {
        if (n < 3) throw new GameException("Polygons must have at least 3 points.");
        this.center = new Vector2(centerX, centerY);
        this.n = n;
        this.radius = radius;
        points = new ArrayList<>(n);
        for (int i=0; i< n; i++) {
            points.add(new Vector2());
        }
        setPoints();
    }

    private void setPoints() {
        float dtheta = GeomUtil.PI2 / n, theta = -GeomUtil.PIO2;
        for (int i=0; i<n; i++) {
            points.get(i).set(
                    (float)Math.cos(theta) * radius + center.x,
                    (float)Math.sin(theta) * radius + center.y
                   );
            theta += dtheta;
        }
        updateAABB();
    }

    private void updateAABB() {
        Vector2 pt = points.get(0);
        top = bottom = pt.y;
        left = right = pt.x;
        for (int i=1; i < n; i++) {
            pt = points.get(i);
            if (pt.x < left)
                left = pt.x;
            if (pt.x > right)
                right = pt.x;
            if (pt.y < top)
                top = pt.y;
            if (pt.y > bottom)
                bottom = pt.y;
        }
    }

    @Override
    public List<Vector2> getPoints() {
        return points;
    }

    @Override
    public iBoundingBox getAABB() {
        return this;
    }

    @Override
    public iBoundingCircle getBoundingCircle() {
        return this;
    }

    @Override
    public boolean containsPoint(Vector2 point) {
        return point.distanceToSquared(center) <= (radius * radius) &&
                GeomUtil.checkAABBPoint(this, point) &&
                GeomUtil.containsPoint(points, point);
    }

    @Override
    public boolean intersects(iShape other) {
        return GeomUtil.checkAABBs(this, other.getAABB()) &&
                GeomUtil.checkBoundingCircles(this, other.getBoundingCircle()) &&
                GeomUtil.intersects(this, other);
    }

    @Override
    public void draw(GL2 gl) {
        gl.glBegin(GL2.GL_LINE_LOOP);
        Vector2 pt;
        for (int i=0; i<n; i++) {
            pt = points.get(i);
            gl.glVertex2f(pt.x, pt.y);
        }
        gl.glEnd();
    }

    @Override
    public void fill(GL2 gl) {
        gl.glBegin(GL2.GL_POLYGON);
        Vector2 pt;
        for (int i=0; i<n; i++) {
            pt = points.get(i);
            gl.glVertex2f(pt.x, pt.y);
        }
        gl.glEnd();
    }

    @Override
    public void draw(GL2 gl, iPointCallback pcb) {
        gl.glBegin(GL2.GL_LINE_LOOP);
        Vector2 pt;
        for (int i=0; i<n; i++) {
            pt = points.get(i);
            pcb.point(gl, pt);
            gl.glVertex2f(pt.x, pt.y);
        }
        gl.glEnd();
    }

    @Override
    public void fill(GL2 gl, iPointCallback pcb) {
        gl.glBegin(GL2.GL_POLYGON);
        Vector2 pt;
        for (int i=0; i<n; i++) {
            pt = points.get(i);
            pcb.point(gl, pt);
            gl.glVertex2f(pt.x, pt.y);
        }
        gl.glEnd();
    }

    @Override
    public void translate(float dx, float dy) {
        center.add(dx, dy);
        for (int i=0; i<n; i++) {
            points.get(i).add(dx, dy);
        }
        right += dx;
        left += dx;
        top += dy;
        bottom += dy;
    }

    @Override
    public void translate(Vector2 dpos) {
        center.add(dpos);
        for (int i=0; i<n; i++) {
            points.get(i).add(dpos);
        }
        right += dpos.x;
        left += dpos.x;
        top += dpos.y;
        bottom += dpos.y;
    }

    @Override
    public void rotate(float angle) {
        if (Math.abs(angle) > GeomUtil.EPSILON) {
            Transform2 trans = Transform2.createRotation(angle, center);
            trans.transformLocal(points);
        }
        updateAABB();
    }

    @Override
    public void transform(SimpleTransform2 trans) {
        translate(trans.getTranslationX(), trans.getTranslationY());
    }

    @Override
    public float getMinY() {
        return top;
    }

    @Override
    public float getMinX() {
        return left;
    }

    @Override
    public float getMaxY() {
        return bottom;
    }

    @Override
    public float getMaxX() {
        return right;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    @Override
    public float getRadiusSquared() {
        return radius * radius;
    }

    @Override
    public Vector2 getCenter() {
        return center;
    }

    @Override
    public void scale(float s) {
        radius *= s;
        setPoints();
    }

    public void setCenter(Vector2 center) {
        translate(center.subtract(this.center));
    }

    public void setCenter(float x, float y) {
        translate(x - center.x, y - center.y);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        setPoints();
    }

    @Override
    public float getArea() {
        return 0.5f * n * radius * radius * (float)Math.sin(GeomUtil.PI2 / n);
    }

    @Override
    public List<iContour> getContours() {
        return Arrays.asList((iContour)this);
    }

    @Override
    public boolean isClosed() {
        return true;
    }
}
