/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.cake.collections.MultiMap;
import org.cake.game.geom.iShape;

/**
 *
 * @author Aaron
 */
public class CollisionSystem {

    private MultiMap<aCollisionFilter, Entry> shapes;

    public CollisionSystem() {
        shapes = new MultiMap<>();
    }

    public void addShape(aCollisionFilter filter, iShape shape) {
        shapes.put(filter, new Entry(shape));
    }

    public void addShape(aCollisionFilter filter, iShape shape, iCollisionListener listener) {
        shapes.put(filter, new Entry(shape, listener));
    }

    public void checkCollisions() {
        List<aCollisionFilter> filters = new ArrayList<>(shapes.getKeys());
        for (int i=0; i<filters.size(); i++) {
            aCollisionFilter filter = filters.get(i);
            for (int a=i; a<filters.size(); a++) {
                aCollisionFilter f2 = filters.get(a);
                checkColisions(shapes.get(filter), shapes.get(f2), filter.detects(f2), f2.detects(filter));
            }
        }
    }

    private void checkColisions(Collection<Entry> e1, Collection<Entry> e2, boolean e1e2, boolean e2e1) {
        if (!e1e2 && !e2e1) return;
        for (Entry s1: e1) {
            for (Entry s2: e2) {
                if (s1.shape.intersects(s2.shape)) {
                    if (e1e2 && s1.listener != null)
                        s1.listener.collisionDetected(s1.shape, s2.shape);
                    if (e2e1 && s2.listener != null)
                        s2.listener.collisionDetected(s2.shape, s1.shape);
                }
            }
        }
    }

    private class Entry {
        iShape shape;
        iCollisionListener listener;
        Entry(iShape shape) {
            this.shape = shape;
            if (shape instanceof iCollisionListener)
                listener = (iCollisionListener)shape;
        }
        Entry(iShape shape, iCollisionListener listener) {
            this.shape = shape;
            this.listener = listener;
        }
    }
}
