/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.List;
import javax.media.opengl.GL2;

/**
 * An interface linking the two transform classes.
 * @author Aaron Cake
 */
public interface iTransform2 {

    public boolean isIdentity();

    public Vector2 transform(Vector2 v);
    public Vector2 transform(Vector2 v, Vector2 dest);
    public Vector2 transformLocal(Vector2 v);
    public List<Vector2> transformLocal(List<Vector2> v);
    public List<Vector2> transform(List<Vector2> v);

    public float[] getData();
    public Transform2 asAffineTransform();

    public void glMultMatrix(GL2 gl);

}
