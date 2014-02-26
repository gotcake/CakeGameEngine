package org.cake.game.geom.decomp.glutess;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cake.game.geom.Decomposition;
import org.cake.game.geom.Triangle;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iConvex;

/**
 *
 * @author Aaron
 */
public class DecompositionGeneratorCallback extends GLUtessellatorCallbackAdapter {

    private int type, count;
    private Decomposition decomp;
    private Vector2 p1, p2;

    @Override
    public void begin(int type) {
        this.type = type;
        count = 0;
        if (decomp == null) {
            decomp = new Decomposition();
        }
    }
    @Override
    public void vertex(Object vertexData) {
        count++;
        float[] vertex = (float[])vertexData;
        Vector2 v = new Vector2(vertex[0], vertex[1]);
        if (type == GL.GL_TRIANGLES) {
            if (count == 3) {
                decomp.addTri(p1, p2, v);
                count = 0;
            } else if (count == 2) {
                p2 = v;
            } else { // count == 1
                p1 = v;
            }
        } else if (type == GL.GL_TRIANGLE_FAN) {
            if (count == 1) {
                p1 = v;
            } else if (count == 2) {
                p2 = v;
            } else { // count >= 3
                decomp.addTri(p1, p2, v);
                p2 = v;
            }
        } else if (type == GL.GL_TRIANGLE_STRIP) {
            if (count == 1) {
                p1 = v;
            } else if (count == 2) {
                p2 = v;
            } else { // count >= 3
                decomp.addTri(p1, p2, v);
                p1 = p2;
                p2 = v;
            }
        }
    }
    @Override
    public void end() {
        if (type == GL.GL_TRIANGLES) {
            if (count != 0) {
                System.err.println("Warning: Unmatched triangle.");
            }
        } else if (type == GL.GL_TRIANGLE_FAN) {
            if (count < 3) {
                System.err.println("Warning: Unfinished triangle fan.");
            }
        } else if (type == GL.GL_TRIANGLE_STRIP) {
            if (count < 3) {
                System.err.println("Warning: Unfinished triangle stip.");
            }
        }
    }

    @Override
    public void error(int errnum) {
        System.err.println("Warning: Tesselation error: " + GLU.createGLU(GLU.getCurrentGL()).gluErrorString(errnum));
    }

    @Override
    public void combine(float[] coords, Object[] data,
                      float[] weight, Object[] outData) {
        outData[0] = coords;
    }

    public Decomposition getDecomp() {
        Decomposition d = decomp;
        if (d == null)
            d = new Decomposition();
        decomp = null;
        return d;
    }
}
