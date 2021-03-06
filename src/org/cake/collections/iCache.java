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

/**
 * A interface for a cache.
 * @author Aaron Cake
 */
public interface iCache<K, V> {
    
    /**
     * Checks to see if a cache contains a key.
     * @param key a key
     * @return true if it exists, false if not.
     */
    public boolean has(K key);
    
    /**
     * Gets a value specified by the given key, or returns null.
     * @param key a key
     * @return the value or null
     */
    public V get(K key);
    
    /**
     * Store a value in the cache under a given key
     * @param key a key
     * @param value some value to store
     */
    public void put(K key, V value);
    
    public void remove(V obj);
    
    public void removeKey(K key);
    
    /**
     * Clears the cache.
     */
    public void clear();
    
}
