/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

/**
 *
 * @author Aaron
 */
public interface iConvex extends iContour, iShape {

    public float getArea();

    public Vector2 getCenter();

}
