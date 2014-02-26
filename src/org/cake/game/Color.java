/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.media.opengl.GL2;
import org.cake.game.Color.ColorSerializer;
import org.cake.game.exception.GameException;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.io.objectxml.ObjectXMLException;
import org.cake.game.io.objectxml.ObjectXMLNode;
import org.cake.game.io.objectxml.iObjectXMLSerializable;
import org.cake.game.io.objectxml.iObjectXMLSerializableWith;
import org.cake.game.io.objectxml.iObjectXMLSerializer;

/**
 * A color implementation.
 * Color constants are ImmutableColor so their values cannot be changed.
 * @author Aaron Cake
 */
public class Color implements iColor, iObjectXMLSerializableWith<ColorSerializer> {
    
    public static class ColorSerializer implements iObjectXMLSerializer<iColor> {
        @Override
        public iColor deserialize(ObjectXMLNode node) {
            String value = node.getAttr("value");
            if (node.getType().equals(Color.class)) {
                return new Color(value);
            } else if (node.getType().equals(ImmutableColor.class)) {
                return new ImmutableColor(value);
            } else {
                return null;
            }
        }

        @Override
        public boolean serialize(ObjectXMLNode node, iColor c) {
            node.setAttr("value", c.colorString());
            return true;
        }
    }

    public static void glClearColor(GL2 gl) {
        gl.glColor3f(1, 1, 1);
    }

    public static Color glGetCurrentColor(GL2 gl) {
        gl.glGetFloatv(GL2.GL_CURRENT_COLOR, carr, 0);
        return new Color(carr[0], carr[1], carr[2], carr[3]);
    }

    public static Color glGetCurrentColor(GL2 gl, Color target) {
        gl.glGetFloatv(GL2.GL_CURRENT_COLOR, carr, 0);
        return target.set(carr[0], carr[1], carr[2], carr[3]);
    }

    public static void glPushColor(GL2 gl) {
        float[] arr = new float[4];
        gl.glGetFloatv(GL2.GL_CURRENT_COLOR, arr, 0);
        colorStack.push(arr);
    }

    public static void glPopColor(GL2 gl) {
        if (!colorStack.isEmpty()) {
            gl.glColor4fv(colorStack.pop(), 0);
        }
    }

    /**
     * The ImmutableColor black
     */
    public static final ImmutableColor black = new ImmutableColor(0, 0, 0);
    /**
     * The ImmutableColor white
     */
    public static final ImmutableColor white = new ImmutableColor(1, 1, 1);
    /**
     * The ImmutableColor red
     */
    public static final ImmutableColor red = new ImmutableColor(1, 0, 0);
    /**
     * The ImmutableColor green
     */
    public static final ImmutableColor green = new ImmutableColor(0, 1, 0);
    /**
     * The ImmutableColor blue
     */
    public static final ImmutableColor blue = new ImmutableColor(0, 0, 1);
    /**
     * The ImmutableColor transparent white
     */
    public static final ImmutableColor transparentWhite = new ImmutableColor(1, 1, 1, 0);

    /**
     * The ImmutableColor transparent black
     */
    public static final ImmutableColor transparentBlack = new ImmutableColor(0, 0, 0, 0);
    /**
     * The ImmutableColor yellow
     */
    public static final ImmutableColor yellow = new ImmutableColor(1, 1, 0);
    /**
     * The ImmutableColor yellow
     */
    public static final ImmutableColor magenta = new ImmutableColor(1, 0, 1);
    /**
     * The ImmutableColor yellow
     */
    public static final ImmutableColor cyan = new ImmutableColor(0, 1, 1);
    /**
     * The ImmutableColor gray
     */
    public static final ImmutableColor gray = new ImmutableColor(0.5f, 0.5f, 0.5f);
    /**
     * The ImmutableColor light gray
     */
    public static final ImmutableColor lightgray = new ImmutableColor(0.75f, 0.75f, 0.75f);
    /**
     * The ImmutableColor dark gray
     */
    public static final ImmutableColor darkgray = new ImmutableColor(0.25f, 0.25f, 0.25f);
    /**
     * The ImmutableColor orange
     */
    public static final ImmutableColor orange = new ImmutableColor(1, 0.5f, 0);

