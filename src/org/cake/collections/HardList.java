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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An iCollection List with hard references
 * @author Aaron Cake
 */
public class HardList<V> implements iCollection<V> {
    
    private List<V> list;
    
    public HardList() {
        list = new ArrayList();
    }
    
    public HardList(Collection<V> collection) {
        list = new ArrayList(collection);
    }
    
    public HardList(iCollection<V> collection) {
        list = new ArrayList();
        for (V obj: collection)
            list.add(obj);
    }

    @Override
    public boolean has(V obj) {
        return list.contains(obj);
    }

    @Override
    public void add(V obj) {
        list.add(obj);
    }

    @Override
    public void remove(V obj) {
        list.remove(obj);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Iterator<V> iterator() {
        return list.iterator();
    }

}
