/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.io.Serializable;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.io.objectxml.ObjectXMLNode;
import org.cake.game.io.objectxml.iObjectXMLSerializer;

/**
 *
 * @author Aaron Cake
 */
public class Line2 implements Serializable {

    static {
        ObjectXML.registerParser(Line2.class, new iObjectXMLSerializer<Line2>() {
            @Override
            public Line2 deserialize(ObjectXMLNode node) {
                return new Line2(node.getAttrNumber("x1").floatValue(),
                        node.getAttrNumber("y1").floatValue(),
                        node.getAttrNumber("x2").floatValue(),
                        node.getAttrNumber("y2").floatValue());
            }

            @Override
            public boolean serialize(ObjectXMLNode node, Line2 obj) {
                node.setAttr("x1", obj.x1);
                node.setAttr("y1", obj.y1);
                node.setAttr("x2", obj.x2);
                node.setAttr("y2", obj.y2);
                return true;
            }
        });
    }

    public static Vector2 interpolate(float x1, float y1, float x2, float y2, float t) {
        return new Vector2(x1 + (x2 - x1) * t, y1 + (y2 - y1) * t);
    }

    public static float projectPoint(float x1, float y1, float x2, float y2, float px, float py) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return ((y1 - py) * (y1 - y2) - (x1 - px) * (x2 - x1)) / (dx * dx + dy * dy);
    }

    private static boolean relativeInterior(float x1, float y1, float x2, float y2, float px, float py, boolean includeEndpoints) {
        if (x1 == x2 && y1 == y2) { // same point, also avoid division by 0
            return x1 == px && y1 == py;
        }
        float dx = x1 - x2;
        float dy = y1 - y2;
        float r = ((y1 - py) * (y1 - y2) - (x1 - px) * (x2 - x1)) / (dx * dx + dy * dy);
        if (includeEndpoints)
            return r >= 0 && r <= 1;
        else
            return r > 0 && r < 1;
    }

    public static boolean intersects(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, boolean includeEndpoints) {
        float denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        float numa = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        float numb = (x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3);
        if (denom == 0) { // parallel
            if (numa == 0 && numb == 0) { // coincident
                return relativeInterior(x1, y1, x2, y2, x3, y3, includeEndpoints)
                        || relativeInterior(x1, y1, x2, y2, x4, y4, includeEndpoints)
                        || relativeInterior(x3, y3, x4, y4, x1, y1, includeEndpoints)
                        || relativeInterior(x3, y3, x4, y4, x2, y2, includeEndpoints);
            }
            return false; // not conicident, no intersection
        }
        float ua = numa/denom;
        float ub = numb/denom;
        if (includeEndpoints) {
            if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) { // intersection is on both line segments
                return true;
            }
            return false;
        } else {
            if (ua > 0 && ua < 1 && ub > 0 && ub < 1) { // intersection is on both line segments
                return true;
            }
            return false;
        }
    }

    public static boolean intersects(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, boolean includeEndpoints) {
        return intersects(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, includeEndpoints);
    }

    public static Vector2 intersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, boolean includeEndpoints) {
        float denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        float numa = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        float numb = (x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3);
        if (denom == 0) { // parallel
            if (numa == 0 && numb == 0) { // coincident
                if (relativeInterior(x1, y1, x2, y2, x3, y3, includeEndpoints)) {
                    if (relativeInterior(x3, y3, x4, y4, x1, y1, includeEndpoints)) {
                        return new Vector2( (x3 + x1) / 2, (y3 + y1) / 2 );
                    } else {
                        return new Vector2( (x3 + x2) / 2, (y3 + y2) / 2 );
                    }
                } else if (relativeInterior(x1, y1, x2, y2, x4, y4, includeEndpoints)) {
                    if (relativeInterior(x3, y3, x4, y4, x1, y1, includeEndpoints)) {
                        return new Vector2( (x4 + x1) / 2, (y4 + y1) / 2 );
                    } else {
                        return new Vector2( (x4 + x2) / 2, (y4 + y2) / 2 );
                    }
                }
                return null;
            }
            return null; // not conicident, no intersection
        }
        float ua = numa/denom;
        float ub = numb/denom;
        if (includeEndpoints) {
            if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) { // intersection is on both line segments
                return new Vector2( x1 + ua * (x2 - x1), y1 + ua * (y2 - y1) );
            }
        } else {
            if (ua > 0 && ua < 1 && ub > 0 && ub < 1) { // intersection is on both line segments
                return new Vector2( x1 + ua * (x2 - x1), y1 + ua * (y2 - y1) );
            }
        }
        return null;
    }

    public static Vector2 intersection(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, boolean includeEndpoints) {
        return intersection(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, includeEndpoints);
    }

    public float x1, y1, x2, y2;

    public Line2() {
        x1 = y1 = x2 = y2 = 0;
    }

    public Line2(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Line2(Vector2 p1, Vector2 p2) {
        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
    }

    public Vector2 getP1() {
        return new Vector2(x1, y1);
    }

    public Vector2 getP1(Vector2 dest) {
        return dest.set(x1, y1);
    }

    public Vector2 getP2() {
        return new Vector2(x2, y2);
    }

    public Vector2 getP2(Vector2 dest) {
        return dest.set(x2, y2);
    }

    public Vector2 getP1ToP2() {
        return new Vector2(x2 - x1, y2 - y1);
    }

    public Vector2 getP2ToP1() {
        return new Vector2(x1 - x2, y1 - y2);
    }

    public Vector2 getP1ToP2(Vector2 dest) {
        return dest.set(x2 - x1, y2 - y1);
    }

    public Vector2 getP2ToP1(Vector2 dest) {
        return dest.set(x1 - x2, y1 - y2);
    }

    public Vector2 interpolate(float t) {
        return new Vector2(x1 + (x2 - x1) * t, y1 + (y2 - y1) * t);
    }

    public float length() {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    public float lengthSquared() {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public Line2 set(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        return this;
    }

    public Line2 set(Vector2 p1, Vector2 p2) {
        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
        return this;
    }

    public Line2 setP1(float x, float y) {
        x1 = x;
        y1 = y;
        return this;
    }

    public Line2 setP1(Vector2 p) {
        x1 = p.x;
        y1 = p.y;
        return this;
    }

    public Line2 setP2(float x, float y) {
        x2 = x;
        y2 = y;
        return this;
    }

    public Line2 setP2(Vector2 p) {
        x2 = p.x;
        y2 = p.y;
        return this;
    }

    public float projectPoint(float x, float y) {
        return projectPoint(x1, y1, x2, y2, x, y);
    }

    public float projectPoint(Vector2 pt) {
        return projectPoint(x1, y1, x2, y2, pt.x, pt.y);
    }

    public boolean intersects(Line2 other) {
        return intersects(x1, y1, x2, y2, other.x1, other.y1, other.x2, other.y2, true);
    }

    public Vector2 intersection(Line2 other) {
        return intersection(x1, y1, x2, y2, other.x1, other.y1, other.x2, other.y2, true);
    }

    public Vector2 intersection(Line2 other, boolean includeEndpoints) {
        return intersection(x1, y1, x2, y2, other.x1, other.y1, other.x2, other.y2, includeEndpoints);
    }

    public boolean intersects(Line2 other, boolean includeEndpoints) {
        return intersects(x1, y1, x2, y2, other.x1, other.y1, other.x2, other.y2, includeEndpoints);
    }

    public Line2 reverse() {
        return new Line2(x2, y2, x1, y1);
    }

    public float slope() {
        return (y1 - y2) / (x1 - x2);
    }

    public float minX() {
        return (x1 < x2 ? x1 : x2);
    }

    public float maxX() {
        return (x1 > x2 ? x1 : x2);
    }

    public float minY() {
        return (y1 < y2 ? y1 : y2);
    }

    public float maxY() {
        return (y1 > y2 ? y1 : y2);
    }

    public Line2 reverseLocal() {
        float temp = x1;
        x1 = x2;
        x2 = temp;
        temp = y1;
        y1 = y2;
        y2 = temp;
        return this;
    }

    public Line2 copy() {
        return new Line2(x1, y1, x2, y2);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Float.floatToIntBits(this.x1);
        hash = 97 * hash + Float.floatToIntBits(this.y1);
        hash = 97 * hash + Float.floatToIntBits(this.x2);
        hash = 97 * hash + Float.floatToIntBits(this.y2);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Line2))
            return false;
        Line2 l = (Line2)other;
        return l.x1 == x1 && l.y1 == y1
                && l.x2 == x2 && l.y2 == y2;
    }

    @Override
    public String toString() {
        return "Line2[(" + x1 + ',' + y1 + "),(" + x2 + ',' + y2 + ")]";
    }

}
