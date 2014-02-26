/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.Pair;
import org.cake.game.collision.BoundingBox;
import org.cake.game.collision.BoundingCircle;
import org.cake.game.collision.CollisionUtil;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;
import org.cake.game.geom.decomp.SimpleDecomposer;
import org.cake.game.geom.decomp.iDecomposer;

/**
 *
 * @author Aaron
 */
public class Shape implements iShape, iBoundingCircle {

    private List<iContour> contours;
    private Contour current;
    private Decomposition decomp;
    private BoundingBox bounds;
    private Pair<Vector2, Float> centerAndArea;
    private Float radius;
    
    public Shape() {
        contours = new ArrayList();
    }
    
    public Shape(List<Vector2> points, boolean closed) {
        contours = new ArrayList();
        addPoints(points);
        if (closed)
            closeContour();
    }
    
    public Decomposition getDecomposition() {
        if (decomp == null)
            decomp = GeomUtil.decompose(contours);
        return decomp;
    }
    
    public void addPoint(Vector2 pt) {
        if (current == null) {
            current = new Contour();
            contours.add(current);
        }
        current.points.add(pt);
    }
    
    public void newContour() {
        current = null;
    }
    
    public void addPoints(List<Vector2> pts) {
        if (current == null) {
            current = new Contour();
            contours.add(current);
        }
        for (Vector2 pt: pts)
            current.points.add(pt);
    }
    
    public void addPoint(float x, float y) {
        addPoint(new Vector2(x, y));
    }
    
    public void closeContour() {
        current.closed = true;
        current = null;
    }

    @Override
    public iBoundingBox getAABB() {
        if (bounds == null) {
            bounds = new BoundingBox();
            for (iContour c: contours)
                for (Vector2 pt: c.getPoints())
                    bounds.addPoint(pt);
        }
        return bounds;
    }

    @Override
    public iBoundingCircle getBoundingCircle() {
        return this;
    }

    @Override
    public boolean containsPoint(Vector2 point) {
        return CollisionUtil.checkPointInShape(this, point);
    }

    @Override
    public boolean intersects(iShape other) {
        return CollisionUtil.checkCollision(this, other);
    }

    @Override
    public void draw(GL2 gl) {
        for (iContour contour: this.contours) {
            gl.glBegin(GL2.GL_LINE_LOOP);
            for (Vector2 pt: contour.getPoints()) {
                pt.glVertex(gl);
            }
            gl.glEnd();
        }
    }

    @Override
    public void fill(GL2 gl) {
       getDecomposition().fill(gl);
    }

    @Override
    public void draw(GL2 gl, iPointCallback pcb) {
        for (iContour contour: this.contours) {
            gl.glBegin(GL.GL_LINE_LOOP);
            for (Vector2 pt: contour.getPoints()) {
                pcb.point(gl, pt);
                pt.glVertex(gl);
            }
            gl.glEnd();
        }
    }

    @Override
    public void fill(GL2 gl, iPointCallback pcb) {
        getDecomposition().fill(gl, pcb);
    }

    @Override
    public void translate(float dx, float dy) {
        for (iContour contour: contours) {
            for (Vector2 pt: contour.getPoints()) {
                pt.add(dx, dy);
            }
        }
        if (decomp != null)
            decomp.translate(dx, dy);
        if (centerAndArea != null)
            centerAndArea.a.add(dx, dy);
        if (bounds != null)
            bounds.set(bounds.left + dx, bounds.top + dy, bounds.right + dx, bounds.bottom + dy);
    }

    @Override
    public void translate(Vector2 dpos) {
        translate(dpos.x, dpos.y);
    }

    @Override
    public void rotate(float angle) {
        Transform2 trans = Transform2.createRotation(angle, getCenter());
        for (iContour contour: contours) {
            trans.transformLocal(contour.getPoints());
        }
        if (decomp != null)
            decomp.transform(trans);
        bounds = null;
    }

    @Override
    public void transform(SimpleTransform2 trans) {
        for (iContour contour: contours) {
            trans.transformLocal(contour.getPoints());
        }
        if (decomp != null)
            decomp.transform(trans);
        bounds = null;
        trans.transformLocal(centerAndArea.a);
    }
   
    public void transform(iTransform2 trans) {
        for (iContour contour: contours) {
            trans.transformLocal(contour.getPoints());
        }
        if (decomp != null)
            decomp.transform(trans);
        bounds = null;
        centerAndArea = null;
        radius = null;
    }

    @Override
    public void scale(float s) {
        Transform2 trans = Transform2.createTranslation(getCenter().inverse());
        trans.scale(s, s);
        trans.translate(centerAndArea.a);
        transform(trans);
    }

    @Override
    public List<iContour> getContours() {
        return contours;
    }

    @Override
    public Vector2 getCenter() {
        if (centerAndArea == null)
            centerAndArea = getDecomposition().calculateCenterOfMassAndArea();
        return centerAndArea.a;
    }

    @Override
    public float getRadius() {
        if (radius == null) {
            radius = 0f;
            for (iContour c: contours)
                radius = Math.max(radius, GeomUtil.getMaxRaduisSquared(c.getPoints(), getCenter()));
            radius = (float)Math.sqrt(radius);
        }
        return radius;
    }

    @Override
    public float getRadiusSquared() {
        return getRadius() * radius;
    }
    
    private static class Contour implements iContour {
        
         List<Vector2> points;
         boolean closed;
         
         Contour() {
             closed = false;
             points = new ArrayList();
         }

        @Override
        public List<Vector2> getPoints() {
            return points;
        }

        @Override
        public boolean isClosed() {
            return closed;
        }
        
    }
}
