/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL2;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;

/**
 *
 * @author Aaron
 */
public class Triangle implements iConvex, iBoundingBox, iBoundingCircle {

    private float radius;
    private Vector2 center, p1, p2, p3;
    private float top, left, bottom, right;

    public Triangle(Vector2 center, float size) {
        this(center.x, center.y, size, 0);
    }

    public Triangle(Vector2 center, float size, float angle) {
        this(center.x, center.y, size, angle);
    }

    public Triangle(float centerX, float centerY, float size) {
        this(centerX, centerY, size, 0);
    }

    public Triangle(float centerX, float centerY, float size, float angle) {
        center = new Vector2(centerX, centerY);
        float ss = (float)Math.sqrt(3) * size / 3;
        p1 = new Vector2(centerX, centerY - ss);
        p2 = new Vector2(centerX + size / 2, centerY + ss / 2);
        p3 = new Vector2(centerX - size / 2, centerY + ss / 2);
        radius = ss;
        if (Math.abs(angle) > GeomUtil.EPSILON) {
            Transform2 trans = Transform2.createRotation(angle, center);
            trans.transformLocal(p1);
            trans.transformLocal(p2);
            trans.transformLocal(p3);
        }
        updateAABB();
    }

    public Triangle(Vector2 p1, Vector2 p2, Vector2 p3) {
        setPoints(p1, p2, p3);
    }

    public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        setPoints(x1, y1, x2, y2, x3, y3);
    }

    public void setPoints(float x1, float y1, float x2, float y2, float x3, float y3) {
        p1 = new Vector2(x1, y2);
        p2 = new Vector2(x2, y2);
        p3 = new Vector2(x3, y3);
        if (!GeomUtil.isCCW(p1, p2, p3)) {
            Vector2 temp = p1;
            p1 = p3;
            p3 = temp;
        }
        center = GeomUtil.getWeightedCenter(center, p1, p2, p3);
        radius = (float)Math.sqrt(GeomUtil.getMaxRaduisSquared(center, p1, p2, p3));
        updateAABB();
    }

    public void setPoints(Vector2 p1, Vector2 p2, Vector2 p3) {
        if (GeomUtil.isCCW(p1, p2, p3)) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        } else {
            this.p1 = p3;
            this.p2 = p2;
            this.p3 = p1;
        }
        center = GeomUtil.getWeightedCenter(center, p1, p2, p3);
        radius = (float)Math.sqrt(GeomUtil.getMaxRaduisSquared(center, p1, p2, p3));
        updateAABB();
    }

    private void updateAABB() {
        bottom = top = p1.y;
        left = right = p1.x;
        if (p2.y < top)
            top = p2.y;
        if (p3.y < top)
            top = p3.y;
        if (p2.y > bottom)
            bottom = p2.y;
        if (p3.y > bottom)
            bottom = p3.y;
        if (p2.x < left)
            left = p2.x;
        if (p3.x < left)
            left = p3.x;
        if (p2.x > right)
            right = p2.x;
        if (p3.x > right)
            right = p3.x;
    }

    @Override
    public List<Vector2> getPoints() {
        return Arrays.asList(p1, p2, p3);
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
        return GeomUtil.checkAABBPoint(this, point) &&
                point.distanceToSquared(center) <= (radius * radius) &&
                GeomUtil.containsPoint(point, p1, p2, p3);
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
            gl.glVertex2f(p1.x, p1.y);
            gl.glVertex2f(p2.x, p2.y);
            gl.glVertex2f(p3.x, p3.y);
        gl.glEnd();
    }

    @Override
    public void fill(GL2 gl) {
        gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex2f(p1.x, p1.y);
            gl.glVertex2f(p2.x, p2.y);
            gl.glVertex2f(p3.x, p3.y);
        gl.glEnd();
    }

    @Override
    public void draw(GL2 gl, iPointCallback pcb) {
        gl.glBegin(GL2.GL_LINE_LOOP);
            pcb.point(gl, p1);
            gl.glVertex2f(p1.x, p1.y);
            pcb.point(gl, p2);
            gl.glVertex2f(p2.x, p2.y);
            pcb.point(gl, p3);
            gl.glVertex2f(p3.x, p3.y);
        gl.glEnd();
    }

    @Override
    public void fill(GL2 gl, iPointCallback pcb) {
        gl.glBegin(GL2.GL_TRIANGLES);
            pcb.point(gl, p1);
            gl.glVertex2f(p1.x, p1.y);
            pcb.point(gl, p2);
            gl.glVertex2f(p2.x, p2.y);
            pcb.point(gl, p3);
            gl.glVertex2f(p3.x, p3.y);
        gl.glEnd();
    }

    public void setCenter(Vector2 center) {
        translate(center.subtract(this.center));
    }

    public void setCenter(float x, float y) {
        translate(x - center.x, y - center.y);
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
    public void rotate(float angle) {
        if (Math.abs(angle) > GeomUtil.EPSILON) {
            Transform2 trans = Transform2.createRotation(angle, center);
            trans.transformLocal(p1);
            trans.transformLocal(p2);
            trans.transformLocal(p3);
        }
        updateAABB();
    }

    @Override
    public void translate(float dx, float dy) {
        center.add(dx, dy);
        p1.add(dx, dy);
        p2.add(dx, dy);
        p3.add(dx, dy);
        right += dx;
        left += dx;
        top += dy;
        bottom += dy;
    }

    @Override
    public void translate(Vector2 dpos) {
        center.add(dpos);
        p1.add(dpos);
        p2.add(dpos);
        p3.add(dpos);
        right += dpos.x;
        left += dpos.x;
        top += dpos.y;
        bottom += dpos.y;
    }

    @Override
    public void transform(SimpleTransform2 trans) {
        trans.transformLocal(p1);
        trans.transformLocal(p2);
        trans.transformLocal(p3);
        trans.transformLocal(center);
        updateAABB();
    }

    @Override
    public void scale(float s) {
        Transform2 trans = Transform2.createTranslation(center).scaleLocal(s, s);
        trans.transformLocal(p1);
        trans.transformLocal(p2);
        trans.transformLocal(p3);
        updateAABB();
    }

    @Override
    public float getArea() {
        return Math.abs((p2.x * p1.y - p1.x * p2.y) + (p3.x * p2.y - p2.x * p3.y) + (p1.x * p3.y - p3.x * p1.y)) / 2;
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
