/*
    This file is part of CakeGame engine.

    CakeGame engine is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CakeGame engine is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CakeGame engine.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cake.collections;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An iCollection implementation using Weak references.
 * These references may be removed when the garbage collector detects no more references to the object.
 * @author Aaron Cake
 */
public class WeakList<V> implements iCollection<V> {
    
    private List<Reference<V>> list;
    
    public WeakList() {
        list = new ArrayList();
    }
    
    public WeakList(Collection<V> collection) {
        list = new ArrayList();
        for(V obj: collection)
            add(obj);
    }
    
    public WeakList(iCollection<V> collection) {
        list = new ArrayList();
        for(V obj: collection)
            add(obj);
    }
    

    @Override
    public boolean has(V obj) {
        for (Reference<V> ref: list) {
            V o = ref.get();
            if (o == null) {
                list.remove(ref);
            } else if (o.equals(obj))
                return true;
        }
        return false;
    }

    @Override
    public void add(V obj) {
        list.add(new WeakReference(obj));
    }

    @Override
    public void remove(V obj) {
        for (Reference<V> ref: list) {
            V o = ref.get();
            if (o == null) {
                list.remove(ref);
            } else if (o.equals(obj)) {
                list.remove(ref);
                return;
            }
        }
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            
            private int index = 0;

            @Override
            public boolean hasNext() {
                for (int i=index; i<list.size(); i++) {
                    Reference<V> ref = list.get(i);
                    if (ref.get() == null) {
                        list.remove(i);
                        index--;
                        continue;
                    } else
                        return true;
                }
                return false;
            }

            @Override
            public V next() {
                for (int i=index; i<list.size(); i++) {
                    Reference<V> ref = list.get(i);
                    if (ref.get() == null) {
                        list.remove(i);
                        i--;
                        continue;
                    } else {
                        index++;
                        return ref.get();
                    }
                }
                return null;
            }

            @Override
            public void remove() {
                list.remove(index);
            }
            
        };
    }

}
