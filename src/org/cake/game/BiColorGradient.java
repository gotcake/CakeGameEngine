/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import java.util.Objects;
import org.cake.game.io.objectxml.iObjectXMLSerializable;

/**
 * A simple bi-color 1 dimensional gradient with optional repeat
 * @author Aaron Cake
 */
public class BiColorGradient implements iGradient1, iObjectXMLSerializable {

    private boolean repeats;
    private iColor a, b;
    
    private BiColorGradient() {};

    public BiColorGradient(iColor a, iColor b, boolean repeat) {
        this.a = a;
        this.b = b;
        this.repeats = repeat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BiColorGradient other = (BiColorGradient) obj;
        if (this.repeats != other.repeats) {
            return false;
        }
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.repeats ? 1 : 0);
        hash = 43 * hash + Objects.hashCode(this.a);
        hash = 43 * hash + Objects.hashCode(this.b);
        return hash;
    }

    @Override
    public iColor getColor(float t) {
        if (repeats) {
            t = Math.abs(t) % 2.0f;
            t = t > 1.0f ? 2.0f - t : t;
        } else {
            t = t < 0.0f ? 0.0f : (t > 1.0f ? 1.0f : t);
        }
        return a.interpolate(b, t);
    }

    /**
     * @return the repeats
     */
    public boolean getRepeats() {
        return repeats;
    }

    /**
     * @param repeats the repeats to set
     */
    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }

    /**
     * @return the a
     */
    public iColor getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(iColor a) {
        this.a = a;
    }

    /**
     * @return the b
     */
    public iColor getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(iColor b) {
        this.b = b;
    }

}
