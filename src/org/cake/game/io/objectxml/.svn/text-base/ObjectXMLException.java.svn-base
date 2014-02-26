/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io.objectxml;

import org.cake.game.exception.GameException;
import org.w3c.dom.Element;

/**
 *
 * @author Aaron
 */
public class ObjectXMLException extends GameException {

    public static ObjectXMLException invalidType(Element xml, String expected) {
        return new ObjectXMLException("Wrong ObjectXML node. '" + expected + "' expected, '" + xml.getNodeName() + "' found.");
    }

    public static ObjectXMLException invalidType(Element xml) {
        return new ObjectXMLException("Invalid ObjectXML node '" + xml.getNodeName() + "' not supported.");
    }

    public ObjectXMLException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ObjectXMLException(Throwable cause) {
        super(cause);
    }

    public ObjectXMLException(String msg) {
        super(msg);
    }

}
