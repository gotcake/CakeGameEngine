/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;
import org.cake.game.exception.GameException;
import org.cake.game.geom.decomp.SimpleDecomposer;
import org.cake.game.geom.decomp.iDecomposer;
import org.cake.collections.SoftCache;

/**
 * A set of utility methods dealing with geometry
 * @author Aaron Cake
 */
public class GeomUtil {

    private static SoftCache<Thread, iDecomposer> decompCache = new SoftCache<>();

    private GeomUtil() {}

    /** The value Elipson that represents the limit of floating point operating precision. */
    public static final float EPSILON = computeElipson();


    /**  The float version of PI */
    public static final float PI = 3.1415927f;

    /** 2 PI constant */
    public static final float PI2 = 6.2831853f;

    /** PI/2 Constant */
    public static final float PIO2 = 1.5707963f;

    /** The value of 1/3 */
    private static final float INV_3 = 1.0f / 3.0f;

    /** The value of the inverse of the square root of 3; 1/sqrt(3) */
    private static final float INV_SQRT_3 = (float)(1.0 / Math.sqrt(3.0));

    private static float computeElipson() {
        float e = 0.5f;
        while (1.0f + e > 1.0f) {
                e *= 0.5f;
        }
        return e * 2.0f;
    }

    public static float epsilonOrder(int order) {
        return EPSILON * (int)Math.pow(2, order);
    }

    /**
     * Computes the average center for a set of points.
     * @param pts a list of points
     * @param dest a destination vector, or null
     * @return return the average center for the set of points
     */
    public static Vector2 getCenter(List<Vector2> pts, Vector2 dest) {
        if (pts.isEmpty())
            throw new GameException("Empty point list.");
        if (dest == null)
            dest = new Vector2();
        for (Vector2 pt: pts) {
            dest.add(pt);
        }
        return dest.divide(pts.size());
    }

    /**
     * Computes the weighted center for a simple convex contour
     * @param points a simple convex contour
     * @param dest a destination vector, or null
     * @return the area weighted center
     */
    public static Vector2 getWeightedCenter(List<Vector2> points, Vector2 dest) {
        if (points.isEmpty())
            throw new GameException("Empty point list.");
        int size = points.size();
        if (size == 1) {
            Vector2 p = points.get(0);
            return p.copy();
        }
        // otherwise perform the computation
        Vector2 center, p1, p2, temp = new Vector2();
        if (dest == null)
            center = new Vector2();
        else
            center = dest.set(0, 0);
        float area = 0;
        for (int i = 0; i < size; i++) {
                p1 = points.get(i);
                p2 = i + 1 < size ? points.get(i + 1) : points.get(0);
                float triangleArea = 0.5f * p1.cross(p2);
                // add it to the total area
                area += triangleArea;
                // area weighted centroid
                // (p1 + p2) * (D / 3)
                // = (x1 + x2) * (yi * x(i+1) - y(i+1) * xi) / 3
                // we will divide by the total area later
                center.add(temp.set(p1).add(p2).multiply(INV_3 * triangleArea));
        }
        // check for zero area
        if (Math.abs(area) <= EPSILON) {
                // zero area can only happen if all the points are the same point
                // in which case just return a copy of the first
                return points.get(0).copy();
        }
        // finish the centroid calculation by dividing by the total area
        return center.divide(area);
    }

