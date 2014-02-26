/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.cake.game.exception.GameException;

/*
 * In General:
 *
 * a | b | c
 * d | e | f
 *
 * times
 *
 * g | h | i
 * j | k | l
 *
 * a g+b j | a h+b k | c+a i+b l
 * d g+e j | d h+e k | f+d i+e l
 */

/*
 * In General:
 * m00*(A) + m01*(D) | m00*(B) + m01*(E) | m02 + m00*(C) + m01*(F)
 * m10*(A) + m11*(D) | m10*(B) + m11*(E) | m12 + m10*(C) + m11*(F)
 */

/**
 * A transform capable of affine and non-linear transforms.
 * @author Aaron Cake
 */
public class Transform2 implements iTransform2 {

    public static Transform2 createTranslation(float dx, float dy) {
        Transform2 trans = new Transform2();
        trans.translateLocal(dx, dy);
        return trans;
    }

    public static Transform2 createTranslation(Vector2 dpos) {
        Transform2 trans = new Transform2();
        trans.translateLocal(dpos);
        return trans;
    }

    public static Transform2 createRotation(float angle, float x, float y) {
        Transform2 trans = new Transform2();
        trans.rotateLocal(angle, x, y);
        return trans;
    }

    public static Transform2 createRotation(float angle, Vector2 origin) {
        Transform2 trans = new Transform2();
        trans.rotateLocal(angle, origin);
        return trans;
    }

    public static Transform2 createHorizontalShear(float k) {
        Transform2 trans = new Transform2();
        trans.shearHorizontalLocal(k);
        return trans;
    }

    public static Transform2 createScale(float scaleX, float scaleY) {
        Transform2 trans = new Transform2();
        trans.scaleLocal(scaleX, scaleY);
        return trans;
    }

    public static Transform2 createScale(float scale) {
        Transform2 trans = new Transform2();
        trans.scaleLocal(scale, scale);
        return trans;
    }

    public static Transform2 createReflection(float x, float y) {
        Transform2 trans = new Transform2();
        trans.reflectLocal(x, y);
        return trans;
    }

    private float m00, m01, m02;
    private float m10, m11, m12;

    /**
     * Create transform from the given data
     */
    public Transform2(float[] data) {
        m00 = data[0];
        m01 = data[1];
        m02 = data[2];
        m10 = data[3];
        m11 = data[4];
        m12 = data[5];
    }

    /**
     * Create an identity transform.
     */
    public Transform2() {
        m00 = m11 = 1;
        m01 = m02 = m10 = m12 = 0;
    }

