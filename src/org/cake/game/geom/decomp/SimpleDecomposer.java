/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom.decomp;

import java.util.ArrayList;
import java.util.List;
import org.cake.game.geom.*;

/**
 *
 * @author Aaron
 */
public class SimpleDecomposer implements iDecomposer {

    private iDecomposer fallback = new GLUTessDecomposer();

    @Override
    public  Decomposition decomposeMulti(List<iContour> contours) {
        return fallback.decomposeMulti(contours);
    }

    @Override
    public Decomposition decomposeSingle(List<Vector2> contour) {
        int size = contour.size();
        if (size < 3) {
            return new Decomposition(); // too small return empty decomposition
        }

        // If the number of points reaches a threshold (90) or the
        // contour is not simple, use the fallback decomposer.
        if (size > 90 || GeomUtil.isSelfIntersecting(contour)) {
            return fallback.decomposeSingle(contour);
        } else {
            Decomposition ret = triangulate(contour);

            // if the simple decomposition failed (worst case for this method)
            // revert to fallback
            if (ret == null) {
                return fallback.decomposeSingle(contour);
            }

            return ret; // simple decomposition succeeded
        }
    }

    public static Decomposition triangulate(List<Vector2> points) {

        Decomposition decomp = new Decomposition();

        int n = points.size();
        if ( n < 3 ) return null;

        int[] V = new int[n];

        /* we want a counter-clockwise polygon in V */

        if ( 0.0f < area(points) )
            for (int v=0; v<n; v++)
                V[v] = v;
        else
            for(int v=0; v<n; v++)
                V[v] = (n-1)-v;

        int nv = n;

        /*  remove nv-2 Vertices, creating 1 triangle every time */
        int count = 2*nv;   /* error detection */

        for(int m=0, v=nv-1; nv>2; ) {

            /* if we loop, it is probably a non-simple polygon */
            if (0 >= (count--)) {
                //** Triangulate: ERROR - probable bad polygon!
                return null;
            }

            /* three consecutive vertices in current polygon, <u,v,w> */

            int u = v;
            if (nv <= u)
                u = 0;     //previous
            v = u+1;
            if (nv <= v)
                v = 0;     //new v
            int w = v+1;
            if (nv <= w)
                w = 0;     //next

            /*int u = nv <= v ? 0 : v;
            v = nv <= u+1 ? 0 : u+1;
            int w = nv < v+1 ? 0 : v+1;*/

            if ( snip(points, u, v, w, nv, V) ) {

                /* output Triangle */
                decomp.addTri(points.get(V[u]), points.get(V[v]), points.get(V[w]));

                m++;

                /* remove v from remaining polygon */
                for(int s=v,t=v+1;t<nv;s++,t++)
                    V[s] = V[t];

                nv--;

                /* resest error detection counter */
                count = 2 * nv;
            }
        }

        return decomp;
    }

    private static float area(List<Vector2> contour) {

        int n = contour.size();

        float area=0.0f;

        Vector2 a = contour.get(n - 1), b;

        for(int i=0; i<n; i++) {
            b = contour.get(i);
            area += a.x * b.y - b.x * a.y; // cross product
        }

        return area*0.5f;
    }

    private static boolean insideTriangle(Vector2 a, Vector2 b, Vector2 c, Vector2 p) {

        return (c.x - b.x)*(p.y - b.y) - (c.y - b.y)*(p.x - b.x) >= 0.0f
                && (a.x - c.x)*(p.y - c.y) - (a.y - c.y)*(p.x - c.x) >= 0.0f
                && (b.x - a.x)*(p.y - a.y) - (b.y - a.y)*(p.x - a.x) >= 0.0f;

    }

    private static boolean snip(List<Vector2> contour, int u, int v, int w, int n, int[] V) {

        Vector2 a, b, c;

        a = contour.get(V[u]);
        b = contour.get(V[v]);
        c = contour.get(V[w]);

        if (GeomUtil.EPSILON > ((b.x-a.x)*(c.y-a.y)) - ((b.y-a.y)*(c.x-a.x)))
            return false;

        for (int i=0; i<n; i++) {
            if(i == u || i == v || i == w)
                continue;
            if (insideTriangle(a, b, c, contour.get(V[i])))
                return false;
        }

        return true;
    }

}
