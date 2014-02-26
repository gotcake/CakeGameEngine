/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import org.cake.game.geom.GeomUtil;
import org.cake.game.geom.SimpleTransform2;
import org.cake.game.geom.Transform2;
import org.cake.game.geom.Vector2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * A test that runs various tests on the core geometry classes.
 * @author Aaron Cake
 */
public class GeomCoreTest {

    private static final double EPSILON = GeomUtil.EPSILON;

    @Test
    public void testVector2() {

        Vector2 a = new Vector2(1, 2);
        Vector2 b = new Vector2(-1, 5);

        assertEquals("dot", 9, a.dot(b), 0);

        assertEquals("cross", 7, a.cross(b), 0);

        assertEquals("vectorTo", new Vector2(-2, 3), a.vectorTo(b));

        assertEquals("sum", b, a.sum(-2, 3));

        assertEquals("sum", b, a.sum(new Vector2(-2, 3)));

        assertEquals("add", new Vector2(5, 7), new Vector2(3, 4).add(2, 3));

        assertEquals("add", new Vector2(5, 7), new Vector2(3, 4).add(new Vector2(2, 3)));

        assertEquals("subtract", new Vector2(2, 2), new Vector2(3, 4).subtract(new Vector2(1, 2)));

        assertEquals("difference", new Vector2(2, 2), new Vector2(3, 4).difference(new Vector2(1, 2)));

        assertEquals("copy", a, a.copy());

        assertEquals("magnitude", (float)Math.hypot(1, 2), a.magnitude(), 0);

        assertEquals("magnitudeSquared", 5 , a.magnitudeSquared(), 0);

        assertEquals("distanceTo", (float)Math.hypot(-2, 3), a.distanceTo(b), 0);

        assertEquals("distanceToSquared", 13, a.distanceToSquared(b), 0);

        assertEquals("multiply", new Vector2(9, 6), new Vector2(6, 4).multiply(1.5f));

        assertEquals("product", new Vector2(9, 6), new Vector2(6, 4).product(1.5f));

        assertEquals("divide", new Vector2(3, 2), new Vector2(6, 4).divide(2));

        assertEquals("divided", new Vector2(3, 2), new Vector2(6, 4).divided(2));

    }

    @Test
    public void testTransform2() {

        assertEquals("translate", new Vector2(6, 6), Transform2.createTranslation(new Vector2(5, 2)).transform(new Vector2(1, 4)));

        assertEquals("translate", new Vector2(6, 6), Transform2.createTranslation(5, 2).transformLocal(new Vector2(1, 4)));

        Vector2 result =  Transform2.createRotation(GeomUtil.PIO2, 0, 0).transformLocal(new Vector2(10, 0));
        assertEquals("rotateX", 0, result.x, GeomUtil.epsilonOrder(3)); // this operation has low precision so an epsilon or order 3 is used.
        assertEquals("rotateY", 10, result.y, GeomUtil.epsilonOrder(3));

        assertEquals("scale", new Vector2(2.2f, 3.0f), Transform2.createScale(2, 3).transform(new Vector2(1.1f, 1.0f)));

        assertEquals("reflect", new Vector2(-2, 1), Transform2.createReflection(1, 2).transform(new Vector2(2, -1)));

        assertEquals("shear", new Vector2(5, 5), Transform2.createHorizontalShear(1).transform(new Vector2(0, 5)));


    }

    @Test
    public void testSimpleTransform2() {

        assertEquals("translate", new Vector2(6, 6), SimpleTransform2.createTranslation(new Vector2(5, 2)).transform(new Vector2(1, 4)));

        assertEquals("translate", new Vector2(6, 6), SimpleTransform2.createTranslation(5, 2).transformLocal(new Vector2(1, 4)));

        Vector2 result =  SimpleTransform2.createRotation(GeomUtil.PIO2, 0, 0).transformLocal(new Vector2(10, 0));
        assertEquals("rotateX", 0, result.x, GeomUtil.epsilonOrder(3)); // this operation has low precision so an epsilon or order 3 is used.
        assertEquals("rotateY", 10, result.y, GeomUtil.epsilonOrder(3));


    }
}
