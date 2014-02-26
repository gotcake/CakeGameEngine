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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.cake.game.exception.ResourceException;

/**
 * A resource location that resolves resource requests based on the given string as an absolute URL or a URL relative to the root given.
 * @author Aaron
 */
public class URLResourceLocator implements iResourceLocator {
    
    private URI root;
    
    public URLResourceLocator(String root) {
        try {
            this.root = new URI(root);
        } catch (URISyntaxException ex) {
            throw ResourceException.invalidFormat(root);
        }
    }
    
    public URLResourceLocator() {
        root = null;
    }

    @Override
    public iResource getResource(String ref) {
        try {
            if (root != null) {
                URL url = root.resolve(ref).toURL();
                url.openConnection();
                return new URLResource(url);
            } else {
                return new URLResource(new URL(ref));
            }
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public boolean isWriteable() {
        return false;
    }
    
    public static class URLResource implements iResource {
        
        private URL url;
        
        public URLResource(URL url) {
            this.url = url;
        }
        
        @Override
        public String getLocation() {
            return url.toString();
        }

        @Override
        public InputStream openRead() {
            try {
                return url.openStream();
            } catch (IOException ex) {
                throw ResourceException.readFail(this);
            }
        }

        @Override
        public OutputStream openWrite() {
            throw ResourceException.writeFail(this);
        }

        @Override
        public OutputStream openAppend() {
            throw ResourceException.writeFail(this);
        }

        @Override
        public boolean delete() {
            throw ResourceException.deleteFail(this);
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }

        @Override
        public long lastModified() {
            try {
                return url.openConnection().getLastModified();
            } catch (IOException ex) {
                return 0;
            }
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public long contentLength() {
            try {
                return url.openConnection().getContentLengthLong();
            } catch (IOException ex) {
                return 0;
            }
        }
        
        public String toString() {
            return "URLResource[" + url.toString() + "]";
        }

        @Override
        public String getExtension() {
            String str = url.toString();
            int pos = str.lastIndexOf('.');
            if (pos < 0)
                return "";
            return str.substring(pos);
        }
        
    }
    
}
