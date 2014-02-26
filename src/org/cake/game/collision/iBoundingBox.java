/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import org.cake.game.geom.Vector2;

/**
 * An interface for an AABB
 * @author Aaron Cake
 */
public interface iBoundingBox {

    public float getMinY();

    public float getMinX();

    public float getMaxY();

    public float getMaxX();

}
