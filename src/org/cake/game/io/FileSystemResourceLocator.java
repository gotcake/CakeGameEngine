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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import org.cake.game.exception.ResourceException;

/**
 * Resolves resource requests as absolute file paths or paths relative to the classpath or relative to the root given.
 * @author Aaron Cake
 */
public class FileSystemResourceLocator implements iResourceLocator {
    
    private File root;
    
    public FileSystemResourceLocator(File root) {
        this.root = root;
    }
    
    public FileSystemResourceLocator() {
        root = null;
    }
    
    @Override
    public iResource getResource(String ref) {
        File f;
        if (root == null) {
            f = new File(ref);
        } else {
            f = new File(root, ref);
        }
        if (f.isDirectory())
            return null;
        return new FileResource(f);
    }

    @Override
    public boolean isWriteable() {
        FileSystem f = FileSystems.getDefault();
        return !f.isReadOnly();
    }
    
    public static class FileResource implements iResource {
        
        private File f;
        
        public FileResource(File f) {
            this.f = f;
        }

        @Override
        public String getLocation() {
            return f.toURI().toString();
        }

        @Override
        public InputStream openRead() {
            try {
                FileInputStream fin = new FileInputStream(f);
                return fin;
            } catch (Exception ex) {
                throw ResourceException.readFail(this);
            }
        }

        @Override
        public OutputStream openWrite() {
            try {
                FileOutputStream fout = new FileOutputStream(f, false);
                return fout;
            } catch (Exception ex) {
                throw ResourceException.writeFail(this);
            }
        }

        @Override
        public OutputStream openAppend() {
            try {
                FileOutputStream fout = new FileOutputStream(f, true);
                return fout;
            } catch (Exception ex) {
                throw ResourceException.writeFail(this);
            }
        }

        @Override
        public boolean delete() {
            try {
                return f.delete();
            } catch (Exception e) {
                throw ResourceException.deleteFail(this);
            }
        }

        @Override
        public boolean canRead() {
            return f.canRead();
        }

        @Override
        public boolean canWrite() {
            return f.canWrite();
        }

        @Override
        public long lastModified() {
            return f.lastModified();
        }

        @Override
        public long contentLength() {
            return f.length();
        }

        @Override
        public boolean exists() {
            return f.exists();
        }
        
        public String toString() {
            return "FileResource[" + f.getAbsolutePath() + "]";
        }

        @Override
        public String getExtension() {
            String abs = f.getAbsolutePath();
            return abs.substring(abs.lastIndexOf('.') + 1);
        }
        
    }
    
}