    public static Vector2 getWeightedCenter(Vector2 dest, Vector2... points) {
        if (points.length == 0)
            throw new GameException("Empty point list.");
        int size = points.length;
        if (size == 1) {
            Vector2 p = points[0];
            return p.copy();
        }
        // otherwise perform the computation
        Vector2 center, p1, p2, temp = new Vector2();
        if (dest == null)
            center = new Vector2();
        else
            center = dest.set(0, 0);
        float area = 0;
        for (int i = 0; i < size; i++) {
                p1 = points[i];
                p2 = i + 1 < size ? points[i + 1] : points[0];
                float triangleArea = 0.5f * p1.cross(p2);
                // add it to the total area
                area += triangleArea;
                // area weighted centroid
                // (p1 + p2) * (D / 3)
                // = (x1 + x2) * (yi * x(i+1) - y(i+1) * xi) / 3
                // we will divide by the total area later
                center.add(temp.set(p1).add(p2).multiply(INV_3 * triangleArea));
        }
        // check for zero area
        if (Math.abs(area) <= EPSILON) {
                // zero area can only happen if all the points are the same point
                // in which case just return a copy of the first
                return points[0].copy();
        }
        // finish the centroid calculation by dividing by the total area
        return center.divide(area);
    }

    /**
     * Calculates the max radius squared from the average center of a set of points.
     * @param pts a set of points
     * @return the max radius squared from the average center
     */
    public static float getMaxRaduisSquared(List<Vector2> pts) {
        return getMaxRaduisSquared(pts, getCenter(pts, null));
    }

    /**
     * Calculates the max radius squared of a set of points from a given center
     * @param pts a set of points
     * @param center the center
     * @return the max radius squared
     */
    public static float getMaxRaduisSquared(List<Vector2> pts, Vector2 center) {
        float max = 0, r;
        for (Vector2 pt: pts) {
            r = center.distanceToSquared(pt);
            if (r > max)
                max = r;
        }
        return max;
    }

    public static float getMaxRaduisSquared(Vector2 center, Vector2... pts) {
        float max = 0, r;
        for (Vector2 pt: pts) {
            r = center.distanceToSquared(pt);
            if (r > max)
                max = r;
        }
        return max;
    }
    /*
     bool pointInPolygon() {
        int i, j=polySides-1;
        boolean inside=false;

        for (i=0; i<polySides; i++) {
            if ((polyY[i]< y && polyY[j]>=y
            ||   polyY[j]< y && polyY[i]>=y)
            &&  (polyX[i]<=x || polyX[j]<=x)) {
            if (polyX[i]+(y-polyY[i])/(polyY[j]-polyY[i])*(polyX[j]-polyX[i])<x) {
                inside=!inside; }}
            j=i; }
        return inside;
     }
     */
    /**
     * Uses the point-in-shape algorithm with even-odd winding to determine if a shape contains a point.
     * @param contour a single contour
     * @param pt some point to check
     * @return true if the point is in the shape, false if it is outside
     */
    public static boolean containsPoint(List<Vector2> contour, Vector2 pt) {
        boolean inside = false;
        int size = contour.size();
        Vector2 pi, pj = contour.get(size - 1);
        for (int i=0; i<size; i++) {
            pi = contour.get(i);
            if (((pi.y < pt.y && pj.y >= pt.y)
                    || (pj.y < pt.y && pi.y >= pt.y))
                    && (pi.x <= pt.x || pj.x < pt.x)) {
                if (pi.x + (pt.y - pi.y) / (pj.y-pi.y) * (pj.x - pi.x) < pt.x) {
                    inside = !inside;
                }
            }
            pj = pi;
        }
        return inside;
    }
    
    public static boolean contoursContainPoint(List<iContour> contours, Vector2 pt) {
        boolean inside = false;
        for (iContour c: contours) {
            if (containsPoint(c.getPoints(), pt))
                inside = !inside;
        }
        return inside;
    }

    public static boolean containsPoint(Vector2 pt, Vector2... contour) {
        boolean inside = false;
        int size = contour.length;
        Vector2 pi, pj = contour[size - 1];
        for (int i=0; i<size; i++) {
            pi = contour[i];
            if (((pi.y < pt.y && pj.y >= pt.y)
                    || (pj.y < pt.y && pi.y >= pt.y))
                    && (pi.x <= pt.x || pj.x < pt.x)) {
                if (pi.x + (pt.y - pi.y) / (pj.y-pi.y) * (pj.x - pi.x) < pt.x) {
                    inside = !inside;
                }
            }
            pj = pi;
        }
        return inside;
    }

