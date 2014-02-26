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

import java.net.URL;

/**
 * A resource location that attempts to load resources via the ClassLoader.
 * @author Aaron Cake
 */
public class ClassLoaderResourceLocator implements iResourceLocator {
    
    private ClassLoader cl;
    
    public ClassLoaderResourceLocator(ClassLoader cl) {
        this.cl = cl;
    }
    
    public ClassLoaderResourceLocator() {
        cl = ClassLoader.getSystemClassLoader();
    }

    @Override
    public iResource getResource(String ref) {
        URL url = cl.getResource(ref);
        if (url == null)
            return null;
        return new URLResourceLocator.URLResource(url);
    }

    @Override
    public boolean isWriteable() {
        return false;
    }
    
}
