/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.opengl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import org.cake.game.Game;
import org.cake.game.Image;
import org.cake.game.iGameRunnable;
import org.cake.game.iInvalidationListener;
import static javax.media.opengl.GL2.*;

/**
 * A class which can render to a FrameBufferObject
 * @author Aaron Cake
 */
public class FBORenderer {
    
    protected FBORenderer(GL2 gl, iGameRunnable r) {
        this.runnable = r;
        Game.currentGame().addListener(new iInvalidationListener() {
            @Override
            public void invalidated(Game g) {
                if (!destroyed) {
                    update(g.getGraphics().getGL());
                    if (runnable != null)
                        runCallback(g);
                }
            }
            @Override public void postInvalidation(Game g) { }
        });
    }

    public FBORenderer(GL2 gl, int width, int height, iGameRunnable r) {
        this(gl, r);
        update(gl, width, height);
        if (runnable != null)
            runCallback(Game.currentGame());
    }
    
    private void runCallback(Game g) {
        GL2 gl = g.getGraphics().getGL();
        beginRendering(gl);
        runnable.run(g);
        endRendering(gl);
    }
            
            
    private int fboID, colorTextureID, depthTextureID;
    private int textureWidth, textureHeight;
    private Texture texture;
    private Image image;
    private boolean destroyed = true;
    private iGameRunnable runnable;
    
    protected void update(GL2 gl) {
        update(gl, textureWidth, textureHeight);
    }
    
    protected void update(GL2 gl, int width, int height) {
        this.textureHeight = height;
        this.textureWidth = width;
        //allocate the framebuffer object ...
        int[] result = new int[1];
        gl.glGenFramebuffers(1, result, 0);
        fboID = result[0];
        gl.glBindFramebuffer(GL_FRAMEBUFFER, fboID);
        //allocate the colour texture ...
        gl.glGenTextures(1, result, 0);
        colorTextureID = result[0];
        gl.glBindTexture(GL_TEXTURE_2D, colorTextureID);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        //allocate the depth texture ...
        gl.glGenTextures(1, result, 0);
        depthTextureID = result[0];
        gl.glBindTexture(GL_TEXTURE_2D, depthTextureID);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, textureWidth, textureHeight, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, null);
        //attach the textures to the framebuffer
        gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTextureID, 0);
        gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTextureID, 0);
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);

        texture = new Texture(colorTextureID);
        //image = new Image(texture, textureWidth, textureHeight);
        destroyed = false;
    }
    
    public int getWidth() {
        return textureWidth;
    }
    
    public int getHeight() {
        return textureHeight;
    }

    public void beginRendering(GL2 gl) {
        gl.glPushAttrib(GL_TRANSFORM_BIT | GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT);
        //bind the framebuffer ...
        gl.glBindFramebuffer(GL_FRAMEBUFFER, fboID);
        gl.glPushAttrib(GL_VIEWPORT_BIT);
        gl.glViewport(0, 0, textureWidth, textureHeight);
        gl.glMatrixMode(GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, textureWidth, textureHeight, 0, -1, 1);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
    }

    public void endRendering(GL2 gl) {
        gl.glPopMatrix();
        gl.glMatrixMode(GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glPopAttrib();
        //unbind the framebuffer ...
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        gl.glPopAttrib();
    }

    public void renderRunnable(Game g, iGameRunnable r) {
        GL2 gl = g.getGraphics().getGL();
        beginRendering(gl);
        r.run(g);
        endRendering(gl);
    }

    public Texture getTexture() {
        return texture;
    }

    public Image getImage() {
        return image;
    }

    public void destroy(GL2 gl) {
        if (!destroyed) {
            gl.glDeleteFramebuffers(1, Buffers.newDirectIntBuffer(fboID));
            gl.glDeleteTextures(1, Buffers.newDirectIntBuffer(colorTextureID));
            gl.glDeleteTextures(1, Buffers.newDirectIntBuffer(depthTextureID));
            destroyed = true;
        }
    }

    public void draw(GL2 gl, float x, float y, float width, float height) {
        /*gl.glShadeModel(GL_SMOOTH);
        gl.glDisable(GL_LIGHTING);
        gl.glFrontFace(GL_CCW);
        gl.glDisable(GL_CULL_FACE);
        //disable depth test so that billboards can be rendered on top of each other ...
        gl.glDisable(GL_DEPTH_TEST);*/
        //
        gl.glPushAttrib(GL_TEXTURE_BIT);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, colorTextureID);
        //set the texture up to be used for painting a surface ...
        int textureTarget = GL_TEXTURE_2D;
        gl.glEnable(textureTarget);
        gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        gl.glTexParameteri(textureTarget, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.glTexParameteri(textureTarget, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        gl.glTexParameteri(textureTarget, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(textureTarget, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(x, y);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(x + width, y);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(x + width, y + height);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(x, y + height);
        gl.glEnd();
        //
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glPopAttrib();
    }
}
