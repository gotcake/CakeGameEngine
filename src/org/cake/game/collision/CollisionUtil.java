/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import java.util.List;
import org.cake.game.FloatRange;
import org.cake.game.geom.*;

/**
 *
 * @author Aaron
 */
public class CollisionUtil {

    /*public static boolean hasSeparatingAxis(iShape a, iShape b) {
        List<Vector2> la;
        for (iConvex ca: a.getDecomposition()) {
            la = ca.getPoints();
            for (iConvex cb: b.getDecomposition()) {
                if (!hasSeparatingAxis(la, cb.getPoints()))
                    return false;
            }
        }
        return true;
    }

    public static boolean hasSeparatingAxis(List<Vector2> convexA, List<Vector2> convexB) {
        int sizea = convexA.size();
        int sizeb = convexB.size();
        Vector2 last = convexA.get(sizea - 1), cur;
        for (int i=0; i<sizea; i++) {
            cur = convexA.get(i);
            Vector2 axis = last.vectorTo(cur).leftOrthagonalLocal();
            FloatRange r1 = projectAxis(axis, convexA);
            FloatRange r2 = projectAxis(axis, convexB);
            if (!r1.intersects(r2))
                return true;
            last = cur;
        }
        last = convexB.get(sizeb - 1);
        for (int i=0; i<sizeb; i++) {
            cur = convexB.get(i);
            Vector2 axis = last.vectorTo(cur).leftOrthagonalLocal();
            FloatRange r1 = projectAxis(axis, convexA);
            FloatRange r2 = projectAxis(axis, convexB);
            if (!r1.intersects(r2))
                return true;
            last = cur;
        }
        return false;
    }

    public static FloatRange projectAxis(Vector2 axis, List<Vector2> convex) {
        FloatRange r = new FloatRange(axis.dot(convex.get(0)));
        int size = convex.size();
        for (int i=1; i<size; i++) {
            r.include(axis.dot(convex.get(i)));
        }
        return r;
    }*/


    public static ConvexCollisionResult getCollisionResult(List<Vector2> convexA, List<Vector2> convexB) {
        return null;
    }
    
    public static boolean checkCollision(iShape a, iShape b) {
        return GeomUtil.checkAABBs(a.getAABB(), b.getAABB()) && GeomUtil.checkBoundingCircles(a.getBoundingCircle(), b.getBoundingCircle()) && GeomUtil.intersects(a, b);
    }
    
    public static boolean checkPointInShape(iShape s, Vector2 pt) {
        return GeomUtil.checkAABBPoint(s.getAABB(), pt) && GeomUtil.checkBoundingCirclePoint(s.getBoundingCircle(), pt) && GeomUtil.contoursContainPoint(s.getContours(), pt);
    }

}
