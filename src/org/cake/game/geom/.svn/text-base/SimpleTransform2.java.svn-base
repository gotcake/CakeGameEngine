/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import org.cake.game.exception.GameException;

/**
 * A simple transform class only capable of rotation and translation. Necessary for the collision/physics engines.
 * @author Aaron Cake
 */
public class SimpleTransform2 implements iTransform2 {

    public static SimpleTransform2 createTranslation(float dx, float dy) {
        SimpleTransform2 trans = new SimpleTransform2();
        trans.translateLocal(dx, dy);
        return trans;
    }

    public static SimpleTransform2 createTranslation(Vector2 dpos) {
        SimpleTransform2 trans = new SimpleTransform2();
        trans.translateLocal(dpos);
        return trans;
    }

    public static SimpleTransform2 createRotation(float angle, float x, float y) {
       SimpleTransform2 trans = new SimpleTransform2();
        trans.rotateLocal(angle, x, y);
        return trans;
    }

    public static SimpleTransform2 createRotation(float angle, Vector2 origin) {
        SimpleTransform2 trans = new SimpleTransform2();
        trans.rotateLocal(angle, origin);
        return trans;
    }

    private float m00, m01, m02;
    private float m10, m11, m12;

    /**
     * Create an identity transform.
     */
    public SimpleTransform2() {
        m00 = m11 = 1;
        m01 = m02 = m10 = m12 = 0;
    }

    /**
     * Multiplies this Transform with another one in a new copy.
     * @param t
     * @return the new Transform
     */
    public SimpleTransform2 mult(SimpleTransform2 t) {
        SimpleTransform2 n = new SimpleTransform2();
        n.m00 = m00 * t.m00 + m01 * t.m10;
        n.m01 = m00 * t.m01 + m01 * t.m11;
        n.m02 = m00 * t.m02 + m01 * t.m12 + m02;
        n.m10 = m10 * t.m00 + m11 * t.m10;
        n.m11 = m10 * t.m01 + m11 * t.m11;
        n.m12 = m10 * t.m02 + m11 * t.m12 + m12;
        return n;
    }

    /**
     * Locally multiplies a transform onto this one.
     * @param t
     * @return this transform
     */
    public SimpleTransform2 multLocal(SimpleTransform2 t) {
        float n00 = m00 * t.m00 + m01 * t.m10;
        float n01 = m00 * t.m01 + m01 * t.m11;
        float n02 = m00 * t.m02 + m01 * t.m12 + m02;
        float n10 = m10 * t.m00 + m11 * t.m10;
        float n11 = m10 * t.m01 + m11 * t.m11;
        float n12 = m10 * t.m02 + m11 * t.m12 + m12;
        m00 = n00;
        m01 = n01;
        m02 = n02;
        m10 = n10;
        m11 = n11;
        m12 = n12;
        return this;
    }