    private static final Map<Integer, String> colorNames;
    private static final float[] carr = new float[4];
    private static final Stack<float[]> colorStack = new Stack<>();
    private static final Pattern hexPattern = Pattern.compile("^#[0-9a-f]{3,8}$");
    static {
        colorNames = new HashMap();
        for (Field f: Color.class.getFields()) {
            if (f.getType() == ImmutableColor.class && Modifier.isStatic(f.getModifiers()))
                try {
                colorNames.put(((iColor)f.get(null)).toInt(), f.getName().toLowerCase());
                } catch (Exception ex) { }
        }
    }

    public float r, g, b, a;
    
    private Color() {}

    /**
     * Create a color with the given RGBA values ranging from 0.0 to 1.0.
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component (opaqueness)
     */
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Create a opaque color with the given RGB values ranging from 0.0 to 1.0.
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     */
    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }

    /**
     * Make a copy of a color
     * @param other another color
     */
    public Color(iColor other) {
        this.r = other.getRed();
        this.g = other.getGreen();
        this.b = other.getBlue();
        this.a = other.getAlpha();
    }
    
    public Color(int rgba) {
        this.r = (0x000000ff & (rgba >> 24)) / 255f;
        this.g = ((0x00ff0000 & rgba) >> 16) / 255f;
        this.b = ((0x0000ff00 & rgba) >> 8) / 255f;
        this.a = (0x000000ff & rgba) / 255f;
    }
    
    public Color(String color) {
        color = color.toLowerCase();
        if (hexPattern.matcher(color).matches()) {
            if (color.length() == 4) {
                this.r = Integer.parseInt("" + color.charAt(1), 16) / 15f;
                this.g = Integer.parseInt("" + color.charAt(2), 16) / 15f;
                this.b = Integer.parseInt("" + color.charAt(3), 16) / 15f;
                this.a = 1;
            } else if (color.length() == 5) {
                this.r = Integer.parseInt("" + color.charAt(1), 16) / 15f;
                this.g = Integer.parseInt("" + color.charAt(2), 16) / 15f;
                this.b = Integer.parseInt("" + color.charAt(3), 16) / 15f;
                this.a = Integer.parseInt("" + color.charAt(4), 16) / 15f;
            } else if (color.length() == 7) {
                this.r = Integer.parseInt("" + color.charAt(1) + color.charAt(2), 16) / 255f;
                this.g = Integer.parseInt("" + color.charAt(3) + color.charAt(4), 16) / 255f;
                this.b = Integer.parseInt("" + color.charAt(5) + color.charAt(6), 16) / 255f;
                this.a = 1;
            } else if (color.length() == 9) {
                this.r = Integer.parseInt("" + color.charAt(1) + color.charAt(2), 16) / 255f;
                this.g = Integer.parseInt("" + color.charAt(3) + color.charAt(4), 16) / 255f;
                this.b = Integer.parseInt("" + color.charAt(5) + color.charAt(6), 16) / 255f;
                this.a = Integer.parseInt("" + color.charAt(7) + color.charAt(8), 16) / 255f;
            } else {
                throw new GameException("Invalid Color string:\"" + color + "\"");
            }
        } else if (colorNames.containsValue(color)) {
            for(Entry<Integer, String> entry: colorNames.entrySet()) {
                if (entry.getValue().equals(color)) {
                    int rgba = entry.getKey();
                    this.r = (0x000000ff & (rgba >> 24)) / 255f;
                    this.g = ((0x00ff0000 & rgba) >> 16) / 255f;
                    this.b = ((0x0000ff00 & rgba) >> 8) / 255f;
                    this.a = (0x000000ff & rgba) / 255f;
                }
            }
        } else {
            throw new GameException("Invalid Color string:\"" + color + "\"");
        }
    }

    @Override
    public void glBindColor(GL2 gl) {
        gl.glColor4f(r, g, b, a);
    }
    
    @Override
    public void glBindColorWithAlphaMask(GL2 gl, float alpha) {
        gl.glColor4f(r, g, b, a * alpha);
    }

    @Override
    public float getAlpha() {
        return a;
    }

    @Override
    public float getRed() {
        return r;
    }

    @Override
    public float getGreen() {
        return g;
    }

    @Override
    public float getBlue() {
        return b;
    }

    @Override
    public Color blend(iColor other) {
        return new Color((r + other.getRed()) / 2,
                    (g + other.getGreen()) / 2,
                    (b + other.getBlue()) / 2,
                    (a + other.getAlpha()) / 2
                    );
    }

    @Override
    public Color interpolate(iColor other, float t) {
        return new Color(r * (1 - t) + other.getRed() * t,
                    g * (1 - t) + other.getGreen() * t,
                    b * (1 - t) + other.getBlue() * t,
                    a * (1 - t) + other.getAlpha() * t
                    );
    }

    @Override
    public Color multiply(iColor other) {
        return new Color(r * other.getRed(),
                    g * other.getGreen(),
                    b * other.getBlue(),
                    a *  other.getAlpha()
                    );
    }

    @Override
    public void glBindColorOpaque(GL2 gl) {
        gl.glColor3f(r, g, b);
    }

    @Override
    public Color opaque() {
        return new Color(r, g, b, 1);
    }

    @Override
    public Color copy() {
        return new Color(r, g, b, a);
    }

    public ImmutableColor immutableCopy() {
        return new ImmutableColor(r, g, b, a);
    }


    /**
     * Set the RGBA components of this Color.
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component (opaqueness)
     * @return this Color
     */
    public Color set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    /**
     * Set the RGB components of this Color, and the alpha to 1.
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return this Color
     */
    public Color set(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
        return this;
    }

    /**
     * Blends this color with another color and stores the result in this Color.
     * @param other another color
     * @return this Color, after being blended
     */
    public Color blendLocal(iColor other) {
        this.r = (r + other.getRed()) / 2;
        this.g = (g + other.getGreen()) / 2;
        this.b = (b + other.getBlue()) / 2;
        this.a = (a + other.getAlpha()) / 2;
        return this;
    }

    /**
     * Interpolates this color with another color by the amount t and stores the result in this Color.
     * @param other another color
     * @param t the amount to interpolate (0.0 to 1.0)
     * @return this Color, after being interpolated
     */
    public Color interpolateLocal(iColor other, float t) {
        this.r = r * (1 - t) + other.getRed() * t;
        this.g = g * (1 - t) + other.getGreen() * t;
        this.b = b * (1 - t) + other.getBlue() * t;
        this.a = a * (1 - t) + other.getAlpha() * t;
        return this;
    }

    /**
     * Multiplies this color with another color and stores the result in this Color.
     * @param other another color
     * @return this Color, after being multiplied.
     */
    public Color multiplyLocal(iColor other) {
        this.r *= other.getRed();
        this.g *= other.getGreen();
        this.b *= other.getBlue();
        this.a *= other.getAlpha();
        return this;
    }

    /**
     * Sets the alpha value of this Color to 1.
     * @return this Color
     */
    public Color opaqueLocal() {
        this.a = 1;
        return this;
    }

    @Override
    public String toString() {
        return "Color[" + r + "," + g + "," + b + "," + a + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof iColor))
            return false;
        iColor c = (iColor)other;
        return r == c.getRed() && g == c.getGreen() && b == c.getBlue() && a == c.getAlpha();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Float.floatToIntBits(this.r);
        hash = 23 * hash + Float.floatToIntBits(this.g);
        hash = 23 * hash + Float.floatToIntBits(this.b);
        hash = 23 * hash + Float.floatToIntBits(this.a);
        return hash;
    }

    @Override
    public String colorString() {
        String name = colorNames.get(toInt());
        if (name == null)
            return toHexString();
        return name;
    }

    @Override
    public Color transparent() {
        return new Color(r, g, b, 0);
    }

    @Override
    public int toInt() {
        int i = (int)(r * 255);
        i <<= 8;
        i += (int)(g * 255);
        i <<= 8;
        i += (int)(b * 255);
        i <<= 8;
        i += (int)(a * 255);
        return i;
    }

    @Override
    public String toHexString() {
        String s = Integer.toHexString(toInt());
        while (s.length() < 8)
            s = "0" + s;
        if ((int)(a * 255) == 255) {
            s = s.substring(0, 6);
        }
        boolean pairs = true;
        for (int i=0; i<s.length(); i+=2)
            if (s.charAt(i) != s.charAt(i+1)) {
                pairs = false;
                break;
            }
        if (pairs) {
            String temp = "";
            for (int i=0; i<s.length(); i+=2)
                temp += s.charAt(i);
            s = temp;
        }
        return "#" + s;
}

    /**
     * A immutable implementation of iColor
     */
    public static class ImmutableColor implements iColor, iObjectXMLSerializableWith<ColorSerializer> {

        private float r, g, b, a;
        
        private ImmutableColor() {}
        
        /**
        * Create a color with the given RGBA values ranging from 0.0 to 1.0.
        * @param r the red component
        * @param g the green component
        * @param b the blue component
        * @param a the alpha component (opaqueness)
        */
       public ImmutableColor(float r, float g, float b, float a) {
           this.r = r;
           this.g = g;
           this.b = b;
           this.a = a;
       }
       
       public ImmutableColor(int rgba) {
            this.r = (0x000000ff & (rgba >> 24)) / 255f;
            this.g = ((0x00ff0000 & rgba) >> 16) / 255f;
            this.b = ((0x0000ff00 & rgba) >> 8) / 255f;
            this.a = (0x000000ff & rgba) / 255f;
        }

       /**
        * Create a opaque color with the given RGB values ranging from 0.0 to 1.0.
        * @param r the red component
        * @param g the green component
        * @param b the blue component
        */
       public ImmutableColor(float r, float g, float b) {
           this.r = r;
           this.g = g;
           this.b = b;
           this.a = 1;
       }

       /**
        * Make a copy of a color
        * @param other another color
        */
       public ImmutableColor(iColor other) {
           this.r = other.getRed();
           this.g = other.getGreen();
           this.b = other.getBlue();
           this.a = other.getAlpha();
       }
       
       public ImmutableColor(String color) {
            color = color.toLowerCase();
            if (hexPattern.matcher(color).matches()) {
                if (color.length() == 4) {
                    this.r = Integer.parseInt("" + color.charAt(1), 16) / 15f;
                    this.g = Integer.parseInt("" + color.charAt(2), 16) / 15f;
                    this.b = Integer.parseInt("" + color.charAt(3), 16) / 15f;
                    this.a = 1;
                } else if (color.length() == 5) {
                    this.r = Integer.parseInt("" + color.charAt(1), 16) / 15f;
                    this.g = Integer.parseInt("" + color.charAt(2), 16) / 15f;
                    this.b = Integer.parseInt("" + color.charAt(3), 16) / 15f;
                    this.a = Integer.parseInt("" + color.charAt(4), 16) / 15f;
                } else if (color.length() == 7) {
                    this.r = Integer.parseInt("" + color.charAt(1) + color.charAt(2), 16) / 255f;
                    this.g = Integer.parseInt("" + color.charAt(3) + color.charAt(4), 16) / 255f;
                    this.b = Integer.parseInt("" + color.charAt(5) + color.charAt(6), 16) / 255f;
                    this.a = 1;
                } else if (color.length() == 9) {
                    this.r = Integer.parseInt("" + color.charAt(1) + color.charAt(2), 16) / 255f;
                    this.g = Integer.parseInt("" + color.charAt(3) + color.charAt(4), 16) / 255f;
                    this.b = Integer.parseInt("" + color.charAt(5) + color.charAt(6), 16) / 255f;
                    this.a = Integer.parseInt("" + color.charAt(7) + color.charAt(8), 16) / 255f;
                } else {
                    throw new GameException("Invalid Color string:\"" + color + "\"");
                }
            } else if (colorNames.containsValue(color)) {
                for(Entry<Integer, String> entry: colorNames.entrySet()) {
                    if (entry.getValue().equals(color)) {
                        int rgba = entry.getKey();
                        this.r = (0x000000ff & (rgba >> 24)) / 255f;
                        this.g = ((0x00ff0000 & rgba) >> 16) / 255f;
                        this.b = ((0x0000ff00 & rgba) >> 8) / 255f;
                        this.a = (0x000000ff & rgba) / 255f;
                    }
                }
            } else {
                throw new GameException("Invalid Color string:\"" + color + "\"");
            }
        }

        @Override
        public String toString() {
            return "ImmutableColor[" + r + "," + g + "," + b + "," + a + "]";
        }
        
        @Override
        public void glBindColor(GL2 gl) {
            gl.glColor4f(r, g, b, a);
        }
        
        @Override
        public void glBindColorWithAlphaMask(GL2 gl, float alpha) {
            gl.glColor4f(r, g, b, a * alpha);
        }

        @Override
        public float getAlpha() {
            return a;
        }

        @Override
        public float getRed() {
            return r;
        }

        @Override
        public float getGreen() {
            return g;
        }

        @Override
        public float getBlue() {
            return b;
        }

        @Override
        public Color blend(iColor other) {
            return new Color((r + other.getRed()) / 2,
                    (g + other.getGreen()) / 2,
                    (b + other.getBlue()) / 2,
                    (a + other.getAlpha()) / 2
                    );
        }

        @Override
        public Color interpolate(iColor other, float t) {
            return new Color(r * (1 - t) + other.getRed() * t,
                    g * (1 - t) + other.getGreen() * t,
                    b * (1 - t) + other.getBlue() * t,
                    a * (1 - t) + other.getAlpha() * t
                    );
        }

        @Override
        public Color multiply(iColor other) {
            return new Color(r * other.getRed(),
                    g * other.getGreen(),
                    b * other.getBlue(),
                    a *  other.getAlpha()
                    );
        }

        @Override
        public void glBindColorOpaque(GL2 gl) {
            gl.glColor3f(r, g, b);
        }

        @Override
        public Color opaque() {
            return new Color(r, g, b, 1);
        }

        @Override
        public Color copy() {
            return new Color(r, g, b, a);
        }

        @Override
        public boolean equals(Object other) {
            if (other == null || !(other instanceof iColor))
                return false;
            iColor c = (iColor)other;
            return r == c.getRed() && g == c.getGreen() && b == c.getBlue() && a == c.getAlpha();
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Float.floatToIntBits(this.r);
            hash = 23 * hash + Float.floatToIntBits(this.g);
            hash = 23 * hash + Float.floatToIntBits(this.b);
            hash = 23 * hash + Float.floatToIntBits(this.a);
            return hash;
        }

        @Override
        public String colorString() {
            String name = colorNames.get(toInt());
            if (name == null)
                return toHexString();
            return name;
        }

        @Override
        public Color transparent() {
            return new Color(r, g, b, 0);
        }

        @Override
        public int toInt() {
            int i = (int)(r * 255);
            i <<= 8;
            i += (int)(g * 255);
            i <<= 8;
            i += (int)(b * 255);
            i <<= 8;
            i += (int)(a * 255);
            return i;
        }

        @Override
        public String toHexString() {
            String s = Integer.toHexString(toInt());
            while (s.length() < 8)
                s = "0" + s;
            if ((int)(a * 255) == 255) {
                s = s.substring(0, 6);
            }
            boolean pairs = true;
            for (int i=0; i<s.length(); i+=2)
                if (s.charAt(i) != s.charAt(i+1)) {
                    pairs = false;
                    break;
                }
            if (pairs) {
                String temp = "";
                for (int i=0; i<s.length(); i+=2)
                    temp += s.charAt(i);
                s = temp;
            }
            return "#" + s;
        }
        
        

    }
}
