/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.cake.game.Color;
import org.cake.game.Color.ImmutableColor;
import org.cake.game.iColor;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.io.objectxml.iObjectXMLSerializableWith;

/**
 *
 * @author Aaron
 */
public class TypeParameterTest {
    
    public static void main(String[] args) {
        
        Class<?> clazz = Color.class;
        
        Type[] interfaces = clazz.getGenericInterfaces();
        System.out.println("interfaces: " + Arrays.toString(interfaces));
        for (Type interfac: interfaces) {
            if (interfac instanceof ParameterizedType) {
                ParameterizedType interfaceType = (ParameterizedType)interfac;
                Class<?> interfaceClass = (Class<?>)interfaceType.getRawType();
                System.out.println("interfaceClass: " + interfaceClass);
                Type[] typeParams = interfaceType.getActualTypeArguments();
                System.out.println("typeParams: " + Arrays.toString(typeParams));
            }
        }
        iColor color = new Color("#ff0f00");
        System.out.println(color);
        String xml = ObjectXML.serialize(color);
        
        //System.out.println(xml);
        
        iColor c = (iColor)ObjectXML.unserialize(xml);
        
        System.out.println(c.colorString());
        System.out.println(c);
        System.out.println(Integer.toHexString(c.toInt()));
        System.out.println(Color.red);
        System.out.println(Integer.toHexString(Color.red.toInt()));
    }
    
}
