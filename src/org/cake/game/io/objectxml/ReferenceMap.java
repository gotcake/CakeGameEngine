/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io.objectxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A basic hash table that uses strict comparison ( == )
 * @author Aaron
 */
public class ReferenceMap<T> {
    
    private HashMap<Key, Association<T>> map;
    private int next = 1;
    
    public ReferenceMap() {
        map = new HashMap<>();
    }
    
    public int reference(Object obj) {
        Key k = new Key(obj);
        Association<T> value = map.get(k);
        if (value != null) {
            if (value.refID == 0)
                 value.refID = next++;   
            return value.refID;
        }
        map.put(k, new Association());
        return -1;
    }
    
    public T associate(Object obj, T val) {
        Key k = new Key(obj);
        Association<T> value = map.get(k);
        if (value != null) {
            value.value = val;
            return val;
        } else {
            map.put(k, new Association(val));
            return val;
        }
    }
    
    public T getAssociation(Object obj) {
        Key k = new Key(obj);
        Association<T> value = map.get(k);
        if (value != null)
            return value.value;
        else
            return null;
    }
    
    public Collection<Association<T>> listAssociations() {
        List<Association<T>> list = new ArrayList();
        for (Association<T> a: map.values()) {
            if (a.refID != 0 && a.value != null)
                list.add(a);
        }
        return list;
    }
    
    public void clear() {
        map.clear();
    }
    
    public static class Association<K> {
        int refID;
        int count;
        K value;
        public Association() {
            this.refID = 0;
            count = 0;
        }
        public Association(K val) {
            this.refID = 0;
            this.value = val;
            count = 0;
        }
    }
    
    private static class Key {
        Object obj;
        Key(Object obj) {
            this.obj = obj;
        }
        @Override public boolean equals(Object obj) {
            if (obj instanceof Key)
                return this.obj == ((Key)obj).obj;
            return false;
        }
        @Override public int hashCode() {
            return System.identityHashCode(obj);
        }
    }
}
