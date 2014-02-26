/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import java.util.List;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.io.objectxml.ObjectXMLNode;
import org.cake.game.io.objectxml.iObjectXMLSerializer;

/**
 *
 * @author Aaron
 */
public class FloatRange {

    static {
        ObjectXML.registerParser(FloatRange.class, new iObjectXMLSerializer<FloatRange>() {

            @Override
            public FloatRange deserialize(ObjectXMLNode node) {
                return new FloatRange(node.getAttrNumber("high").floatValue(), node.getAttrNumber("low").floatValue());
            }

            @Override
            public boolean serialize(ObjectXMLNode node, FloatRange r) {
                node.setAttr("high", r.high);
                node.setAttr("low", r.low);
                return true;
            }
        });
    }

    private float high;
    private float low;

    public FloatRange(float a) {
        low = high = a;
    }

    public FloatRange(float a, float b) {
        if (a < b) {
            low = a;
            high = b;
        } else {
            high = a;
            low = b;
        }
    }

    public FloatRange(float... nums) {
        if (nums.length == 0) {
            low = high = 0;
            return;
        }
        float h = nums[0], l = nums[0], n;
        for (int i=1; i<nums.length; i++) {
            n = nums[i];
            if (n < l)
                l = n;
            if (n > h)
                h = n;
        }
        high = h;
        low = l;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FloatRange)) {
            return false;
        }
        FloatRange r = (FloatRange)obj;
        return r.high == high && r.low == low;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Float.floatToIntBits(this.high);
        hash = 37 * hash + Float.floatToIntBits(this.low);
        return hash;
    }

    public FloatRange(List<Float> nums) {
        if (nums.isEmpty()) {
            low = high = 0;
            return;
        }
        float h = nums.get(0), l = nums.get(0), n;
        for (int i=1; i<nums.size(); i++) {
            n = nums.get(i);
            if (n < l)
                l = n;
            if (n > h)
                h = n;
        }
        high = h;
        low = l;
    }

    public float getLow() {
        return low;
    }

    public float getHigh() {
        return high;
    }

    public float getAverage() {
        return (high + low) / 2;
    }

    public void set(float a, float b) {
        if (a < b) {
            low = a;
            high = b;
        } else {
            high = a;
            low = b;
        }
    }

    public void set(float... nums) {
        if (nums.length == 0) {
            low = high = 0;
            return;
        }
        float h = nums[0], l = nums[0], n;
        for (int i=1; i<nums.length; i++) {
            n = nums[i];
            if (n < l)
                l = n;
            if (n > h)
                h = n;
        }
        high = h;
        low = l;
    }

    public void include(float t) {
        if (t < low)
            low = t;
        if (t > high)
            high = t;
    }

    public FloatRange union(FloatRange other) {
        return new FloatRange(Math.min(low, other.low), Math.max(high, other.high));
    }

    public boolean intersects(FloatRange other) {
        return (other.low < high && other.low > low) || (other.high < high && other.high > low) ||
                (low < other.high && low > other.low) || (high < other.high && high > other.low);
    }

    public FloatRange intersection(FloatRange other) {
        if (other.low < high && other.low > low)
            return new FloatRange(other.low, high);
        if (other.high < high && other.high > low)
            return new FloatRange(low, other.high);
        return null;
    }

    public float interpolate(float t) {
        return t * (high - low) + low;
    }

    public boolean isInRange(float num) {
        return num <= high && num >= low;
    }

    public float clipRange(float num) {
        return num < low ? low : (num > high ? high : num);
    }

    public float randomRange() {
        return (float)Math.random() * (high - low) + low;
    }
}
