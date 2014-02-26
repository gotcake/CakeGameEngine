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

import java.util.*;

/**
 * A class which allows you to map multiple values to a single key.
 * @author Aaron Cake
 */
public class MultiMap<K, V> {

    private HashMap<K, Set<V>> map;

    public MultiMap() {
        map = new HashMap<>();
    }

    public Set<K> getKeys() {
        return map.keySet();
    }

    public Set<V> get(K key) {
        Set<V> set = map.get(key);
        if (set == null)
            return Collections.EMPTY_SET;
        return set;
    }

    public void put(K key, V value) {
        Set<V> set = map.get(key);
        if (set == null) {
            set = new HashSet<>();
            map.put(key, set);
        }
        set.add(value);
    }

    public void putAll(K key, Collection<V> values) {
        Set<V> set = map.get(key);
        if (set == null) {
            set = new HashSet<>();
            map.put(key, set);
        }
        set.addAll(values);
    }

    public void putAll(K key, V... values) {
        Set<V> set = map.get(key);
        if (set == null) {
            set = new HashSet<>();
            map.put(key, set);
        }
        set.addAll(Arrays.asList(values));
    }

    public void removeValue(K key, V value) {
        Set<V> set = map.get(key);
        if (set != null)
            set.remove(value);
    }

    public void removeValue(V value) {
        for (Set<V> set: map.values()) {
            set.remove(value);
        }
    }

    public void removeKey(K key) {
        map.remove(key);
    }

    public void clear() {
        map.clear();
    }

}
