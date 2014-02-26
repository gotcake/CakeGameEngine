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
 * A range between two integers
 * @author Aaron Cake
 */
public class IntRange {

    static {
        ObjectXML.registerParser(IntRange.class, new iObjectXMLSerializer<IntRange>() {

            @Override
            public IntRange deserialize(ObjectXMLNode node) {
                return new IntRange(node.getAttrNumber("high").intValue(), node.getAttrNumber("low").intValue());
            }

            @Override
            public boolean serialize(ObjectXMLNode node, IntRange r) {
                node.setAttr("high", r.high);
                node.setAttr("low", r.low);
                return true;
            }
        });
    }

    private int high;
    private int low;

    public IntRange(int a) {
        low = high = a;
    }

    public IntRange(int a, int b) {
        if (a < b) {
            low = a;
            high = b;
        } else {
            high = a;
            low = b;
        }
    }

    public IntRange(int... nums) {
        if (nums.length == 0) {
            low = high = 0;
            return;
        }
        int h = nums[0], l = nums[0], n;
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
        if (obj == null || !(obj instanceof IntRange)) {
            return false;
        }
        IntRange r = (IntRange)obj;
        return r.high == high && r.low == low;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.high;
        hash = 37 * hash + this.low;
        return hash;
    }

    public IntRange(List<Integer> nums) {
        if (nums.isEmpty()) {
            low = high = 0;
            return;
        }
        int h = nums.get(0), l = nums.get(0), n;
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

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    public float getAverage() {
        return (high + low) / 2;
    }

    public void set(int a, int b) {
        if (a < b) {
            low = a;
            high = b;
        } else {
            high = a;
            low = b;
        }
    }

    public void set(int... nums) {
        if (nums.length == 0) {
            low = high = 0;
            return;
        }
        int h = nums[0], l = nums[0], n;
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

    public void include(int t) {
        if (t < low)
            low = t;
        if (t > high)
            high = t;
    }

    public IntRange union(IntRange other) {
        return new IntRange(Math.min(low, other.low), Math.max(high, other.high));
    }

    public boolean intersects(IntRange other) {
        return (other.low < high && other.low > low) || (other.high < high && other.high > low) ||
                (low < other.high && low > other.low) || (high < other.high && high > other.low);
    }

    public IntRange intersection(IntRange other) {
        if (other.low < high && other.low > low)
            return new IntRange(other.low, high);
        if (other.high < high && other.high > low)
            return new IntRange(low, other.high);
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
