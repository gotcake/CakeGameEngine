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
import org.cake.game.collision.CollisionUtil;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;

/**
 *
 * @author Aaron
 */
public class Rectangle implements iConvex, iBoundingBox, iBoundingCircle{

    private float width, height, angle, radius, top, left, bottom, right;
    private Vector2 center, p1, p2, p3, p4;

    public Rectangle(Vector2 center, Vector2 size) {
        this(center, size.x, size.y, 0);
    }

    public Rectangle(Vector2 center, float width, float height) {
        this(center, width, height, 0);
    }

    public Rectangle(Vector2 center, Vector2 size, float angle) {
        this(center, size.x, size.y, angle);
    }

    public Rectangle(Vector2 center, float width, float height, float angle) {
        this.width = width;
        this.height = height;
        this.center = center;
        this.angle = angle;
        this.p1 = new Vector2();
        this.p2 = new Vector2();
        this.p3 = new Vector2();
        this.p4 = new Vector2();
        setPoints();
    }

    public Rectangle(float centerX, float centerY, float width, float height) {
        this(centerX, centerY, width, height, 0);
    }

    public Rectangle(float centerX, float centerY, float width, float height, float angle) {
        this.width = width;
        this.height = height;
        this.center = new Vector2(centerX, centerY);
        this.angle = angle;
        this.p1 = new Vector2();
        this.p2 = new Vector2();
        this.p3 = new Vector2();
        this.p4 = new Vector2();
        setPoints();
    }

    private void setPoints() {
        float w2 = width / 2;
        float h2 = height / 2;
        p1.set(center.x - w2, center.y - h2);
        p2.set(center.x + w2, center.y - h2);
        p3.set(center.x + w2, center.y + h2);
        p4.set(center.x - w2, center.y + h2);
        if (Math.abs(angle) > GeomUtil.EPSILON) {
            Transform2 t = Transform2.createRotation(angle, center);
            t.transformLocal(p1);
            t.transformLocal(p2);
            t.transformLocal(p3);
            t.transformLocal(p4);
        }
        updateBounds();
    }

    private void updateBounds() {
        radius = (float)Math.hypot(width/2, height/2);
        bottom = top = p1.y;
        left = right = p1.x;
        if (p2.y < top)
            top = p2.y;
        if (p3.y < top)
            top = p3.y;
        if (p4.y < top)
            top = p4.y;
        if (p2.y > bottom)
            bottom = p2.y;
        if (p3.y > bottom)
            bottom = p3.y;
        if (p4.y > bottom)
            bottom = p4.y;
        if (p2.x < left)
            left = p2.x;
        if (p3.x < left)
            left = p3.x;
        if (p4.x < left)
            left = p4.x;
        if (p2.x > right)
            right = p2.x;
        if (p3.x > right)
            right = p3.x;
        if (p4.x > right)
            right = p4.x;
    }

    @Override
    public List<Vector2> getPoints() {
        return Arrays.asList(p1, p2, p3, p4);
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
                GeomUtil.containsPoint(point, p1, p2, p3, p4);
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
            gl.glVertex2f(p4.x, p4.y);
        gl.glEnd();
    }

    @Override
    public void fill(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(p1.x, p1.y);
            gl.glVertex2f(p2.x, p2.y);
            gl.glVertex2f(p3.x, p3.y);
            gl.glVertex2f(p4.x, p4.y);
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
            pcb.point(gl, p4);
            gl.glVertex2f(p4.x, p4.y);
        gl.glEnd();
    }

    @Override
    public void fill(GL2 gl, iPointCallback pcb) {
        gl.glBegin(GL2.GL_QUADS);
            pcb.point(gl, p1);
            gl.glVertex2f(p1.x, p1.y);
            pcb.point(gl, p2);
            gl.glVertex2f(p2.x, p2.y);
            pcb.point(gl, p3);
            gl.glVertex2f(p3.x, p3.y);
            pcb.point(gl, p4);
            gl.glVertex2f(p4.x, p4.y);
        gl.glEnd();
    }

    public float getAngle() {
        return angle;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2 getSize() {
        return new Vector2(width, height);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        setPoints();
    }

    public void setSize(Vector2 size) {
        this.width = size.x;
        this.height = size.y;
        setPoints();
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
        this.angle = (this.angle + angle) % GeomUtil.PI2;
        setPoints();
    }

    @Override
    public void translate(float dx, float dy) {
        center.add(dx, dy);
        p1.add(dx, dy);
        p2.add(dx, dy);
        p3.add(dx, dy);
        p4.add(dx, dy);
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
        p4.add(dpos);
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
        trans.transformLocal(p4);
        trans.transformLocal(center);
        updateBounds();
    }

    @Override
    public void scale(float s) {
        width *= s;
        height *= s;
        setPoints();
    }

    @Override
    public float getArea() {
        return width * height;
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
