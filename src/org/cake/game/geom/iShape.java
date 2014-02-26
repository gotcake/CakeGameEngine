/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.List;
import javax.media.opengl.GL2;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;
import org.cake.game.scene.iEntity;

/**
 *
 * @author Aaron Cake
 */
public interface iShape extends iTranslateable2, iRotatable2, iSimpleTransformable, iScalable1 {

    /**
     * Gets all the points contained in the shape.
     * @return the points in this shape
     */
    public List<iContour> getContours();

    /**
     * Gets the axis-aligned bounding box for this shape.
     * @return the AABB
     */
    public iBoundingBox getAABB();

    public iBoundingCircle getBoundingCircle();
    
    public Vector2 getCenter();

    /**
     * Check to see if the given point resides in this shape.
     * @param point
     * @return
     */
    public boolean containsPoint(Vector2 point);
    /**
     * Checks to see if this shape intersects another shape.
     * @param other another shape
     * @return true if they intersect
     */
    public boolean intersects(iShape other);

    /**
     * Draws the contours of this shape.
     * @param gl the GL2 context.
     */
    public void draw(GL2 gl);
    /**
     * Draws this shape, filled.
     * @param gl the GL2 context.
     */
    public void fill(GL2 gl);

    /**
     * Draws the contours of this shape with a point callback being called for each vertex.
     * @param gl the GL2 context
     * @param pcb a point callback
     */
    public void draw(GL2 gl, iPointCallback pcb);

    /**
     * Draws this shape, filled, with a point callback being called for each vertex.
     * @param gl the GL2 context
     * @param pcb a point callback
     */
    public void fill(GL2 gl, iPointCallback pcb);

}
