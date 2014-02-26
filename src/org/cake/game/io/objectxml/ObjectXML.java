/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io.objectxml;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.cake.game.io.ResourceManager;
import org.cake.game.io.objectxml.ReferenceMap.Association;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *
 * @author Aaron Cake
 */
public class ObjectXML implements Iterable<ObjectXMLNode>  {

    private static DocumentBuilder docBuilder;
    private static Transformer transformer;
    protected static final String ReferenceIDName = "_refID";

    static {
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        } catch (ParserConfigurationException | TransformerConfigurationException ex) {}

    }

    protected static Map<Class, iObjectXMLSerializer> parsers = new HashMap<>();
    protected static Map<Class, iObjectXMLSerializer> serializerInstances = new HashMap<>();

    public static <T> void registerParser(Class<T> clazz, iObjectXMLSerializer<T> parser) {
        parsers.put(clazz, parser);
    }

    public static String serialize(Object obj) {
        ObjectXML xml = new ObjectXML();
        xml.addChild(obj);
        return xml.getXML();
    }

    public static Object unserialize(String src) {
        ObjectXML xml = new ObjectXML(src);
        return xml.firstChild().getAsObject();
    }

    public static void serializeToFile(Object obj, String ref) {
        ObjectXML xml = new ObjectXML();
        xml.addChild(obj);
        xml.save(ref);
    }

    public static Object unserializeFromFile(String ref) {
        ObjectXML xml = open(ref);
        return xml.firstChild().getAsObject();
    }
    
    public static void serializeToStream(Object obj, OutputStream os) {
        ObjectXML xml = new ObjectXML();
        xml.addChild(obj);
        xml.save(os);
    }

    public static Object unserializeFromStrean(InputStream is) {
        ObjectXML xml = new ObjectXML(is);
        return xml.firstChild().getAsObject();
    }

    public static ObjectXML open(String ref) {
        return new ObjectXML(ResourceManager.getDefault().getResource(ref).openRead());
    }

    private static Set<String> validTagNames = new HashSet(Arrays.asList(
            "number", "string", "char", "list", "object", "map", "entry", "boolean", "enum", "reference"
            ));

    protected Document doc;
    protected Element element;
    protected ReferenceMap<Element> ref;
    protected Map<String, Object> referenceValues;

    protected ObjectXML(Element ele, ObjectXML objxml) {
        if (!validTagNames.contains(ele.getNodeName()))
            throw ObjectXMLException.invalidType(ele);
        this.element = ele;
        this.doc = ele.getOwnerDocument();
        this.ref = objxml.ref;
        this.referenceValues = objxml.referenceValues;
    }

    public ObjectXML() {
        doc = docBuilder.newDocument();
        element = doc.createElement("objxml");
        doc.appendChild(element);
        ref = new ReferenceMap();
    }

    public ObjectXML(String source) {
        try {
            doc = docBuilder.parse(new InputSource(new StringReader(source)));
            element = doc.getDocumentElement();
            referenceValues = new HashMap();
        } catch (SAXException|IOException ex) {
            throw new ObjectXMLException(ex);
        }
    }

    public ObjectXML(InputStream is) {
        try {
            doc = docBuilder.parse(is);
            element = doc.getDocumentElement();
            referenceValues = new HashMap();
        } catch (SAXException | IOException ex) {
            throw new ObjectXMLException(ex);
        }
    }

    public ObjectXMLNode getNamedChild(String name) {
        NodeList nl = element.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element e = (Element)n;
                if (e.hasAttribute("name") && e.getAttribute("name").equals(name))
                    return new ObjectXMLNode(e, this);
            }
        }
        return null;
    }
    
    protected static Element getNamedChildElement(Element parent, String name) {
        NodeList nl = parent.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element e = (Element)n;
                if (e.hasAttribute("name") && e.getAttribute("name").equals(name))
                    return e;
            }
        }
        return null;
    }

    public ObjectXMLNode getIndexedChild(int index) {
        NodeList nl = element.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element e = (Element)n;
                if (e.hasAttribute("index") && e.getAttribute("index").equals(String.valueOf(index)))
                    return new ObjectXMLNode(e, this);
            }
        }
        return null;
    }

    public ObjectXMLNode firstChild() {
        NodeList nl = element.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element e = (Element)n;
                return new ObjectXMLNode(e, this);
            }
        }
        return null;
    }

    protected static String serialize64(Serializable o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
        } catch (IOException ex) {
            throw new ObjectXMLException(ex);
        }
        return new String( Base64Coder.encode( baos.toByteArray() ) );
    }

    protected static Object unserialize64(String s) {
        ObjectInputStream ois = null;
        try {
            byte [] data = Base64Coder.decode(s);
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (IOException | ClassNotFoundException ex) {
            throw new ObjectXMLException(ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {}
        }
    }
    
    protected static Class getSerializerClassFromTypeParameter(Class clazz) {
        for (Type interfac: clazz.getGenericInterfaces()) {
            if (interfac instanceof ParameterizedType) {
                ParameterizedType interfaceType = (ParameterizedType)interfac;
                Class<?> interfaceClass = (Class<?>)interfaceType.getRawType();
                if (interfaceClass.equals(iObjectXMLSerializableWith.class)) {
                    Type type = interfaceType.getActualTypeArguments()[0];
                    if (type instanceof Class)
                        return (Class)type;
                }
            }
        }
        return null;
    }
    
    protected static iObjectXMLSerializer getSerializerFromTypeParameter(Class clazz) {
        Class serializer = getSerializerClassFromTypeParameter(clazz);
        if (serializer == null)
            return null;
        iObjectXMLSerializer s = serializerInstances.get(clazz);
        if (s == null) {
            try {
                s = (iObjectXMLSerializer)serializer.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new ObjectXMLException(ex);
            }
            serializerInstances.put(serializer, s);
        }
        return s;
    }
    
    protected void autoSerializeObject(Element e, Object obj) {
        Class clazz = obj.getClass();
        for (Field f: clazz.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && !Modifier.isFinal(mod)) {
                try {
                    f.setAccessible(true);
                    Object val = f.get(obj);
                    if (val != null) {
                        Element valElement = makeElement(val);
                        valElement.setAttribute("name", f.getName());
                        e.appendChild(valElement);
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ObjectXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    protected Element makeEnumElement(Enum val) {
        Element ele = doc.createElement("enum");
        ele.setAttribute("class", val.getClass().getName());
        ele.setAttribute("value", val.name());
        return ele;
    }
    
    protected Element makeArrayElement(Object obj) {
        Element ele = doc.createElement("list");
        ele.setAttribute("class", obj.getClass().getName());
        int size = Array.getLength(obj);
        for (int i=0; i<size; i++)
            ele.appendChild(makeElement(Array.get(obj, i)));
        return ele;
    }

    protected Element makeElement(Object obj) {
        if (obj.getClass().isEnum())
            return makeEnumElement((Enum)obj);
        if (obj instanceof Number)
            return makeElement((Number)obj);
        if (obj instanceof String)
            return makeElement((String)obj);
        if (obj instanceof Character)
            return makeElement((Character)obj);
        if (obj instanceof Boolean)
            return makeElement((Boolean)obj);
        if (obj instanceof Collection)
            return makeElement((Collection)obj);
        if (obj.getClass().isArray())
            return makeArrayElement(obj);
        if (obj instanceof Map)
            return makeElement((Map)obj);
        if (obj instanceof Entry)
            return makeElement((Entry)obj);
        int i = ref.reference(obj);
        if (i != -1) {
           Element ele = doc.createElement("reference");
           ele.setAttribute("targetID", String.valueOf(i));
           return ele;
        } else {
            iObjectXMLSerializer parser = getSerializerFromTypeParameter(obj.getClass());
            if (parser == null)
                parser = parsers.get(obj.getClass());
            Element ele = doc.createElement("object");
            ele.setAttribute("class", obj.getClass().getName());
            if (parser == null) {
                if (obj instanceof iObjectXMLSerializable) {
                    autoSerializeObject(ele, obj);
                    ref.associate(obj, ele);
                } else if (obj instanceof Serializable) {
                    ele.setAttribute("serialized", serialize64((Serializable)obj));
                    ref.associate(obj, ele);
                } else {
                    throw new ObjectXMLException("Class '" + obj.getClass().getCanonicalName() + "' is unserializable.");
                }
            } else {
                parser.serialize(new ObjectXMLNode(ele, this), obj);
                ref.associate(obj, ele);
            }
            return ele;
        }
    }

    protected Element makeElement(Map m) {
        int i = ref.reference(m);
        if (i != -1) {
           Element ele = doc.createElement("reference");
           ele.setAttribute("targetID", String.valueOf(i));
           return ele;
        } else {
            Element ele = doc.createElement("map");
            ele.setAttribute("class", m.getClass().getName());
            for (Object key: m.keySet()) {
                Element e = doc.createElement("entry");
                Element keyElement = makeElement(key);
                keyElement.setAttribute("name", "key");
                Object val = m.get(key);
                Element valElement = makeElement(val);
                valElement.setAttribute("name", "value");
                e.appendChild(keyElement);
                e.appendChild(valElement);
                ele.appendChild(e);
            }
            return ref.associate(m, ele);
        }
    }

    protected Element makeElement(Entry e) {
        Element ele = doc.createElement("entry");
        Element keyElement = makeElement(e.getKey());
        keyElement.setAttribute("name", "key");
        Element valElement = makeElement(e.getValue());
        valElement.setAttribute("name", "value");
        ele.appendChild(keyElement);
        ele.appendChild(valElement);
        return ref.associate(e, ele);
    }

    protected Element makeElement(String s) {
        Element ele = doc.createElement("string");
        ele.setAttribute("value", s);
        return ele;
    }

    protected Element makeElement(Character c) {
        Element ele = doc.createElement("char");
        ele.setAttribute("value", String.valueOf(c));
        return ele;
    }

    protected Element makeElement(Boolean b) {
        Element ele = doc.createElement("boolean");
        ele.setAttribute("value", String.valueOf(b));
        return ele;
    }

    protected Element makeElement(Collection c) {
        int i = ref.reference(c);
        if (i != -1) {
           Element ele = doc.createElement("reference");
           ele.setAttribute("targetID", String.valueOf(i));
           return ele;
        } else {
            Element ele = doc.createElement("list");
            ele.setAttribute("class", c.getClass().getName());
            for (Object o: c) {
                ele.appendChild(makeElement(o));
            }
            return ref.associate(c, ele);
        }
    }

    protected static Number parseNumber(String str) {
        if (str.length() < 2)
            throw new ObjectXMLException("invalid number format, must be a number followed by a valid type indicator.");
        char last = str.charAt(str.length() - 1);
        String left = str.substring(0, str.length() - 1);
        try {
            switch (last) {
                case 'i':
                    return Integer.parseInt(left);
                case 'l':
                    return Long.parseLong(left);
                case 's':
                    return Short.parseShort(left);
                case 'b':
                    return Byte.parseByte(left);
                case 'f':
                    return Float.parseFloat(left);
                case 'd':
                    return Double.parseDouble(left);
                case 'I':
                    return new BigInteger(left);
                case 'D':
                    return new BigDecimal(left);
                case 'A':
                    return new AtomicLong(Long.parseLong(left));
                case 'a':
                    return new AtomicInteger(Integer.parseInt(left));
            }
        } catch (Exception ex) { }
        throw new ObjectXMLException("invalid number format, must be a number followed by a valid type indicator.");
    }
    protected static String formatNumber(Number n) {
        if (n instanceof Double) {
            return n.toString() + "d";
        } else if (n instanceof Float) {
            return n.toString() + "f";
        } else if (n instanceof Integer) {
            return n.toString() + "i";
        } else if (n instanceof Long) {
            return n.toString() + "l";
        } else if (n instanceof Short) {
            return n.toString() + "s";
        } else if (n instanceof Byte) {
            return n.toString() + "b";
        } else if (n instanceof BigInteger) {
            return n.toString() + "I";
        } else if (n instanceof BigDecimal) {
            return n.toString() + "D";
        } else if (n instanceof AtomicInteger) {
            return n.toString() + "a";
        } else if (n instanceof AtomicLong) {
            return n.toString() + "A";
        } else {
            throw new ObjectXMLException("Unsupported number type: " + n.getClass().getCanonicalName());
        }
    }

    protected static boolean parseBoolean(String str) {
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            throw new ObjectXMLException("invalid boolean format.");
        }
    }

    protected Element makeElement(Number n) {
        Element ele = doc.createElement("number");
        ele.setAttribute("value", formatNumber(n));
        return ele;
    }
    
    protected void resolveReferences() {
        if (ref != null) {
            for (Association<Element> a: ref.listAssociations()) {
                a.value.setAttribute(ReferenceIDName, String.valueOf(a.refID));
            }
        }
    }

    public void addChild(Object obj) {
        element.appendChild(makeElement(obj));
    }

    public void addChild(Object obj, String name) {
        Element e = makeElement(obj);
        e.setAttribute("name", name);
        element.appendChild(e);
    }

    public List<ObjectXMLNode> getChildren() {
        List<ObjectXMLNode> children = new ArrayList<>();
        NodeList nl = element.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                children.add(new ObjectXMLNode((Element)n, this));
            }
        }
        return children;
    }

    @Override
    public Iterator<ObjectXMLNode> iterator() {
        return new Iterator<ObjectXMLNode>() {

            private int index = 0;
            private NodeList nl = element.getChildNodes();

            @Override
            public boolean hasNext() {
                for (int i = index; i<nl.getLength(); i++) {
                    Node n = nl.item(i);
                    if (n instanceof Element)
                        return true;
                }
                return false;
            }

            @Override
            public ObjectXMLNode next() {
                for (int i = index; i<nl.getLength(); i++) {
                    Node n = nl.item(i);
                    if (n instanceof Element) {
                        index = i;
                        return new ObjectXMLNode((Element)n, ObjectXML.this);
                    }
                }
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public String getXML() {
        resolveReferences();
        DOMSource source = new DOMSource(element);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            throw new ObjectXMLException(ex);
        }
        return writer.toString();
    }

    public void save(String ref) {
        resolveReferences();
        DOMSource source = new DOMSource(element);
        StreamResult result = new StreamResult(ResourceManager.getDefault().getResource(ref).openWrite());
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            throw new ObjectXMLException(ex);
        }
    }

    public void save(OutputStream os) {
        resolveReferences();
        DOMSource source = new DOMSource(element);
        StreamResult result = new StreamResult(os);
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            throw new ObjectXMLException(ex);
        }
    }

}
