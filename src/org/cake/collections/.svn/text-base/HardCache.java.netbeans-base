
package org.cake.collections;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A cache that holds items directly in memory via a Map. Cached items will not be guaranteed until the cache is manually cleared.
 * @author Aaron Cake
 */
public class HardCache<K, V> implements iCache<K, V> {

    private Map<K, V> map;

    public HardCache() {
        map = new HashMap<>();
    }

    @Override
    public boolean has(K key) {
        return map.containsKey(key);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void remove(V obj) {
        Set<Map.Entry<K, V>> set = map.entrySet();
        for(Map.Entry<K, V> entry: set) {
            V o = entry.getValue();
            if (o == null || o.equals(obj))
                set.remove(entry);
        }
    }
    
    @Override
    public void removeKey(K key) {
        map.remove(key);
    }
}
