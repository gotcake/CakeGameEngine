/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

/**
 *
 * @author Aaron
 */
public abstract class aCollisionFilter {

    private int id;

    public aCollisionFilter(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public abstract boolean detects(aCollisionFilter other);

}