    /**
     * A narrow-phase check for intersection between two shapes.
     * First checks for intersection between each line, then looks to see if any point lies in any other shape.
     * @param b a shape
     * @param a another shape
     * @return
     */
    public static boolean intersects(iConvex convexA, iConvex convexB) {
        List<Vector2> a = convexA.getPoints();
        List<Vector2> b = convexB.getPoints();
        int sizea = a.size(), sizeb = b.size();
        Vector2 a1 = a.get(sizea - 1), a2, b1, b2;
        for (int ia = 0; ia < sizea; ia++) {
            a2 = a.get(ia);
            b1 = b.get(sizeb - 1);
            for (int ib = 0; ib < sizeb; ib++) {
                b2 = b.get(ib);
                if (Line2.intersects(a1.x, a1.y, a2.x, a2.y, b1.x, b1.y, b2.x, b2.y, false))
                    return true;
                b1 = b2;
            }
            a1 = a2;
        }
        for (int i = 0; i<sizea; i++) {
            if (convexB.containsPoint(a.get(i)))
                return true;
        }
        for (int i = 0; i<sizeb; i++) {
            if (convexA.containsPoint(b.get(i)))
                return true;
        }
        return false;
    }

    public static boolean intersects(iShape shapeA, iShape shapeB) {
        if (shapeA instanceof iConvex && shapeB instanceof iConvex)
            return intersects((iConvex)shapeA, (iConvex)shapeB);
        for (iContour contour: shapeA.getContours())
            for (Vector2 pt: contour.getPoints())
                if (shapeB.containsPoint(pt))
                    return true;
        for (iContour contour: shapeB.getContours())
            for (Vector2 pt: contour.getPoints())
                if (shapeA.containsPoint(pt))
                    return true;
        for (iContour ca: shapeA.getContours()) {
            List<Vector2> a = ca.getPoints();
            int sizea = a.size();
            for (iContour cb: shapeB.getContours()) {
                List<Vector2> b = cb.getPoints();
                int sizeb = b.size();
                Vector2 a1 = a.get(sizea - 1), a2, b1, b2;
                for (int ia = 0; ia < sizea; ia++) {
                    a2 = a.get(ia);
                    b1 = b.get(sizeb - 1);
                    for (int ib = 0; ib < sizeb; ib++) {
                        b2 = b.get(ib);
                        if (Line2.intersects(a1.x, a1.y, a2.x, a2.y, b1.x, b1.y, b2.x, b2.y, false))
                            return true;
                        b1 = b2;
                    }
                    a1 = a2;
                }
            }
         }
         return false;
    }

    /**
     * Checks to see if a contour is a simple convex shape by computing the sum of the angles.
     * @param pts a single contour
     * @return true if it simple convex, false if not
     */
    public static boolean isSimpleConvex(List<Vector2> pts) {
        int size = pts.size();
        float angle = 0;
        float target = (size - 2) * PI;
        Vector2 p1, p2, p3, v1 = new Vector2(), v2 = new Vector2();
        for (int i=0, j=size-1, k=size-2; i<size; k=j, j=i++) {
            p1 = pts.get(k);
            p2 = pts.get(j);
            p3 = pts.get(i);
            v1.set(p1).vectorToLocal(p2);
            v2.set(p2).vectorToLocal(p3);
            angle += v1.angleBewteen(v2);
        }
        return Math.abs(angle - target) < EPSILON;
    }

    /**
     * Checks to see if a contour has a counter-clockwise winding by computing
     * the sum of the cross-products of each point with the next point.
     * @param pts a single contour
     * @return true if it is CCW, false otherwise
     */
    public static boolean isCCW(List<Vector2> pts) {
        float area = 0;
        Vector2 p1, p2;
        int size = pts.size();
        for (int i=0, j=size-1; i<size; j=i++) {
            p1 = pts.get(j);
            p2 = pts.get(i);
            area += p1.cross(p2);
        }
        return area > 0;
    }

