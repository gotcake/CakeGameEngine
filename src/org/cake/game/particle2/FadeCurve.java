/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle2;

/**
 *
 * @author Aaron
 */
public enum FadeCurve {
    None,
    LinearOut,
    LinearIn,
    LinearInOut,
    QuadraticOut,
    QuadraticIn,
    QuadraticInOut
    ;
    
    public static float calculate(FadeCurve type, float t) {
        switch (type) {
            case None: return 1;
            case LinearOut: return t;
            case LinearIn: return 1 - t;
            case LinearInOut: return 1 - (Math.abs(0.5f - t) * 2);
            case QuadraticOut: return (float)Math.sqrt(t);
            case QuadraticIn: return (float)Math.sqrt(1 - t);
            case QuadraticInOut: return (float)Math.sqrt(1 - (Math.abs(0.5f - t) * 2));
        }
        return 0;
    }
}
