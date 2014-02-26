/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import org.cake.game.geom.iShape;

/**
 *
 * @author Aaron
 */
public interface iCollisionListener {

    public void collisionDetected(iShape s1, iShape s2);

}
