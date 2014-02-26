/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle2;

import javax.media.opengl.GL2;
import org.cake.game.BiColorGradient;
import org.cake.game.Color;
import org.cake.game.Graphics;
import org.cake.game.MultiColorGradient;
import org.cake.game.collision.BoundingBox;
import org.cake.game.collision.BoundingCircle;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;
import org.cake.game.geom.Rectangle;
import org.cake.game.geom.SimpleTransform2;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iShape;
import org.cake.game.iGradient1;
import org.cake.game.io.objectxml.iObjectXMLSerializable;

/**
 *
 * @author Aaron
 */
public class FillShapeParticleRenderer implements iParticleRenderer, iObjectXMLSerializable {
    
    public static FillShapeParticleRenderer createDefault() {
        FillShapeParticleRenderer r = new FillShapeParticleRenderer();
        r.setGradient(new BiColorGradient(Color.white, Color.black, true));
        r.setShape(new Rectangle(0, 0, 1, 1, (float)Math.PI/4));
        r.setInitialOpacity(1);
        r.setFinalOpacity(0);
        r.setParticleFade(FadeCurve.LinearOut);
        r.setEmitterFade(FadeCurve.None);
        r.setBlendingMode(Graphics.BlendingMode.BlendAlpha);
        return r;
    }
    
    private iShape shape;
    private iGradient1 gradient;
    private float initialOpacity;
    private float finalOpacity;
    private FadeCurve particleFade, emitterFade;
    private Graphics.BlendingMode blendingMode;
    private transient float radius;

    @Override
    public void init(GL2 gl) {
        
    }

    @Override
    public void renderParticle(GL2 gl, Particle p, float emitterLife) {
        float t = 1;
        if (particleFade != FadeCurve.None)
            t = FadeCurve.calculate(particleFade, p.life);
        if (emitterFade != FadeCurve.None)
            t = FadeCurve.calculate(emitterFade, emitterLife);
        if (t == 1)
            gradient.getColor(1 - p.life).glBindColor(gl);
        else
            gradient.getColor(1 - p.life).glBindColorWithAlphaMask(gl, (initialOpacity - finalOpacity) * t + finalOpacity);
        float scale = p.size / radius;
        gl.glPushMatrix();
        gl.glTranslatef(p.x, p.y, 0);
        gl.glScalef(scale, scale, 1);
        shape.fill(gl);
        gl.glPopMatrix();
    }

    @Override
    public void beginRendering(GL2 gl) {
        Graphics.BlendingMode.glPushBlendMode(gl);
        blendingMode.glBind(gl);
    }

    @Override
    public void endRendering(GL2 gl) {
        Graphics.BlendingMode.glPopBlendMode(gl);
    }

    @Override
    public iBoundingBox getParticleBounds() {
        iBoundingCircle c = shape.getBoundingCircle();
        float radius = c.getRadius();
        Vector2 center = c.getCenter();
        iBoundingBox b = shape.getAABB();
        return new BoundingBox(
                (b.getMinX() - center.x) / radius + center.x,
                (b.getMinY() - center.y) / radius + center.y,
                (b.getMaxX() - center.x) / radius + center.x,
                (b.getMaxY() - center.y) / radius + center.y
                );
    }

    /**
     * @return the shape
     */
    public iShape getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(iShape shape) {
        iBoundingCircle c = shape.getBoundingCircle();
        radius = c.getRadius();
        shape.translate(c.getCenter().inverse());
        this.shape = shape;
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
    public void setGradient(iGradient1 gradient) {
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
     * @return the fadeCurve
     */
    public FadeCurve getParticleFade() {
        return particleFade;
    }

    /**
     * @param fadeCurve the fadeCurve to set
     */
    public void setParticleFade(FadeCurve fadeCurve) {
        this.particleFade = fadeCurve;
    }

    /**
     * @return the emitterFade
     */
    public FadeCurve getEmitterFade() {
        return emitterFade;
    }

    /**
     * @param emitterFade the emitterFade to set
     */
    public void setEmitterFade(FadeCurve emitterFade) {
        this.emitterFade = emitterFade;
    }

    /**
     * @return the blendingMode
     */
    public Graphics.BlendingMode getBlendingMode() {
        return blendingMode;
    }

    /**
     * @param blendingMode the blendingMode to set
     */
    public void setBlendingMode(Graphics.BlendingMode blendingMode) {
        this.blendingMode = blendingMode;
    }
    
}
