/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import java.util.ArrayList;
import java.util.List;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.io.objectxml.ObjectXMLNode;
import org.cake.game.io.objectxml.iObjectXMLSerializable;
import org.cake.game.io.objectxml.iObjectXMLSerializableWith;
import org.cake.game.io.objectxml.iObjectXMLSerializer;

/**
 * A 1 dimensional gradient capable of applying different colors at specified locations along the [0-1] interval
 * Endpoints 0 and 1 default to transparentBlack
 * @author Aaron Cake
 */
public class MultiColorGradient implements iGradient1, iObjectXMLSerializable {

    private boolean repeats;
    private List<ColorLocation> list;
    private transient ColorLocation temp;

    public MultiColorGradient() {
        repeats = false;
        list = new ArrayList<>();
        temp = new ColorLocation(Color.transparentBlack, 0.0f);
        setColor(Color.transparentBlack, 0.0f);
        setColor(Color.transparentBlack, 1.0f);
    }

    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }

    public boolean getRepeats() {
        return repeats;
    }

    public void setColor(iColor c, float pos) {
        pos = pos < 0.0f ? 0.0f : (pos > 1.0f ? 1.0f : pos);
        Util.addSorted(list, new ColorLocation(c, pos), Util.REPLACE);
    }

    @Override
    public iColor getColor(float t) {
        if (repeats) {
            t = Math.abs(t) % 2;
            t = t > 1.0f ? 2.0f - t : t;
        } else
            t = t < 0.0f ? 0.0f : (t > 1.0f ? 1.0f : t);
        temp.location = t;
        int pos = Util.binaryFind(list, temp);
        ColorLocation cl = list.get(pos);
        if (cl.location == t)
            return cl.color;
        ColorLocation pre = list.get(pos - 1);
        t = (t - pre.location) / (cl.location - pre.location);
        return pre.color.interpolate(cl.color, t);
    }

    @Override
    public String toString() {
        return "MultiColorGradient" + Util.toString(list);
    }

    private static class ColorLocation implements Comparable<ColorLocation>, iObjectXMLSerializableWith<ColorLocation.Serializer> {
        
        private static class Serializer implements iObjectXMLSerializer<ColorLocation> {
            @Override
            public ColorLocation deserialize(ObjectXMLNode node) {
                Color c = new Color(node.getAttr("color"));
                return new ColorLocation(c, node.getAttrNumber("location").floatValue());
            }

            @Override
            public boolean serialize(ObjectXMLNode node, ColorLocation obj) {
                node.setAttr("location", obj.location);
                node.setAttr("color", obj.color.colorString());
                return true;
            }
        }

        public iColor color;
        public float location;

        public ColorLocation(iColor c, float loc) {
            color = c;
            location = loc;
        }

        @Override
        public int compareTo(ColorLocation o) {
            return location == o.location ? 0 : (location > o.location ? 1 : -1);
        }

        @Override
        public String toString() {
            return "(" + color.colorString() + "," + location + ")";
        }

    }


}
