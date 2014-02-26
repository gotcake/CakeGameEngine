/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io.objectxml;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Aaron
 */
public class ObjectXMLNode extends ObjectXML {

    private static final Map<Character, Class> numberTypeMap; 

    static {
        numberTypeMap = new HashMap<>();
        numberTypeMap.put('i', Integer.class);
        numberTypeMap.put('l', Long.class);
        numberTypeMap.put('s', Short.class);
        numberTypeMap.put('b', Byte.class);
        numberTypeMap.put('f', Float.class);
        numberTypeMap.put('d', Double.class);
        numberTypeMap.put('D', BigDecimal.class);
        numberTypeMap.put('I', BigInteger.class);
        numberTypeMap.put('a', AtomicInteger.class);
        numberTypeMap.put('A', AtomicLong.class);
    }
    
    protected ObjectXMLNode(Element ele, ObjectXML objxml) {
        super(ele, objxml);
    }
    
    public boolean isObject() {
        return element.getNodeName().equals("object");
    }

    public Class getType() {
        switch (element.getNodeName()) {
            case "number":
                String value = element.getAttribute("value");
                if (value.length() < 2)
                    throw new ObjectXMLException("ObjectXML number node has invalid value attribute, must be a number followed by a valid type indicator.");
                char last = value.charAt(value.length() - 1);
                Class c = numberTypeMap.get(last);
                if (c == null)
                    throw new ObjectXMLException("ObjectXML number node has invalid value attribute, must be a number followed by a valid type indicator.");
                return c;
            case "string":
                return String.class;
            case "char":
                return Character.class;
            case "entry":
                return Entry.class;
            case "list":
            case "map":
            case "object":
                if (!element.hasAttribute("class"))
                    throw new ObjectXMLException("ObjectXML object node missing class attribute.");
                try {
                    return Class.forName(element.getAttribute("class"));
                } catch (ClassNotFoundException ex) {
                    throw new ObjectXMLException("ObjetXML object node has invalid class attribute. '" + element.getAttribute("class") + "' is not a defined class.");
                }
            default:
                throw ObjectXMLException.invalidType(element);
        }
    }

    public boolean isNumber() {
        return element.getNodeName().equals("number");
    }

    public boolean isMap() {
        return element.getNodeName().equals("map");
    }

    public boolean isEntry() {
        return element.getNodeName().equals("entry");
    }

    public boolean isString() {
        return element.getNodeName().equals("string");
    }

    public boolean isChar() {
        return element.getNodeName().equals("char");
    }

    public boolean isList() {
        return element.getNodeName().equals("list");
    }

    public boolean isBoolean() {
        return element.getNodeName().equals("boolean");
    }

    public String getName() {
        return element.hasAttribute("name") ? element.getAttribute("name") : null;
    }

    public Integer getIndex() {
        return element.hasAttribute("index") ? Integer.parseInt(element.getAttribute("index")) : null;
    }

    public Object getAsObject() {
        return getValue(element);
    }

    public boolean getAsBoolean() {
        return getBoolean(element, false);
    }

    public boolean hasAttribute(String name) {
        return element.hasAttribute(name);
    }

    public Number getAttrNumber(String name) {
        if (!element.hasAttribute(name))
            throw new ObjectXMLException("ObjectXML node does not have attribute: " + name);
        return parseNumber(element.getAttribute(name));
    }

    public boolean getAttrBoolean(String name) {
        if (!element.hasAttribute(name))
            throw new ObjectXMLException("ObjectXML node does not have attribute: " + name);
        return parseBoolean(element.getAttribute(name));
    }

    public String getAttr(String name) {
        if (!element.hasAttribute(name))
            throw new ObjectXMLException("ObjectXML node does not have attribute: " + name);
        return element.getAttribute(name);
    }

    public char getAttrChar(String name) {
        if (!element.hasAttribute(name))
            throw new ObjectXMLException("ObjectXML node does not have attribute: " + name);
        String val = element.getAttribute(name);
        if (val.length() != 1)
            throw new ObjectXMLException("ObjectXML: charater requires string of length 1.");
        return val.charAt(0);
    }


    public void setAttr(String name, Number n) {
        element.setAttribute(name, formatNumber(n));
    }

    public void setAttr(String name, boolean b) {
        element.setAttribute(name, String.valueOf(b));
    }

    public void setAttr(String name, String s) {
        element.setAttribute(name, s);
    }

    public void setAtrr(String name, char c) {
        element.setAttribute(name, String.valueOf(c));
    }

