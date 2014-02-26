/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import org.cake.game.geom.*;

/**
 *
 * @author Aaron Cake
 */
public interface iCollidable {

    public aCollisionFilter getFilter();

    public int getID();

    public Object getData();

    public Shape getShape();

    public void setData(Object obj);

}
