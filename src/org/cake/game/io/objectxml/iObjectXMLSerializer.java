/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io.objectxml;

/**
 *
 * @author Aaron
 */
public interface iObjectXMLSerializer<T> {

    /**
     * Callback to parse an ObjectXML node for an object
     * @param xml the thisObj node
     * @return the object
     */
    public T deserialize(ObjectXMLNode node);

    /**
     * Callback to produce an ObjectXML node from an object
     * @param node the thisObj node
     * @param obj the object
     * @return true if successful, false to revert to serialization if possible
     */
    public boolean serialize(ObjectXMLNode node, T obj);

}