    private Object getValue(Element e) {
        switch (e.getNodeName()) {
            case "enum":
                return getEnum(e, true);
            case "number":
                return getNumber(e, true);
            case "string":
                return getString(e, true);
            case "char":
                return getChar(e, true);
            case "list":
                return getListValue(e, true);
            case "map":
                return getMap(e, true);
            case "entry":
                return getEntry(e, true);
            case "object":
                return getObject(e, true);
            case "boolean":
                return getBoolean(e, true);
            case "reference":
                return getReferenceValue(e.getAttribute("targetID"));
            default:
                throw ObjectXMLException.invalidType(e);
        }
    }
    
    private Object getListValue(Element e, boolean checked) {
        if (!checked && !e.getNodeName().equals("list"))
            throw ObjectXMLException.invalidType(e, "list");
        try {
            if (e.hasAttribute(ReferenceIDName) && referenceValues.containsKey(e.getAttribute(ReferenceIDName)))
                return referenceValues.get(e.getAttribute(e.getAttribute(ReferenceIDName)));
            Class clazz = Class.forName(e.getAttribute("class"));
            if (clazz.isArray()) {
                NodeList nl = e.getChildNodes();
                int count = 0;
                for (int i=0; i<nl.getLength(); i++)
                    if (nl.item(i) instanceof Element)
                        count++;
                Object arr = Array.newInstance(clazz.getComponentType(), count);
                if (e.hasAttribute(ReferenceIDName))
                referenceValues.put(e.getAttribute(ReferenceIDName), arr);
                for (int i=0, a=0; i<nl.getLength(); i++) {
                    Node n = nl.item(i);
                    if (n instanceof Element) {
                        //setArrayIndex(clazz, arr, i, (Element)n);
                        Array.set(arr, a++, getValue((Element)n));
                    }
                }
                return arr;
            } else {
                Collection c = (Collection)clazz.newInstance();
                if (e.hasAttribute(ReferenceIDName))
                    referenceValues.put(e.getAttribute(ReferenceIDName), c);
                NodeList nl = e.getChildNodes();
                for (int i=0; i<nl.getLength(); i++) {
                    Node n = nl.item(i);
                    if (n instanceof Element) {
                        c.add(getValue((Element)n));
                    }
                }
                return c;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Entry getEntry(Element e, boolean checked) {
        if (!checked && !e.getNodeName().equals("entry"))
            throw ObjectXMLException.invalidType(e, "entry");
        NodeList nl  = e.getChildNodes();
        Object key = getValue(ObjectXML.getNamedChildElement(e, "key"));
        Object value = getValue(ObjectXML.getNamedChildElement(e, "value"));
        return new Entry(key, value);
    }

    private Map getMap(Element e, boolean checked) {
        try {
            if (!checked && !e.getNodeName().equals("map"))
                throw ObjectXMLException.invalidType(e, "map");
            if (e.hasAttribute(ReferenceIDName) && referenceValues.containsKey(e.getAttribute(ReferenceIDName)))
                return (Map)referenceValues.get(e.getAttribute(e.getAttribute(ReferenceIDName)));
            Map map = (Map)Class.forName(e.getAttribute("class")).newInstance();
            if (e.hasAttribute(ReferenceIDName))
                referenceValues.put(e.getAttribute(ReferenceIDName), map);
            NodeList nl = e.getChildNodes();
            for (int i=0; i<nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n instanceof Element) {
                    Entry entry = getEntry((Element)n, false);
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            return map;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected Object autoDeSerializeObject(Element e, Class clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object object = constructor.newInstance();
            if (e.hasAttribute(ReferenceIDName))
                referenceValues.put(e.getAttribute(ReferenceIDName), object);
            for (Field f: clazz.getDeclaredFields()) {
                int mod = f.getModifiers();
                if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && !Modifier.isFinal(mod)) {
                    f.setAccessible(true);
                    Element valElement = getNamedChildElement(e, f.getName());
                    if (valElement != null)
                        f.set(object, getValue(valElement));
                }
            }
            return object;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected Object getReferenceValue(String rID) {
        if (referenceValues.containsKey(rID))
            return referenceValues.get(rID);
        Object obj = getReferenceValue(rID, doc.getDocumentElement());
        if (obj == null)
            throw new ObjectXMLException("Reference " + rID + " not found!");
        return obj;
    }
    
    protected Object getReferenceValue(String rID, Element root) {
        if (root.hasAttribute(ReferenceIDName) && root.getAttribute(ReferenceIDName).equals(rID)) {
            Object obj = getValue(root);
            referenceValues.put(rID, obj);
            return obj;
        }
        NodeList nl = root.getChildNodes();
        Object obj = null;
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                obj = getReferenceValue(rID, (Element)n);
                if (obj != null)
                    break;
            }
        }
        return obj;
    }

    private Object getObject(Element e, boolean checked) {
        if (!checked && !e.getNodeName().equals("object"))
            throw ObjectXMLException.invalidType(e, "object");
        if (!e.hasAttribute("class"))
            throw new ObjectXMLException("ObjectXML object node missing class attribute.");
        try {
            if (e.hasAttribute(ReferenceIDName) && referenceValues.containsKey(e.getAttribute(ReferenceIDName)))
                return referenceValues.get(e.getAttribute(e.getAttribute(ReferenceIDName)));
            Class clazz = Class.forName(e.getAttribute("class"));
            iObjectXMLSerializer parser = getSerializerFromTypeParameter(clazz);
            if (parser == null)
                parser = parsers.get(clazz);
            if (parser == null) {
                if (iObjectXMLSerializable.class.isAssignableFrom(clazz)) {
                    Object obj = autoDeSerializeObject(e, clazz);
                    if (e.hasAttribute(ReferenceIDName))
                        referenceValues.put(e.getAttribute(ReferenceIDName), obj);
                    return obj;
                } else if (Serializable.class.isAssignableFrom(clazz)) {
                    if (!e.hasAttribute("serialized"))
                        throw new ObjectXMLException("No ObjectXMLParser defined for class '" + clazz.getCanonicalName() + "' and no serialization data provided.");
                    Object obj = unserialize64(e.getAttribute("serialized"));
                    if (e.hasAttribute(ReferenceIDName))
                        referenceValues.put(e.getAttribute(ReferenceIDName), obj);
                    return obj;
                } else {
                    throw new ObjectXMLException("No ObjectXMLParser defined for unserializable class '" + clazz.getCanonicalName() + "'.");
                }
            } else {
                Object obj = parser.deserialize(new ObjectXMLNode(e, this));
                if (e.hasAttribute(ReferenceIDName))
                    referenceValues.put(e.getAttribute(ReferenceIDName), obj);
                return obj;
            }
        } catch (ClassNotFoundException ex) {
            throw new ObjectXMLException("ObjetXML object node has invalid class attribute. '" + e.getAttribute("class") + "' is not a defined class.");
        }
    }
    
    private static Object getEnum(Element e, boolean checked) {
        try {
            if (!checked && !e.getNodeName().equals("enum"))
                throw ObjectXMLException.invalidType(e, "enum");
            Class clazz = Class.forName(e.getAttribute("class"));
            String name = e.getAttribute("value");
            for (Object enumObj: clazz.getEnumConstants()) {
                if (((Enum)enumObj).name().equals(name))
                    return enumObj;
            }
            throw new RuntimeException("ObjectXML: Invalid value '"+name+"' for enum '"+clazz.getSimpleName()+"'");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean getBoolean(Element e, boolean checked) {
        if (!checked && !e.getNodeName().equals("boolean"))
            throw ObjectXMLException.invalidType(e, "boolean");
        if (!e.hasAttribute("value"))
            throw new ObjectXMLException("ObjectXML boolean node missing value attribute.");
        return parseBoolean(e.getAttribute("value"));
    }

    private static Number getNumber(Element e, boolean checked) {
        if (!checked && !e.getNodeName().equals("number"))
            throw ObjectXMLException.invalidType(e, "number");
        if (!e.hasAttribute("value"))
            throw new ObjectXMLException("ObjectXML number node missing value attribute.");
        return parseNumber(e.getAttribute("value"));
    }

    private static String getString(Element e, boolean checked) {
        if (!checked && !e.getNodeName().equals("string"))
            throw ObjectXMLException.invalidType(e, "string");
        if (!e.hasAttribute("value"))
            throw new ObjectXMLException("ObjectXML string node missing value attribute.");
        return e.getAttribute("value");

    }

    private static char getChar(Element e, boolean checked) {
        if (!checked && !e.getNodeName().equals("char"))
            throw ObjectXMLException.invalidType(e, "char");
        if (!e.hasAttribute("value"))
            throw new ObjectXMLException("ObjectXML char node missing value attribute.");
        String value = e.getAttribute("value");
        if (value.length() != 1)
            throw new ObjectXMLException("ObjectXML char has invalid value attribute. String of length 1 expected.");
        return value.charAt(0);
    }

    public Number getAsNumber() {
        return getNumber(element, false);
    }

    public String getAsString() {
        return getString(element, false);
    }

    public char getAsCharacter() {
        return getChar(element, false);
    }
    
    @Override
    public String toString() {
        String name = this.element.getTagName();
        return "ObjectXMLNode[" + name + ("object".equals(name)?" " + this.getType().getCanonicalName():"") + "]";
    }


}