    /**
     * Multiplies this Transform with another one in a new copy.
     * @param t
     * @return the new Transform
     */
    public Transform2 mult(Transform2 t) {
        Transform2 n = new Transform2();
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
    public Transform2 multLocal(Transform2 t) {
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
     * Applies a shearHorizontal of factor k through the origin to this transform
     * @param k the shearHorizontal factor
     * @return this transform
     */
    public Transform2 shearHorizontalLocal(float k) {
        /*
         * 1 k 0
         * 0 1 0
         */
        m01 = m00 * k + m01;
        m11 = m10 * k + m11;
        return this;
    }

    /**
     * Applies a shearHorizontal through to origin to a copy of this transform
     * @param k the shearHorizontal factor
     * @return the new transform
     */
    public Transform2 shearHorizontal(float k) {
        /*
         * 1 k 0
         * 0 1 0
         */
        Transform2 n = new Transform2();
        n.m00 = m00;
        n.m01 = m00 * k + m01;
        n.m02 = m02;
        n.m10 = m10;
        n.m11 = m10 * k + m11;
        n.m12 = m12;
        return n;
    }

    /**
     * Scales a copy of this transform about (0, 0) by the amounts specified.
     * @param scalex
     * @param scaley
     * @return the new transform
     */
    public Transform2 scale(float scalex, float scaley) {
        Transform2 n = new Transform2();
        /*
         * x 0 0
         * 0 y 0
         */
        n.m00 = m00 * scalex + m01 * 0;
        n.m01 = m00 * 0 + m01 * scaley;
        n.m02 = m00 * 0 + m01 * 0 + m02;
        n.m10 = m10 * scalex + m11 * 0;
        n.m11 = m10 * 0 + m11 * scaley;
        n.m12 = m10 * 0 + m11 * 0 + m12;
        return n;
    }

    /**
     * Reflects this transform along a line going through the origin and the point (x, y)
     * @param x
     * @param y
     * @return this transform
     */
    public Transform2 reflectLocal(float x, float y) {
        /*
         * x^2-y^2, 2xy     divided by magnitude of vector squared
         * 2xy, y^2-x^2
         */
        float mag = x * x + y * y;
        float i00 = (x * x - y * y) / mag;
        float i0110 = (2 * x * y) / mag;
        float i11 = (y * y - x * x) / mag;
        float n00 = m00 * i00 + m01 * i0110;
        float n01 = m00 * i0110 + m01 * i11;
        float n10 = m10 * i00 + m11 * i0110;
        float n11 = m10 * i0110 + m11 * i11;
        m00 = n00;
        m01 = n01;
        m10 = n10;
        m11 = n11;
        return this;
    }

    /**
     * Reflects this transform along a line going through the origin and
     * @param x
     * @param y
     * @return the new transform
     */
    public Transform2 reflect(float x, float y) {
        /*
         * x^2-y^2, 2xy      divided by magnitude of vector squared
         * 2xy, y^2-x^2
         */
        Transform2 n = new Transform2();
        float mag = x * x + y * y;
        float i00 = (x * x - y * y) / mag;
        float i0110 = (2 * x * y) / mag;
        float i11 = (y * y - x * x) / mag;
        n.m00 = m00 * i00 + m01 * i0110;
        n.m01 = m00 * i0110 + m01 * i11;
        n.m02 = m02;
        n.m10 = m10 * i00 + m11 * i0110;
        n.m11 = m10 * i0110 + m11 * i11;
        n.m12 = m12;
        return n;
    }

    /**
     * Scales this transform about (0, 0) by the amounts specified.
     * @param scalex
     * @param scaley
     * @return this transform
     */
    public Transform2 scaleLocal(float scalex, float scaley) {
        /*
         * x 0 0
         * 0 y 0
         */
        float n00 = m00 * scalex + m01 * 0;
        float n01 = m00 * 0 + m01 * scaley;
        float n02 = m00 * 0 + m01 * 0 + m02;
        float n10 = m10 * scalex + m11 * 0;
        float n11 = m10 * 0 + m11 * scaley;
        float n12 = m10 * 0 + m11 * 0 + m12;
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
    public Transform2 rotate(float angle, float x, float y) {
        Transform2 n = new Transform2();
        //float sin = (float)FastTrig.sin(angle);
        //float cos = (float)FastTrig.cos(angle);
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);
        float omc = 1.0f - cos;
        /*
         * cos -sin x*omc+y*sin
         * sin cos y*omc-x*sin
         */

        /*
        * In General:
        * m00*(A) + m01*(D) | m00*(B) + m01*(E) | m02 + m00*(C) + m01*(F)
        * m10*(A) + m11*(D) | m10*(B) + m11*(E) | m12 + m10*(C) + m11*(F)
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
    public Transform2 rotateLocal(float angle, float x, float y) {
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);
        float omc = 1.0f - cos;
        /*
         * cos -sin x*omc+y*sin
         * sin cos y*omc-x*sin
         */

        /*
        * m00*(cos) + m01*(sin) | m00*(-sin) + m01*(cos) | m02 + m00*(x*omc+y*sin) + m01*(y*omc-x*sin)
        * m10*(cos) + m11*(sin) | m10*(-sin) + m11*(cos) | m12 + m10*(x*omc+y*sin) + m11*(y*omc-x*sin)
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
    public Transform2 rotate(float angle, Vector2 origin) {
        Transform2 n = new Transform2();
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
    public Transform2 rotateLocal(float angle, Vector2 origin) {
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);
        float omc = 1.0f - cos;
        /*
         * cos -sin x*omc+y*sin
         * sin cos y*omc-x*sin
         */
        /*
         *
         */
        float t02 = origin.x * omc + origin.y * sin;
        float t12 = origin.y * omc - origin.x * sin;
        float n00 = m00 * cos + m01 * sin;
        float n01 = m00 * -sin + m01 * cos;
        float n02 = m00 * t02 + m01 * t12 + m02;
        float n10 = m10 * cos + m11 * sin;
        float n11 = m10 * -sin + m11 * cos;
        float n12 = m10 * t02 + m11 * t12 + m12;
        m00 = n00;
        m01 = n01;
        m02 = n02;
        m10 = n10;
        m11 = n11;
        m12 = n12;
        return this;
    }

    /**
     * Translate a copy of this Transform by a specified distance
     * @param x
     * @param y
     * @return the new Transform
     */
    public Transform2 translate(float x, float y) {
        Transform2 n = new Transform2();
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
    public Transform2 translateLocal(float x, float y) {
        /*
         * 1 0 x
         * 0 1 y
         */
        float n02 = m00 * x + m01 * y + m02;
        float n12 = m10 * x + m11 * y + m12;
        m02 = n02;
        m12 = n12;
        return this;
    }

    /**
     * Translate a copy of this Transform by a specified distance
     * @param distance
     * @return the new Transform
     */
    public Transform2 translate(Vector2 distance) {
        Transform2 n = new Transform2();
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
    public Transform2 translateLocal(Vector2 distance) {
        /*
         * 1 0 x
         * 0 1 y
         */
        float n02 = m00 * distance.x + m01 * distance.y + m02;
        float n12 = m10 * distance.x + m11 * distance.y + m12;
        m02 = n02;
        m12 = n12;
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
    public Transform2 inverse() {
        Transform2 t = new Transform2();
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
    public Transform2 invert() {
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
    public Transform2 reset() {
        m00 = m11 = 1;
        m01 = m02 = m10 = m12 = 0;
        return this;
    }

    public Transform2 copy() {
        Transform2 t = new Transform2();
        t.m00 = m00;
        t.m01 = m01;
        t.m02 = m02;
        t.m10 = m10;
        t.m11 = m11;
        t.m12 = m12;
        return t;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Transform2)) return false;
        if (obj == this) return true;
        Transform2 other = (Transform2)obj;
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
        return "Transform2[" + m00 + ", " + m01 + ", " + m02 + ", " + m10 + ", " + m11 + ", " + m12 + "]";
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
        //gl.glMultMatrixf(matrix, 0);
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

    /*
    * x' = x * m00 + y * m01 + m02
    * y' = x * m10 + y * m11 + m12
    */

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

    @Override
    public float[] getData() {
        return new float[] {
            m00, m01, m02, m10, m11, m12
        };
    }

    @Override
    public Transform2 asAffineTransform() {
        return this;
    }
}
