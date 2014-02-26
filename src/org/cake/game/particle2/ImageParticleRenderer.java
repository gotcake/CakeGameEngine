/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle2;

import javax.media.opengl.GL2;
import org.cake.game.FloatRange;
import org.cake.game.Image;
import org.cake.game.MultiColorGradient;
import org.cake.game.collision.BoundingBox;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.iGradient1;
import org.cake.game.io.objectxml.iObjectXMLSerializable;

/**
 *
 * @author Aaron
 */
public class ImageParticleRenderer implements iParticleRenderer, iObjectXMLSerializable {
    
    private Image image;
    private iGradient1 gradient;
    private float initialOpacity;
    private float finalOpacity;
    private FadeCurve fadeMode;
    
    public ImageParticleRenderer() {}
    
    public ImageParticleRenderer(String img) {
        image = Image.get(img);
    }
    
    public ImageParticleRenderer(Image img) {
        image = img;
    }

    @Override
    public void init(GL2 gl) {
        
    }

    @Override
    public iBoundingBox getParticleBounds() {
        return new BoundingBox(-0.5f, -0.5f, 0.5f, 0.5f);
    }

    @Override
    public void renderParticle(GL2 gl, Particle p, float emitterLife) {
        float so2 = p.size/2;
        float lifeMult = 1;
        gradient.getColor(p.life).glBindColorWithAlphaMask(gl, lifeMult * (finalOpacity - initialOpacity) + initialOpacity);
        image.drawInUse(gl, p.x - so2, p.y - so2, p.size, p.size);
    }

    @Override
    public void beginRendering(GL2 gl) {
        gl.glColor4f(1, 1, 1, 1);
        image.beginUse(gl);
    }

    @Override
    public void endRendering(GL2 gl) {
        image.endUse(gl);
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the gradient
     */
    public iGradient1 getGradient() {
        return gradient;
    }

    /**
     * @param gradient the gradient to set
     */
    public void setGradient(MultiColorGradient gradient) {
        this.gradient = gradient;
    }

    /**
     * @return the initialOpacity
     */
    public float getInitialOpacity() {
        return initialOpacity;
    }

    /**
     * @param initialOpacity the initialOpacity to set
     */
    public void setInitialOpacity(float initialOpacity) {
        this.initialOpacity = initialOpacity;
    }

    /**
     * @return the finalOpacity
     */
    public float getFinalOpacity() {
        return finalOpacity;
    }

    /**
     * @param finalOpacity the finalOpacity to set
     */
    public void setFinalOpacity(float finalOpacity) {
        this.finalOpacity = finalOpacity;
    }

    /**
     * @return the fadeMode
     */
    public FadeCurve getFadeCurve() {
        return fadeMode;
    }

    /**
     * @param fadeCurve the fadeCurve to set
     */
    public void setFadeCurve(FadeCurve fadeCurve) {
        this.fadeMode = fadeCurve;
    }
    
}
