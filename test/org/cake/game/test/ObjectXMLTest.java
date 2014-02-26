/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import org.cake.game.BiColorGradient;
import org.cake.game.Color;
import org.cake.game.FloatRange;
import org.cake.game.geom.Line2;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.particle2.FillShapeParticleRenderer;
import org.cake.game.particle2.ImageParticleRenderer;
import org.cake.game.particle2.StaticParticleEmitter;
import org.cake.game.particle2.StaticParticleEmitter.EmitterMode;

/**
 *
 * @author Aaron
 */
public class ObjectXMLTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        HashMap map = new HashMap();
        map.put("Lmao", 1.0f);
        map.put(12, "Lolz");
        map.put('c', "pfft");
        
        LinkedList ll = new LinkedList();
        ll.add("Hello World");
        ll.add(4f);
        ll.add(10.5);
        ll.add(new int[] {1, 2, 3});
        ll.add(ll);
        ObjectXML.serializeToFile(ll, "resources/list.xml");
        ObjectXML.serializeToFile(ObjectXML.unserializeFromFile("resources/list.xml"), "resources/list.xml");

        BiColorGradient grad = new BiColorGradient(Color.black, Color.red, true);

        Line2 line = new Line2(1, 2, 3, 4);

        ObjectXML.serializeToFile(map, "resources/map.xml");
        ObjectXML.unserializeFromFile("resources/map.xml");
        ObjectXML.serializeToFile(grad, "resources/gradient.xml");
        ObjectXML.unserializeFromFile("resources/gradient.xml");
        ObjectXML.serializeToFile(line, "resources/line.xml");
        ObjectXML.unserializeFromFile("resources/line.xml");
        
        StaticParticleEmitter emitter = new StaticParticleEmitter();
        emitter.setRenderer(FillShapeParticleRenderer.createDefault());
        emitter.setEmitterMode(EmitterMode.Burst);
        emitter.setGravity(9.8f);
        emitter.setWind(0.0f);
        emitter.setEmitterBurstBreakLength(new FloatRange(0.5f, 1));
        emitter.setEmitterBurstLength(new FloatRange(0.5f, 1));
        emitter.setEmitterInitialLife(new FloatRange(8, 10));
        emitter.setEmitterSpawnRate(new FloatRange(100, 150));
        emitter.setParticleEndSize(new FloatRange(20, 25));
        emitter.setParticleInitialSize(new FloatRange(10, 15));
        emitter.setParticleInitialLife(new FloatRange(2, 2.5f));
        emitter.setParticleInitialXOffset(new FloatRange(-10, 10));
        emitter.setParticleInitialYOffset(new FloatRange(-10, 10));
        emitter.setParticleInitialXVelocity(new FloatRange(-5, 5));
        emitter.setParticleInitialXVelocity(new FloatRange(-5, 5));
        
        ObjectXML.serializeToFile(emitter, "resources/emitter.xml");
        ObjectXML.unserializeFromFile("resources/emitter.xml");
        
        System.out.println(grad.equals(ObjectXML.unserializeFromFile("resources/gradient.xml")));



    }

}
