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

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A class defining some sort of resource and methods to access it.
 * @author Aaron
 */
public interface iResource {
    public boolean exists();
    public String getLocation();
    public String getExtension();
    public InputStream openRead();
    public OutputStream openWrite();
    public OutputStream openAppend();
    public boolean delete();
    public boolean canRead();
    public boolean canWrite();
    public long lastModified();
    public long contentLength();
}
