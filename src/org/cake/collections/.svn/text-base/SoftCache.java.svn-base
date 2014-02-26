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

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A cache using soft references.
 * Holds objects in memory until the garbage collector decides to
 * collect them to free up memory.
 * @author Aaron Cake
 */
public class SoftCache<K, V> implements iCache<K, V> {
    
    private Map<K, SoftReference<V>> cache;
    
    public SoftCache() {
        cache = new HashMap<>();
    }

    @Override
    public boolean has(K key) {
        SoftReference<V> ref = cache.get(key);
        if (ref == null)
            return false;
        V obj = ref.get();
        if (obj == null) {
            cache.remove(key);
            return false;
        }
        return true;
    }

    @Override
    public V get(K key) {
        SoftReference<V> ref = cache.get(key);
        if (ref == null)
            return null;
        V obj = ref.get();
        if (obj == null) {
            cache.remove(key);
            return null;
        }
        return obj;
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, new SoftReference(value));
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void remove(V obj) {
        Set<Entry<K, SoftReference<V>>> set = cache.entrySet();
        for(Entry<K, SoftReference<V>> entry: set) {
            V o = entry.getValue().get();
            if (o == null || o.equals(obj))
                set.remove(entry);
        }
    }
    
    @Override
    public void removeKey(K key) {
        cache.remove(key);
    }
    
}
