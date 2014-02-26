/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import java.util.List;
import org.cake.game.geom.GeomUtil;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iTranslateable2;

/**
 * A basic Bounding Circle implementation
 * @author Aaron Cake
 */
public class BoundingCircle implements iBoundingCircle, iTranslateable2 {

    private float radius;
    private Vector2 center;

    public BoundingCircle() {
        radius = 0;
    }

    public BoundingCircle(Vector2 center) {
        radius = 0;
        this.center = center;
    }

    public BoundingCircle(List<Vector2> points) {
        set(points);
    }

    public BoundingCircle(Vector2 center, float radius) {
        this.radius = radius;
        this.center = center;
    }

    public void set(List<Vector2> points) {
        if (points.size() < 1) return;
        center = GeomUtil.getWeightedCenter(points, center);
        radius = (float)Math.sqrt(GeomUtil.getMaxRaduisSquared(points, center));
    }

    public void set(Vector2 center, float radius) {
        this.radius = radius;
        this.center = center;
    }

    public void addPoint(Vector2 pt) {
        if (center == null) {
            center = pt;
            radius = 0;
        } else {
            float distSquared = center.distanceToSquared(pt);
            if (distSquared > radius * radius) {
                radius = (float)Math.sqrt(radius);
            }
        }

    }

    public void addPoint(float x, float y) {
        if (center == null) {
            center = new Vector2(x, y);
            radius = 0;
        }else {
            float distSquared = center.distanceToSquared(x, y);
            if (distSquared > radius * radius) {
                radius = (float)Math.sqrt(radius);
            }
        }
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public void setCenter(float x, float y) {
        center = new Vector2(x, y);
    }

    @Override
    public void translate(Vector2 dpos) {
        center.add(dpos);
    }

    @Override
    public void translate(float dx, float dy) {
        center.add(dx, dy);
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

}
