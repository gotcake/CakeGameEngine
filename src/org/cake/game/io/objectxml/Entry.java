/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io.objectxml;

/**
 *
 * @author Aaron
 */
public class Entry {

    public Entry(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    private Object key, value;

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

}
