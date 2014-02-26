/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.Pair;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;

/**
 *
 * @author Aaron
 */
public class Decomposition {
    
    private List<Tri> tris;
    
    public Decomposition() {
        tris = new ArrayList();
    }
    
    public void addTri(Vector2 a, Vector2 b, Vector2 c) {
        tris.add(new Tri(a, b, c));
    }
    
    public List<Tri> getTris() {
        return tris;
    }
    
    public void transform(iTransform2 transform) {
        for (Tri tri: tris) {
            transform.transformLocal(tri.a);
            transform.transformLocal(tri.b);
            transform.transformLocal(tri.c);
        }
    }
    
    public void translate(float x, float y) {
        for (Tri tri: tris) {
            tri.a.add(x, y);
            tri.b.add(x, y);
            tri.c.add(x, y);
        }
    }
    
    public void fill(GL2 gl) {
        gl.glBegin(GL2.GL_TRIANGLES);
        for (Tri tri: tris) {
            tri.a.glVertex(gl);
            tri.b.glVertex(gl);
            tri.c.glVertex(gl);
        }
        gl.glEnd();
    }
    
    public void fill(GL2 gl, iPointCallback pcb) {
        gl.glBegin(GL2.GL_TRIANGLES);
        for (Tri tri: tris) {
            pcb.point(gl, tri.a);
            tri.a.glVertex(gl);
            pcb.point(gl, tri.b);
            tri.b.glVertex(gl);
            pcb.point(gl, tri.c);
            tri.c.glVertex(gl);
        }
        gl.glEnd();
    }
    
    public void draw(GL2 gl, iPointCallback pcb) {
        for (Tri tri: tris) {
            gl.glBegin(GL2.GL_LINE_LOOP);
            pcb.point(gl, tri.a);
            tri.a.glVertex(gl);
            pcb.point(gl, tri.b);
            tri.b.glVertex(gl);
            pcb.point(gl, tri.c);
            tri.c.glVertex(gl);
            gl.glEnd();
        }
    }
    
    public void draw(GL2 gl) {
        for (Tri tri: tris) {
            gl.glBegin(GL2.GL_LINE_LOOP);
            tri.a.glVertex(gl);
            tri.b.glVertex(gl);
            tri.c.glVertex(gl);
            gl.glEnd();
        }
    }

    public class Tri {
        public Vector2 a, b, c;
        public Tri(Vector2 a, Vector2 b, Vector2 c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
    
    public Pair<Vector2, Float> calculateCenterOfMassAndArea() {
        Vector2 center = new Vector2();
        float area = 0;
        for (Tri tri: tris) {
            float m = Math.abs(
                    tri.a.x * (tri.b.y - tri.c.y) +
                    tri.b.x * (tri.c.y - tri.a.y) +
                    tri.c.x * (tri.a.y - tri.b.y)
                    ) / 2;
            Vector2 c = tri.a.sum(tri.b).add(tri.c).multiply(m / 3);
            center.add(c);
            area += m;
        }
        return new Pair(center.divide(area), area);
    }
    
}
