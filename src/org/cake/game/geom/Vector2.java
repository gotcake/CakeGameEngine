/*
    This file is part of CakeGame engine.

    CakeGame engine is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CakeGame engine is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CakeGame engine.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cake.game.geom;

import java.io.Serializable;
import javax.media.opengl.GL2;
import org.cake.game.Sprite;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.io.objectxml.ObjectXMLNode;
import org.cake.game.io.objectxml.iObjectXMLSerializer;

/**
 * A class representing a 2 dimensional vector, or point in 2d space.
 * @author Aaron Cake
 */
public class Vector2 implements Serializable {

    static {
        ObjectXML.registerParser(Vector2.class, new iObjectXMLSerializer<Vector2>() {
            @Override
            public Vector2 deserialize(ObjectXMLNode node) {
                return new Vector2(node.getAttrNumber("x").floatValue(), node.getAttrNumber("y").floatValue());
            }

            @Override
            public boolean serialize(ObjectXMLNode node, Vector2 obj) {
                node.setAttr("x", obj.x);
                node.setAttr("y", obj.y);
                return true;
            }
        });
    }

    /**
     * The x component
     */
    public float x;
    /**
     * The y component
     */
    public float y;

    /**
     * Creates a zero vector (0, 0)
     */
    public Vector2() {
        x = y = 0;
    }

    /**
     * Creates a vector with the given x and y components
     * @param x the x component
     * @param y the y component
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the sum of this and another vector as a new vector.
     * @param other another vector
     * @return a new vector containing the sum of the two vectors
     */
    public Vector2 sum(Vector2 other) {
        return new Vector2(other.x + x, other.y + y);
    }

    public Vector2 sum(float x, float y) {
        return new Vector2(x + this.x, y + this.y);
    }

    public Vector2 leftOrthagonal() {
        return new Vector2(-y, x);
    }

    public Vector2 leftOrthagonalLocal() {
        float temp = x;
        x = -y;
        y = temp;
        return this;
    }

    public Vector2 unit() {
        float mag = (float)Math.hypot(x, y);
        return new Vector2(x / mag, y / mag);
    }

    public Vector2 unitLocal() {
        float mag = (float)Math.hypot(x, y);
        x /= mag;
        y /= mag;
        return this;
    }

    /**
     * Adds another vector to this one, the result is stored in this vector.
     * @param other another vector
     * @return this vector
     */
    public Vector2 add(Vector2 other) {
        x += other.x;
        y += other.y;
        return this;
    }

    /**
     * Adds the given vector components to this vector, the result is stored in this vector.
     * @param x the x component to add
     * @param y the y component to add
     * @return this vector
     */
    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Gets the difference of this vector minus another vector as a new vector.
     * @param subtract another vector
     * @return a new vector containing the result of this vector minus the other vector
     */
    public Vector2 difference(Vector2 subtract) {
        return new Vector2(x - subtract.x, y - subtract.y);
    }