    public static boolean isCCW(Vector2... pts) {
        float area = 0;
        Vector2 p1, p2;
        int size = pts.length;
        for (int i=0, j=size-1; i<size; j=i++) {
            p1 = pts[j];
            p2 = pts[i];
            area += p1.cross(p2);
        }
        return area > 0;
    }

    public static List<Vector2> ensureCCWW(List<Vector2> contour) {
        if (!isCCW(contour))
            Collections.reverse(contour);
        return contour;
    }

    public static List<Line2> getLinesFromContour(List<Vector2> contour) {
        List<Line2> lines = new ArrayList<>();
        int size = contour.size();
        for (int i=0, j = size - 1; i<size; j = i++) {
            lines.add(new Line2(contour.get(j), contour.get(i)));
        }
        return lines;
    }

    public static boolean isSelfIntersecting(List<Vector2> contour) {
        int size = contour.size();
        if (size < 3) return false;
        Vector2 p1 = contour.get(size - 1), p2, p3, p4;
        for (int i=0; i<size; i++) {
            p3 = p2 = contour.get(i);
            for (int j=i+1; j<size; j++) {
                p4 = contour.get(j);
                if (Line2.intersects(p1, p2, p3, p4, false))
                    return true;
                p3 = p4;
            }
            p1 = p2;
        }
        return false;
    }

    public static Decomposition decomposeSingle(List<Vector2> contour) {
        iDecomposer d = decompCache.get(Thread.currentThread());
        if (d == null) {
            d = new SimpleDecomposer();
            decompCache.put(Thread.currentThread(), d);
        }
        return d.decomposeSingle(contour);
    }

    public static Decomposition decompose(List<iContour> contours) {
        iDecomposer d = decompCache.get(Thread.currentThread());
        if (d == null) {
            d = new SimpleDecomposer();
            decompCache.put(Thread.currentThread(), d);
        }
        return d.decomposeMulti(contours);
    }

    public static void removeRedundantPoints(List<Vector2> contour) {
        Vector2 a, b = contour.get(contour.size() - 1);
        for (int i=0; i<contour.size();) {
            a = contour.get(i);
            if (a.equals(b)) {
                contour.remove(i);
            } else {
                i++;
                b = a;
            }
        }
    }

    public static boolean checkAABBs(iBoundingBox a1, iBoundingBox a2) {
        float top1 = a1.getMinY(), top2 = a2.getMinY(), left1 = a1.getMinX(), left2 = a2.getMinX(),
                bottom1 = a1.getMaxY(), bottom2 = a2.getMaxY(), right1 = a1.getMaxX(), right2 = a2.getMaxX();

        if (top2 >= top1) { // top of other rectangle is below the top of this one
            if (top2 <= bottom1) { // top of other rectangle is in range
                if (left2 >= left1) { // left of other rectangle is to the right of the left of this one
                    if (left2 <= right1) { // the left of the other rectangle is in range
                        return true;
                    } else { // the other rectangle is to the right of this rectangle
                        return false;
                    }
                } else if (right2 >= left1) { // the right of the other rectangle is to the right of the left of this one
                    return true;
                } else { // the other rectangle is to the left of this one
                    return false;
                }
            } else { // rectangle is below this one
                return false;
            }
        } else if (bottom2 >= top1) { // the bottom of the other rectangle is below the top of this one
            if (bottom2 <= bottom1) { // the bottom of the other rectangle is in range
                if (left2 >= left1) { // left of other rectangle is to the right of the left of this one
                    if (left2 <= right1) { // the left of the other rectangle is in range
                        return true;
                    } else { // the other rectangle is to the right of this rectangle
                        return false;
                    }
                } else if (right2 >= left1) { // the right of the other rectangle is to the right of the left of this one
                    return true;
                } else { // the other rectangle is to the left of this one
                    return false;
                }
            } else { // this rectangle is completely in range of the other rectangle
                if (left1 >= left2) { // left of this rectangle is to the right of the left of the other one
                    if (left1 <= right2) { // the left of the this rectangle is in range
                        return true;
                    } else { // this rectangle is to the right of the other rectangle
                        return false;
                    }
                } else if (right1 >= left2) { // the right of this rectangle is to the right of the left of the other one
                    return true;
                } else { // this rectangle is to the left of the other one
                    return false;
                }
            }
        } else { // rectangle is above this one
            return false;
        }
    }