    /**
     * Rotates a copy of this Transform about a certain point.
     * @param angle the angle to rotate by.
     * @param x
     * @param y
     * @return the new Transform
     */
    public SimpleTransform2 rotate(float angle, float x, float y) {
        SimpleTransform2 n = new SimpleTransform2();
        //float sin = (float)FastTrig.sin(angle);
        //float cos = (float)FastTrig.cos(angle);
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);
        float omc = 1.0f - cos;
        /*
         * cos -sin x*omc+y*sin
         * sin cos y*omc-x*sin
         */
        float t02 = x * omc + y * sin;
        float t12 = y * omc - x * sin;
        n.m00 = m00 * cos + m01 * sin;
        n.m01 = m00 * -sin + m01 * cos;
        n.m02 = m00 * t02 + m01 * t12 + m02;
        n.m10 = m10 * cos + m11 * sin;
        n.m11 = m10 * -sin + m11 * cos;
        n.m12 = m10 * t02 + m11 * t12 + m12;
        return n;
    }

    /**
     * Rotates this Transform about a certain point.
     * @param angle the angle to rotate by.
     * @param x
     * @param y
     * @return this Transform
     */
    public SimpleTransform2 rotateLocal(float angle, float x, float y) {
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);
        float omc = 1.0f - cos;
        /*
         * cos -sin x*omc+y*sin
         * sin cos y*omc-x*sin
         */
        float t02 = x * omc + y * sin;
        float t12 = y * omc - x * sin;
        float n00 = m00 * cos + m01 * sin;
        float n01 = m00 * -sin + m01 * cos;
        m02 = m00 * t02 + m01 * t12 + m02;
        float n10 = m10 * cos + m11 * sin;
        float n11 = m10 * -sin + m11 * cos;
        m12 = m10 * t02 + m11 * t12 + m12;
        m00 = n00;
        m01 = n01;
        m10 = n10;
        m11 = n11;
        return this;
    }

    /**
     * Rotates a copy of this Transform about a certain point.
     * @param angle the angle to rotate by.
     * @param origin the point to rotate around
     * @return the new Transform
     */
    public SimpleTransform2 rotate(float angle, Vector2 origin) {
        SimpleTransform2 n = new SimpleTransform2();
        //float sin = (float)FastTrig.sin(angle);
        //float cos = (float)FastTrig.cos(angle);
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);
        float omc = 1.0f - cos;
        /*
         * cos -sin x*omc+y*sin
         * sin cos y*omc-x*sin
         */
        float t02 = origin.x * omc + origin.y * sin;
        float t12 = origin.y * omc - origin.x * sin;
        n.m00 = m00 * cos + m01 * sin;
        n.m01 = m00 * -sin + m01 * cos;
        n.m02 = m00 * t02 + m01 * t12 + m02;
        n.m10 = m10 * cos + m11 * sin;
        n.m11 = m10 * -sin + m11 * cos;
        n.m12 = m10 * t02 + m11 * t12 + m12;
        return n;
    }

    /**
     * Rotates this Transform about a certain point.
     * @param angle the angle to rotate by.
     * @param origin the point to rotate around
     * @return this Transform
     */
    public SimpleTransform2 rotateLocal(float angle, Vector2 origin) {
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);
        float omc = 1.0f - cos;
        /*
         * cos -sin x*omc+y*sin
         * sin cos y*omc-x*sin
         */
        float t02 = origin.x * omc + origin.y * sin;
        float t12 = origin.y * omc - origin.x * sin;
        float n00 = m00 * cos + m01 * sin;
        float n01 = m00 * -sin + m01 * cos;
        m02 = m00 * t02 + m01 * t12 + m02;
        float n10 = m10 * cos + m11 * sin;
        float n11 = m10 * -sin + m11 * cos;
        m12 = m10 * t02 + m11 * t12 + m12;
        m00 = n00;
        m01 = n01;
        m10 = n10;
        m11 = n11;
        return this;
    }

    /**
     * Translate a copy of this Transform by a specified distance
     * @param x
     * @param y
     * @return the new Transform
     */
    public SimpleTransform2 translate(float x, float y) {
        SimpleTransform2 n = new SimpleTransform2();
        /*
         * 1 0 x
         * 0 1 y
         */
        n.m00 = m00;
        n.m01 = m01;
        n.m02 = m00 * x + m01 * y + m02;
        n.m10 = m10;
        n.m11 = m11;
        n.m12 = m10 * x + m11 * y + m12;
        return n;
    }

    /**
     * Translate this Transform by a specified distance
     * @param x
     * @param y
     * @return this Transform
     */
    public SimpleTransform2 translateLocal(float x, float y) {
        /*
         * 1 0 x
         * 0 1 y
         */
        float n02 = m00 * x + m01 * y + m02;
        m12 = m10 * x + m11 * y + m12;
        m02 = n02;
        return this;
    }

    /**
     * Translate a copy of this Transform by a specified distance
     * @param distance
     * @return the new Transform
     */
    public SimpleTransform2 translate(Vector2 distance) {
        SimpleTransform2 n = new SimpleTransform2();
        /*
         * 1 0 x
         * 0 1 y
         */
        n.m00 = m00;
        n.m01 = m01;
        n.m02 = m00 * distance.x + m01 * distance.y + m02;
        n.m10 = m10;
        n.m11 = m11;
        n.m12 = m10 * distance.x + m11 * distance.y + m12;
        return n;
    }

    /**
     * Translate this Transform by a specified distance
     * @param distance
     * @return this Transform
     */
    public SimpleTransform2 translateLocal(Vector2 distance) {
        /*
         * 1 0 x
         * 0 1 y
         */
        float n02 = m00 * distance.x + m01 * distance.y + m02;
        m12 = m10 * distance.x + m11 * distance.y + m12;
        m02 = n02;
        return this;
    }

    /**
     * Transforms a copy of the point v.
     * @param v
     * @return the transformed point.
     */
    @Override
    public Vector2 transform(Vector2 v) {
        /*
         * x' = x * m00 + y * m01 + m02
         * y' = x * m10 + y * m11 + m12
         */
        return new Vector2(v.x * m00 + v.y * m01 + m02, v.x * m10 + v.y * m11 + m12);
    }

    /**
     * Transforms a copy of the point v.
     * @param v
     * @return the transformed point.
     */
    @Override
    public Vector2 transform(Vector2 v, Vector2 dest) {
        /*
         * x' = x * m00 + y * m01 + m02
         * y' = x * m10 + y * m11 + m12
         */
        return dest.set(v.x * m00 + v.y * m01 + m02, v.x * m10 + v.y * m11 + m12);
    }

    /**
     * Transforms a point according to this transform. This method overwrites v.
     * @param v
     * @return the transformed point v
     */
    @Override
    public Vector2 transformLocal(Vector2 v) {
        /*
         * x' = x * m00 + y * m01 + m02
         * y' = x * m10 + y * m11 + m12
         */
        float x = v.x * m00 + v.y * m01 + m02;
        v.y = v.x * m10 + v.y * m11 + m12;
        v.x = x;
        return v;
    }

    /**
     * Determines whether or not the Transform is invertible
     * @return
     */
    public boolean isInvertible() {
        return (m00 * m11 - m01 * m10) != 0;
    }

    /**
     * Get an inverted copy of this Transform
     * @return the new Transform
     */
    public SimpleTransform2 inverse() {
        SimpleTransform2 t = new SimpleTransform2();
        float z = (m00 * m11 - m01 * m10);
        if (z == 0) {
            throw new GameException("Transform is not invertible.");
        }
        t.m00 = m11 / z;
        t.m01 = -m01 / z;
        t.m10 = -m10 / z;
        t.m11 = m00 / z;
        t.m02 = -t.m00 * m02 - t.m01 * m12;
        t.m12 = -t.m10 * m02 - t.m11 * m12;
        return t;
    }

    /**
     * Locally invert this Transform
     * @return this Transform
     */
    public SimpleTransform2 invert() {
        float z = (m00 * m11 - m01 * m10);
        if (z == 0) {
            throw new GameException("Transform is not invertible.");
        }
        float n00 = m11 / z;
        float n01 = -m01 / z;
        float n10 = -m10 / z;
        float n11 = m00 / z;
        float n02 = -n00 * m02 - n01 * m12;
        float n12 = -n10 * m02 - n11 * m12;
        m00 = n00;
        m01 = n01;
        m02 = n02;
        m10 = n10;
        m11 = n11;
        m12 = n12;
        return this;
    }

    /**
     * Gets whether or not this Transform is an identity Transform.
     * Can be used to avoid unnecessary computation.
     * @return true if this is an identity Transform
     */
    @Override
    public boolean isIdentity() {
        return (m00 == 1 && m01 == 0 && m02 == 0 && m10 == 0 && m11 == 1 && m12 == 0);
    }

    /**
     * Resets this Transform to an identity Transform
     * @return this Transform
     */
    public SimpleTransform2 reset() {
        m00 = m11 = 1;
        m01 = m02 = m10 = m12 = 0;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SimpleTransform2)) return false;
        if (obj == this) return true;
        SimpleTransform2 other = (SimpleTransform2)obj;
        if (other.m00 != m00 || other.m01 != m01 || other.m02 != m02 || other.m10 != m10 || other.m11 != m11 || other.m12 != m12) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Float.floatToIntBits(this.m00);
        hash = 73 * hash + Float.floatToIntBits(this.m01);
        hash = 73 * hash + Float.floatToIntBits(this.m02);
        hash = 73 * hash + Float.floatToIntBits(this.m10);
        hash = 73 * hash + Float.floatToIntBits(this.m11);
        hash = 73 * hash + Float.floatToIntBits(this.m12);
        return hash;
    }

    @Override
    public String toString() {
        return "SimpleTransform2[" + m00 + ", " + m01 + ", " + m02 + ", " + m10 + ", " + m11 + ", " + m12 + "]";
    }

    private static float[] matrix = new float[] {
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    };

    @Override
    public void glMultMatrix(GL2 gl) {
        matrix[0] = m00;
        matrix[1] = m01;
        matrix[3] = m02;
        matrix[4] = m10;
        matrix[5] = m11;
        matrix[7] = m12;
        gl.glMultTransposeMatrixf(matrix, 0);
    }

    @Override
    public List<Vector2> transformLocal(List<Vector2> vectors) {
        float x;
        for (Vector2 v: vectors) {
            x = v.x * m00 + v.y * m01 + m02;
            v.y = v.x * m10 + v.y * m11 + m12;
            v.x = x;
        }
        return vectors;
    }

    @Override
    public List<Vector2> transform(List<Vector2> vectors) {
        List<Vector2> ret = new ArrayList<>(vectors.size());
        for (Vector2 v: vectors) {
            ret.add(new Vector2(
                    v.x * m00 + v.y * m01 + m02,
                    v.x * m10 + v.y * m11 + m12
                 ));
        }
        return ret;
    }

    public float getAngle() {
        return (float)Math.atan2(m01, m00);
    }

    public float getTranslationX() {
        return m02;
    }

    public float getTranslationY() {
        return m12;
    }

    public Vector2 getTranslation() {
        return new Vector2(m02, m12);
    }

    @Override
    public float[] getData() {
        return new float[] {
            m00, m01, m02, m10, m11, m12
        };
    }

    @Override
    public Transform2 asAffineTransform() {
        return new Transform2(getData());
    }

}
