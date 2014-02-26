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

package org.cake.game;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import javax.media.opengl.GL2;
import org.cake.game.exception.ResourceException;
import org.cake.game.io.ResourceManager;
import org.cake.collections.SoftCache;
import org.cake.collections.iCache;
import org.cake.game.Image.Serializer;
import org.cake.game.io.iResource;
import org.cake.game.io.objectxml.ObjectXMLNode;
import org.cake.game.io.objectxml.iObjectXMLSerializer;
import org.cake.game.io.objectxml.iObjectXMLSerializableWith;


/**
 * A class representing an image that can be drawn to the screen.
 * All Images are immutable instances so that they may be reused and also
 * to cut down on the number of InvalidationListeners required.
 * @author Aaron Cake
 */
public class Image implements iObjectXMLSerializableWith<Serializer> {
    
    public static class Serializer implements iObjectXMLSerializer<Image> {
        @Override
        public Image deserialize(ObjectXMLNode node) {
            ObjectXMLNode parent = node.getNamedChild("parent");
            if (parent != null) {
                Image parentImg = get(node.getNamedChild("parent").getAsString());
                float x = node.getNamedChild("offx").getAsNumber().floatValue();
                float y = node.getNamedChild("offx").getAsNumber().floatValue();
                float width = node.getNamedChild("width").getAsNumber().floatValue();
                float height = node.getNamedChild("height").getAsNumber().floatValue();
                return new Image(parentImg, x, y, width, height);
            } else {
                return get(node.getNamedChild("source").getAsString());
            }
        }

        @Override
        public boolean serialize(ObjectXMLNode node, Image obj) {
            if (obj.parent == null) {
                node.addChild(obj.source, "source");
                return true;
            } else {
                node.addChild(obj.source, "parent");
                float x = (obj.texLeft - obj.parent.texLeft) * obj.texture.getWidth();
                float y = (obj.texTop - obj.parent.texTop) * obj.texture.getHeight();
                node.addChild(x, "offx");
                node.addChild(y, "offx");
                node.addChild(obj.width, "width");
                node.addChild(obj.height, "height");
                return true;
            }
        }
    }

    /**
     * Gets an Image given by the given source string. The ResourceManager is used to load the image.
     * @param source a valid resource reference string
     * @return the Image
     */
    public static Image get(String source) {
        if (imageCache.has(source)) {
            return imageCache.get(source);
        } else {
            Image img = new Image(source);
            imageCache.put(source, img);
            return img;
        }
    }

    private static iCache<String, Image> imageCache = new SoftCache();
    //private static Queue<Texture> destroyQueue = new ArrayDeque<>();

    private Texture texture;
    private Image parent;
    private Object textureData;
    private String source;
    private float width, height;
    private float texLeft, texTop, texRight, texBottom;
    private iInvalidationListener il;
    
    public Image(Texture texture, int width, int height) {
        this.texture = texture;
        int w = texture.getWidth();
        int h = texture.getHeight();
        texLeft = 0;
        texTop = 0;
        texRight = width / w;
        texBottom = height / h;
    }

    private Image(String source) {
        try {
            iResource resource = ResourceManager.getDefault().getResource(source);
            TextureData data = TextureIO.newTextureData(Game.currentGame().getProfile(), resource.openRead(), true, resource.getExtension());
            texture = TextureIO.newTexture(data);
            this.source = source;
            textureData = new SoftReference(data);
            width = data.getWidth();
            height = data.getHeight();
            TextureCoords texCoords = texture.getImageTexCoords();
            texLeft = texCoords.left();
            texTop = texCoords.top();
            texRight = texCoords.right();
            texBottom = texCoords.bottom();
            il = new iInvalidationListener() {
                @Override
                public void invalidated(Game g) {
                    SoftReference<TextureData> ref = (SoftReference)textureData;
                    TextureData data = ref.get();
                    if (data != null) {
                        texture = TextureIO.newTexture(data);
                    } else {
                        iResource resource = ResourceManager.getDefault().getResource(Image.this.source);
                        try {
                            data = TextureIO.newTextureData(Game.currentGame().getProfile(), resource.openRead(), true, resource.getExtension());
                            textureData = new SoftReference(data);
                        } catch (IOException ex) {
                            throw new ResourceException(ex);
                        }
                        texture = TextureIO.newTexture(data);
                    }
                }

                @Override
                public void postInvalidation(Game g) { }
            };
            Game.currentGame().addListener(il);
        } catch (IOException ex) {
            throw new ResourceException(ex);
        }
    }

    private Image(Image img, float x, float y, float width, float height) {
        if (img.parent != null)
            parent = img.parent;
        else
            parent = img;
        texture = parent.texture;
        source = parent.source;
        this.width = width;
        this.height = height;
        float srcX = x / (float)texture.getWidth();
        float srcY = y / (float)texture.getHeight();
        texLeft =  srcX  + img.texLeft;
        texTop = srcY  + img.texTop;
        texRight = width / (float)texture.getWidth() + img.texLeft + srcX;
        texBottom = height / (float)texture.getHeight() + img.texTop + srcY;
        il = new iInvalidationListener() {
            @Override
            public void invalidated(Game g) { }

            @Override
            public void postInvalidation(Game g) {
                texture = parent.texture;
            }
        };
        Game.currentGame().addListener(il);
    }