    public static boolean checkBoundingCircles(iBoundingCircle bc1, iBoundingCircle bc2) {
        return bc1.getCenter().distanceTo(bc2.getCenter()) <= (bc1.getRadius() + bc2.getRadius());
    }

    public static boolean checkAABBPoint(iBoundingBox a, Vector2 point) {
        return point.x >= a.getMinX() && point.x <= a.getMaxX() &&
                point.y >= a.getMinY() && point.y <= a.getMaxY();
    }

    public static boolean checkBoundingCirclePoint(iBoundingCircle bc, Vector2 point) {
        return bc.getCenter().distanceToSquared(point) <= bc.getRadiusSquared();
    }
    
    public static Shape javaShapeToGameShape(java.awt.Shape shape, int curvePoints) {
        PathIterator pathIterator = shape.getPathIterator(new AffineTransform());
        float[] pt = new float[6];
        Shape s = new Shape();
        List<Vector2> bezCurve;
        Vector2 last = null;
        while (!pathIterator.isDone()) {
            int type = pathIterator.currentSegment(pt);
            switch (type) {
                case PathIterator.SEG_CLOSE:
                    s.closeContour();
                    break;
                case PathIterator.SEG_LINETO:
                    last = new Vector2(pt[0], pt[1]);
                    s.addPoint(last);
                    break;
                case PathIterator.SEG_MOVETO:
                    s.newContour();
                    last = new Vector2(pt[0], pt[1]);
                    s.addPoint(last);
                    break;
                case PathIterator.SEG_QUADTO:
                    bezCurve = new ArrayList();
                    bezCurve.add(last);
                    bezCurve.add(new Vector2(pt[0], pt[1]));
                    last = new Vector2(pt[2], pt[3]);
                    bezCurve.add(last);
                    s.addPoints(generateBezierCurve(bezCurve, 3));
                    break;
                case PathIterator.SEG_CUBICTO:
                    bezCurve = new ArrayList();
                    bezCurve.add(last);
                    bezCurve.add(new Vector2(pt[0], pt[1]));
                    bezCurve.add(new Vector2(pt[2], pt[3]));
                    last = new Vector2(pt[4], pt[5]);
                    bezCurve.add(last);
                    s.addPoints(generateBezierCurve(bezCurve, 4));
                    break;
            }
            pathIterator.next();
        }
        return s;
    }
    
    public static List<Vector2> generateBezierCurve(List<Vector2> points, int count) {
        List<Vector2> pts = new ArrayList();
        List<Vector2> dummy = new ArrayList();
        for (int i=1; i<=count; i++) {
            dummy.addAll(points);
            Vector2 pt = bezierInterpolate(dummy, (float)i / count);
            pts.add(pt);
            dummy.clear();
        }
        return pts;
    }
    
    private static Vector2 bezierInterpolate(List<Vector2> pts, float t) {
        while (pts.size() > 1) {
            for (int i=0; i<pts.size() - 1; i++) {
                pts.set(i, pts.get(i).interpolateTo(pts.get(i + 1), t));
            }
            pts.remove(pts.size() - 1);
        }
        return pts.get(0);
        
    }
}
