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
package org.cake.game.io;

/**
 * Defines a location, or some method of getting a resource.
 * @author Aaron Cake
 */
public interface iResourceLocator {
    /**
     * Gets the requested resource.
     * @param ref the location of the resource.
     * @return the requested resource, or null if it does not exist on this locator.
     */
    public iResource getResource(String ref);
    
    
    /**
     * Checks to see if the locator is writable.
     * The first writable locator is used to return non-existent resources for creating.
     * @return 
     */
    public boolean isWriteable();
}