    public void beginUse(GL2 gl) {
        texture.enable(gl);
        texture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
    }

    public void drawInUse(GL2 gl, float x, float y) {
        gl.glTexCoord2f(texLeft, texTop);
        gl.glVertex2f(x, y);
        gl.glTexCoord2f(texRight, texTop);
        gl.glVertex2f(x + width, y);
        gl.glTexCoord2f(texRight, texBottom);
        gl.glVertex2f(x + width, y + height);
        gl.glTexCoord2f(texLeft, texBottom);
        gl.glVertex2f(x, y + height);
    }

    public void drawInUse(GL2 gl, float x, float y, float width, float height) {
        gl.glTexCoord2f(texLeft, texTop);
        gl.glVertex2f(x, y);
        gl.glTexCoord2f(texRight, texTop);
        gl.glVertex2f(x + width, y);
        gl.glTexCoord2f(texRight, texBottom);
        gl.glVertex2f(x + width, y + height);
        gl.glTexCoord2f(texLeft, texBottom);
        gl.glVertex2f(x, y + height);
    }

    public void endUse(GL2 gl) {
        gl.glEnd();
        texture.disable(gl);
    }

    public void draw(GL2 gl, float x, float y) {
        texture.enable(gl);
        texture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(texLeft, texTop);
            gl.glVertex2f(x, y);
            gl.glTexCoord2f(texRight, texTop);
            gl.glVertex2f(x + width, y);
            gl.glTexCoord2f(texRight, texBottom);
            gl.glVertex2f(x + width, y + height);
            gl.glTexCoord2f(texLeft, texBottom);
            gl.glVertex2f(x, y + height);
        gl.glEnd();
        texture.disable(gl);
    }

    public void draw(GL2 gl, float x, float y, float width, float height) {
        texture.enable(gl);
        texture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(texLeft, texTop);
            gl.glVertex2f(x, y);
            gl.glTexCoord2f(texRight, texTop);
            gl.glVertex2f(x + width, y);
            gl.glTexCoord2f(texRight, texBottom);
            gl.glVertex2f(x + width, y + height);
            gl.glTexCoord2f(texLeft, texBottom);
            gl.glVertex2f(x, y + height);
        gl.glEnd();
        texture.disable(gl);
    }

    public void drawSubImage(GL2 gl, float srcX, float srcY, float imgWidth, float imgHeight, float x, float y, float width, float height) {
        texture.enable(gl);
        texture.bind(gl);
        float left =  srcX / (float)texture.getWidth() + texLeft;
        float top = srcY / (float)texture.getHeight() + texTop;
        float right = imgWidth / (float)texture.getWidth() + texLeft + srcX;
        float bottom = imgHeight / (float)texture.getHeight() + texTop + srcY;
        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(left, top);
            gl.glVertex2f(x, y);
            gl.glTexCoord2f(right, top);
            gl.glVertex2f(x + width, y);
            gl.glTexCoord2f(right, bottom);
            gl.glVertex2f(x + width, y + height);
            gl.glTexCoord2f(left, bottom);
            gl.glVertex2f(x, y + height);
        gl.glEnd();
        texture.disable(gl);
    }
    
    public void drawSubImageInUse(GL2 gl, float srcX, float srcY, float imgWidth, float imgHeight, float x, float y, float width, float height) {
        float left =  srcX / (float)texture.getWidth() + texLeft;
        float top = srcY / (float)texture.getHeight() + texTop;
        float right = imgWidth / (float)texture.getWidth() + texLeft + srcX;
        float bottom = imgHeight / (float)texture.getHeight() + texTop + srcY;
        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(left, top);
            gl.glVertex2f(x, y);
            gl.glTexCoord2f(right, top);
            gl.glVertex2f(x + width, y);
            gl.glTexCoord2f(right, bottom);
            gl.glVertex2f(x + width, y + height);
            gl.glTexCoord2f(left, bottom);
            gl.glVertex2f(x, y + height);
        gl.glEnd();
    }

    public Image getSubImage(float offX, float offY, float width, float height) {
        return new Image(this, offX, offY, width, height);
    }

    public String getSource() {
        return source;
    }

    public TextureData getTextureData() {
        if (textureData instanceof Reference) {
            return ((Reference<TextureData>)textureData).get();
        }
        return (TextureData)textureData;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    public void destory(GL2 gl) {
        texture.destroy(gl);
        imageCache.remove(this);
        if (il != null)
            Game.currentGame().removeListener(il);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (il != null)
            Game.currentGame().removeListener(il);
    }
}
