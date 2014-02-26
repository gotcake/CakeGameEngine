/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom.decomp;

import java.util.List;
import javax.media.opengl.glu.GLU;
import org.cake.game.geom.Decomposition;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.decomp.glutess.GLUtessellatorImpl;
import org.cake.game.geom.decomp.glutess.DecompositionGeneratorCallback;
import org.cake.game.geom.iContour;
import org.cake.game.geom.iConvex;

/**
 * Uses a modified version of JOGL's GLUTessellator to triangulate shapes.
 * @author Aaron Cake
 */
public class GLUTessDecomposer implements iDecomposer {

    private GLUtessellatorImpl tess;
    private DecompositionGeneratorCallback cb;

    @Override
    public Decomposition decomposeMulti(List<iContour> contours) {
        tess.gluTessBeginPolygon(null);
        for (iContour c: contours) {
            List<Vector2> contour = c.getPoints();
            if (contour.size() >= 3) {
               tess.gluTessBeginContour();
                for (Vector2 pt: contour) {
                    tess.gluTessVertex(pt.x, pt.y);
                }
                tess.gluTessEndContour();
            }
        }
        tess.gluTessEndPolygon();
        return cb.getDecomp();
    }

    @Override
    public Decomposition decomposeSingle(List<Vector2> contour) {
        if (contour.size() >= 3) {
            tess.gluTessBeginPolygon(null);
            tess.gluTessBeginContour();
            for (Vector2 pt: contour) {
                tess.gluTessVertex(pt.x, pt.y);
            }
            tess.gluTessEndContour();
            tess.gluTessEndPolygon();
        }
        return cb.getDecomp();
    }

    public GLUTessDecomposer() {
        tess = (GLUtessellatorImpl)GLUtessellatorImpl.gluNewTess();
        cb = new DecompositionGeneratorCallback();
        tess.gluTessCallback(GLU.GLU_TESS_BEGIN, cb);
        tess.gluTessCallback(GLU.GLU_TESS_VERTEX, cb);
        tess.gluTessCallback(GLU.GLU_TESS_END, cb);
        tess.gluTessCallback(GLU.GLU_TESS_COMBINE, cb);
        tess.gluTessCallback(GLU.GLU_TESS_ERROR, cb);
        tess.gluTessNormal(0.0f, 0.0f, 1.0f);
        tess.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
    }

}
