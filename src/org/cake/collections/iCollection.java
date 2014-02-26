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
 * An interface for a group of classes that have varying object reference security
 * @author Aaron Cake
 */
public interface iCollection<V> extends Iterable<V> {
    
    public boolean has(V obj);
    
    public void add(V obj);
    
    public void remove(V obj);
    
    public void clear();

}