    /**
     * Subtracts this vector by another vector, the result is stored in this vector
     * @param other another vector
     * @return this vector
     */
    public Vector2 subtract(Vector2 other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    /**
     * Returns the vector between two points by subtracting this vector from the other vector as a new vector.
     * @param other another vector
     * @return a new vector being the vector between this point and another point
     */
    public Vector2 vectorTo(Vector2 other) {
        return new Vector2(other.x - x, other.y - y);
    }

    /**
     * Calculates the vector between two points by subtracting this vector from the other vector and storing the result in this vector.
     * @param other another vector
     * @return this vector
     */
    public Vector2 vectorToLocal(Vector2 other) {
        x = other.x - x;
        y = other.y - y;
        return this;
    }

    /**
     * Calculates the angle between two vectors
     * @param other another vector
     * @return the angle between this vector and other
     */
    public float angleBewteen(Vector2 other) {
        return (float)Math.acos(dot(other) / (magnitude() * other.magnitude()));
    }

    /**
     * Locally inverts this vector.
     * @return this vector
     */
    public Vector2 invert() {
        x  = -x;
        y  = -y;
        return this;
    }

    /**
     * Returns a new vector being the inverse of this vector
     * @return the inverse of this vector
     */
    public Vector2 inverse() {
        return new Vector2(-x, -y);
    }

    /**
     * Calculates the magnitude of this vector.
     * @return the magnitude of this vector.
     */
    public float magnitude() {
        return (float)Math.hypot(x, y);
    }

    /**
     * Calculates the magnitude squared of this vector.
     * This operation is much faster than calculating the magnitude as
     * there is no need to take the square root.
     * @return the magnitude squared of this vector
     */
    public float magnitudeSquared() {
        return x * x + y * y;
    }

    /**
     * Calculates the distance between this vector and another vector as points.
     * @param other another vector
     * @return the distance between this vector and another vector
     */
    public float distanceTo(Vector2 other) {
        return (float)Math.hypot(x - other.x, y - other.y);
    }

    /**
     * Calculates the distance squared between this vector and another vector as points.
     * This operation is much faster than calculating the distance as
     * there is no need to take the square root.
     * @param other another vector
     * @return the distance between this vector and another vector
     */
    public float distanceToSquared(Vector2 other) {
        float dx = x - other.x;
        float dy = y - other.y;
        return dx * dx + dy * dy;
    }

    /**
     * Gets this vector multiplied by a scalar as a new vector.
     * @param scalar some number
     * @return a new vector containing the result of the scalar times this vector
     */
    public Vector2 product(float scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    /**
     * Gets this vector multiplied by a scalar and stores the result in the destination vector.
     * @param scalar some number
     * @param dest the destination vector
     * @return dest
     */
    public Vector2 product(float scalar, Vector2 dest) {
        return dest.set(x * scalar, y * scalar);
    }

    /**
     * Multiplies this vector by some scalar, the result is stored in this vector.
     * @param scalar some number
     * @return this vector
     */
    public Vector2 multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * Gets this vector divided by a scalar as a new vector.
     * @param scalar some number
     * @return a new vector containing the result of the scalar times this vector
     */
    public Vector2 divided(float scalar) {
        return new Vector2(x / scalar, y / scalar);
    }

    /**
     * Gets this vector divided by a scalar and stores the result the destination vector.
     * @param scalar some number
     * @param dest the destination vector
     * @return dest
     */
    public Vector2 divideded(float scalar, Vector2 dest) {
        return dest.set(x / scalar, y / scalar);
    }

    /**
     * Divides this vector by some scalar, the result is stored in this vector.
     * @param scalar some number
     * @return this vector
     */
    public Vector2 divide(float scalar) {
        x /= scalar;
        y /= scalar;
        return this;
    }

    /**
     * Sets the components of this vector
     * @param x the x component
     * @param y the y component
     * @return this vector
     */
    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Copies the values of the components from another vector to this one
     * @param other another vector
     * @return this vector
     */
    public Vector2 set(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    /**
     * Makes a copy of this vector with the same components
     * @return a copy of this vector
     */
    public Vector2 copy() {
        return new Vector2(x, y);
    }

    /**
     * Interpolates between two points, storing the result in dest
     * @param other the other point to interpolate to
     * @param t the amount to interpolate by [0.0-1.0]
     * @param dest the destination vector to store the result in
     * @return dest
     */
    public Vector2 interpolateTo(Vector2 other, float t, Vector2 dest) {
        dest.x = (1 - t) * x + t * other.x;
        dest.y = (1 - t) * y + t * other.y;
        return dest;
    }

    /**
     * Interpolates between two points
     * @param other the other point to interpolate to
     * @param t the amount to interpolate by [0.0-1.0]
     * @return the new interpolated vector
     */
    public Vector2 interpolateTo(Vector2 other, float t) {
        return new Vector2((1 - t) * x + t * other.x, (1 - t) * y + t * other.y);
    }

    @Override
    public String toString() {
        return "Vector2[(" + x + "," + y + ")]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Float.floatToIntBits(this.x);
        hash = 71 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Vector2)) return false;
        if (obj == this) return true;
        Vector2 other = (Vector2)obj;
        return other.x == x && other.y == y;
    }

    public boolean equals(float x, float y) {
        return this.x == x && this.y == y;
    }

    /**
     * Calculates the dot product of this vector and another vector.
     * @param other another vector
     * @return the dot product
     */
    public float dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Calculates the cross product of this vector with another vector.
     * @param other another vector
     * @return the cross product
     */
    public float cross(Vector2 other) {
        return x * other.y - y * other.x;
    }

    /**
     * Utility method to bind this vector
     * @param gl the gl context
     */
    public void glVertex(GL2 gl) {
        gl.glVertex2f(x, y);
    }

    public float distanceToSquared(float x, float y) {
        float dx = this.x - x;
        float dy = this.y - y;
        return dx * dx + dy * dy;
    }

}
