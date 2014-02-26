/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle2;

import java.util.ArrayDeque;
import java.util.Iterator;
import javax.media.opengl.GL2;
import org.cake.game.FloatRange;
import org.cake.game.collision.BoundingBox;

import static java.lang.Math.max;
import static java.lang.Math.min;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.geom.Vector2;
import org.cake.game.io.objectxml.iObjectXMLSerializable;

/**
 *
 * @author Aaron
 */
public class StaticParticleEmitter implements iParticleEmitter, iObjectXMLSerializable {
    
    public static StaticParticleEmitter createDefault() {
        StaticParticleEmitter emitter = new StaticParticleEmitter();
        emitter.setRenderer(FillShapeParticleRenderer.createDefault());
        emitter.setEmitterMode(EmitterMode.Burst);
        emitter.setGravity(60f);
        emitter.setWind(100.0f);
        emitter.setEmitterBurstBreakLength(new FloatRange(0.5f, 1));
        emitter.setEmitterBurstLength(new FloatRange(0.5f, 1));
        emitter.setEmitterInitialLife(new FloatRange(10, 10));
        emitter.setEmitterSpawnRate(new FloatRange(5, 7));
        emitter.setParticleEndSize(new FloatRange(60, 60));
        emitter.setParticleInitialSize(new FloatRange(10, 10));
        emitter.setParticleInitialLife(new FloatRange(5000, 5000));
        emitter.setParticleInitialXOffset(new FloatRange(0, 0));
        emitter.setParticleInitialYOffset(new FloatRange(0, 0));
        emitter.setParticleInitialXVelocity(new FloatRange(-200, -200));
        emitter.setParticleInitialYVelocity(new FloatRange(-50, -50));
        return emitter;
    }
    
    public enum EmitterMode {
        Burst,
        Continuous,
        Once
    }

    /**
     * @return the particleInitialSize
     */
    public FloatRange getParticleInitialSize() {
        return particleInitialSize;
    }

    /**
     * @param particleInitialSize the particleInitialSize to set
     */
    public void setParticleInitialSize(FloatRange particleInitialSize) {
        this.particleInitialSize = particleInitialSize;
    }

    /**
     * @return the particleEndSize
     */
    public FloatRange getParticleEndSize() {
        return particleEndSize;
    }

    /**
     * @param particleEndSize the particleEndSize to set
     */
    public void setParticleEndSize(FloatRange particleEndSize) {
        this.particleEndSize = particleEndSize;
    }
    
    // emitter configuration
    protected FloatRange particleInitialXOffset;
    protected FloatRange particleInitialYOffset;
    protected FloatRange particleInitialYVelocity;
    protected FloatRange particleInitialXVelocity;
    protected FloatRange particleInitialLife;
    protected FloatRange emitterInitialLife;
    protected FloatRange emitterBurstLength;
    protected FloatRange emitterBurstBreakLength;
    protected FloatRange emitterSpawnRate;
    protected FloatRange particleInitialSize;
    protected FloatRange particleEndSize;
    protected transient float emitterLife, emitterDLife;
    protected float gravity;
    protected float wind;
    protected transient ArrayDeque<Particle> liveParticles, waitingParticles;
    protected iParticleRenderer particleRenderer;
    protected EmitterMode emitterMode;
    protected float offsetX, offsetY;
    
    protected transient float spawnVal;
    protected transient float emitterBreak;
    protected transient boolean active;
    
    public StaticParticleEmitter() {
        liveParticles = new ArrayDeque<>();
        waitingParticles = new ArrayDeque<>();
        spawnVal = 0;
        emitterBreak = 0;
        emitterLife = 1;
        emitterDLife = 0;
        active = true;
    }
    
    public float getPositionX() {
        return offsetX;
    }
    
    public float getPositionY() {
        return offsetY;
    }
    
    public void setPosition(float offX, float offY) {
        offsetX = offX;
        offsetY = offY;
    }
    
    @Override
    public int getActiveParticleCount() {
        return liveParticles.size();
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public void setRenderer(iParticleRenderer renderer) {
        particleRenderer = renderer;
    }
    
    public void reset() {
        waitingParticles.addAll(liveParticles);
        liveParticles.clear();
        emitterLife = 1;
        emitterDLife = 1 / emitterInitialLife.randomRange();
        emitterBreak = 0;
    }

    @Override
    public void update(float delta) {
        
        
        // if in burst mode and particles are all dead, re-initialize emitter life
        if (emitterMode == EmitterMode.Burst && emitterLife <= -emitterBreak) {
            emitterLife = 1;
            float burstLength = emitterBurstLength.randomRange();
            emitterDLife = 1 / burstLength;
            emitterBreak = emitterBurstBreakLength.randomRange() / burstLength;
        }
        
        Iterator<Particle> iter = liveParticles.iterator();
        float ddy = delta * gravity;
        float ddx = delta * wind;
        while (iter.hasNext()) {
            Particle p = iter.next();
            p.life -= p.dlife * delta;
            if (p.life <= 0) { // move the particle to waiting
                waitingParticles.add(p);
                iter.remove();
            } else { // update the particle's position
                p.x += p.dx * delta;
                p.y += p.dy * delta;
                p.dx += ddx;
                p.dy += ddy;
                p.size += p.dsize * delta;
            }
        }
        
        if (emitterMode == EmitterMode.Once && emitterLife <= 0) return;
        
        // if alive or in continuous mode, 
        if (emitterMode == EmitterMode.Continuous || emitterLife > 0) {
            spawnVal += delta;
            spawnParticles();
        }
        
        // if not continuous, decrement life
        if (emitterMode != EmitterMode.Continuous)
            emitterLife -= emitterDLife * delta;
    }
    
    protected void spawnParticles() {
        float spawnThreshold = (1 / emitterSpawnRate.randomRange());
        while (spawnVal > spawnThreshold) {
            spawnVal -= spawnThreshold;
            Particle p;
            if (waitingParticles.isEmpty())
                p = new Particle();
            else
                p = waitingParticles.removeFirst();
            p.life = 1;
            p.x = particleInitialXOffset.randomRange();
            p.y = particleInitialYOffset.randomRange();
            p.dx = particleInitialXVelocity.randomRange();
            p.dy = particleInitialYVelocity.randomRange();
            p.dlife = 1000 / particleInitialLife.randomRange();
            p.size = particleInitialSize.randomRange();
            p.dsize = (particleEndSize.randomRange() - p.size) * p.dlife;
            liveParticles.add(p);
        }
    }

    @Override
    public void draw(GL2 gl) {
        gl.glTranslatef(offsetX, offsetY, 0);
        particleRenderer.beginRendering(gl);
        float life = emitterMode == EmitterMode.Continuous ? 1 : emitterLife;
        for (Particle p: liveParticles) {
            particleRenderer.renderParticle(gl, p, life);
        }
        particleRenderer.endRendering(gl);
        gl.glTranslatef(-offsetX, -offsetY, 0);
    }

    @Override
    public BoundingBox getMaxParticleBounds() {
        float maxLife = particleInitialLife.getHigh() / 1000;
        float minStartY = particleInitialYOffset.getLow();
        float minStartX = particleInitialXOffset.getLow();
        float maxStartY = particleInitialYOffset.getHigh();
        float maxStartX = particleInitialXOffset.getHigh();
        float minStartVY = particleInitialYVelocity.getLow();
        float minStartVX = particleInitialXVelocity.getLow();
        float maxStartVY = particleInitialYVelocity.getHigh();
        float maxStartVX = particleInitialXVelocity.getHigh();
        float minEndY = minStartY + minStartVY * maxLife + 0.5f * gravity * maxLife * maxLife;
        float minEndX = minStartX + minStartVX * maxLife + 0.5f * wind * maxLife * maxLife;
        float maxEndY = maxStartY + maxStartVY * maxLife + 0.5f * gravity * maxLife * maxLife;
        float maxEndX = maxStartX + maxStartVX * maxLife + 0.5f * wind * maxLife * maxLife;
        float maxX = max(maxStartX, maxEndX);
        float minX = min(minStartX, minEndX);
        float maxY = max(maxStartY, maxEndY);
        float minY = min(minStartY, minEndY);
        if (gravity > 0 && minStartVY < 0) { // will have a negative peak
            minY = min(minY,  minStartVY * minStartVY / -gravity / 2 + minStartY);
        }
        if (gravity < 0 && maxStartVY > 0) { // will have a positive peak
            maxY = max(maxY,  maxStartVY * maxStartVY / gravity / 2 + maxStartY);
        }
        if (wind > 0 && minStartVX < 0) { // will have a negative horizontal peak
            minX = min(minX,  minStartVX * minStartVX / -wind / 2 + minStartX);
        }
        if (wind < 0 && maxStartVX > 0) { // will have a positive horizontal peak
            maxX = max(maxX,  maxStartVX * maxStartVX / wind / 2 + maxStartX);
        }
        iBoundingBox pb = particleRenderer.getParticleBounds();
        float maxParticleSize = Math.max(particleInitialSize.getHigh(), particleEndSize.getHigh());
        return new BoundingBox(minX + offsetX + pb.getMinX() * maxParticleSize,
                minY + offsetY + pb.getMinY() * maxParticleSize,
                maxX + offsetX + pb.getMaxX() * maxParticleSize,
                maxY + offsetY + pb.getMaxY() * maxParticleSize);
    }
    
    /**
     * @return the particleInitialXOffset
     */
    public FloatRange getParticleInitialXOffset() {
        return particleInitialXOffset;
    }

    /**
     * @param particleInitialXOffset the particleInitialXOffset to set
     */
    public void setParticleInitialXOffset(FloatRange particleInitialXOffset) {
        this.particleInitialXOffset = particleInitialXOffset;
    }

    /**
     * @return the particleInitialYOffset
     */
    public FloatRange getParticleInitialYOffset() {
        return particleInitialYOffset;
    }

    /**
     * @param particleInitialYOffset the particleInitialYOffset to set
     */
    public void setParticleInitialYOffset(FloatRange particleInitialYOffset) {
        this.particleInitialYOffset = particleInitialYOffset;
    }

    /**
     * @return the particleInitialYVelocity
     */
    public FloatRange getParticleInitialYVelocity() {
        return particleInitialYVelocity;
    }

    /**
     * @param particleInitialYVelocity the particleInitialYVelocity to set
     */
    public void setParticleInitialYVelocity(FloatRange particleInitialYVelocity) {
        this.particleInitialYVelocity = particleInitialYVelocity;
    }

    /**
     * @return the particleInitialXVelocity
     */
    public FloatRange getParticleInitialXVelocity() {
        return particleInitialXVelocity;
    }

    /**
     * @param particleInitialXVelocity the particleInitialXVelocity to set
     */
    public void setParticleInitialXVelocity(FloatRange particleInitialXVelocity) {
        this.particleInitialXVelocity = particleInitialXVelocity;
    }

    /**
     * @return the particleInitialLife
     */
    public FloatRange getParticleInitialLife() {
        return particleInitialLife;
    }

    /**
     * @param particleInitialLife the particleInitialLife to set
     */
    public void setParticleInitialLife(FloatRange particleInitialLife) {
        this.particleInitialLife = particleInitialLife;
    }

    /**
     * @return the emitterInitialLife
     */
    public FloatRange getEmitterInitialLife() {
        return emitterInitialLife;
    }

    /**
     * @param emitterInitialLife the emitterInitialLife to set
     */
    public void setEmitterInitialLife(FloatRange emitterInitialLife) {
        this.emitterInitialLife = emitterInitialLife;
    }

    /**
     * @return the emitterBurstLength
     */
    public FloatRange getEmitterBurstLength() {
        return emitterBurstLength;
    }

    /**
     * @param emitterBurstLength the emitterBurstLength to set
     */
    public void setEmitterBurstLength(FloatRange emitterBurstLength) {
        this.emitterBurstLength = emitterBurstLength;
    }

    /**
     * @return the emitterBurstBreakLength
     */
    public FloatRange getEmitterBurstBreakLength() {
        return emitterBurstBreakLength;
    }

    /**
     * @param emitterBurstBreakLength the emitterBurstBreakLength to set
     */
    public void setEmitterBurstBreakLength(FloatRange emitterBurstBreakLength) {
        this.emitterBurstBreakLength = emitterBurstBreakLength;
    }

    /**
     * @return the emitterSpawnRate
     */
    public FloatRange getEmitterSpawnRate() {
        return emitterSpawnRate;
    }

    /**
     * @param emitterSpawnRate the emitterSpawnRate to set
     */
    public void setEmitterSpawnRate(FloatRange emitterSpawnRate) {
        this.emitterSpawnRate = emitterSpawnRate;
    }

    /**
     * @return the gravity
     */
    public float getGravity() {
        return gravity;
    }

    /**
     * @param gravity the gravity to set
     */
    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    /**
     * @return the wind
     */
    public float getWind() {
        return wind;
    }

    /**
     * @param wind the wind to set
     */
    public void setWind(float wind) {
        this.wind = wind;
    }

    /**
     * @return the particleRenderer
     */
    public iParticleRenderer getParticleRenderer() {
        return particleRenderer;
    }

    /**
     * @param particleRenderer the particleRenderer to set
     */
    public void setParticleRenderer(iParticleRenderer particleRenderer) {
        this.particleRenderer = particleRenderer;
    }

    /**
     * @return the emitterMode
     */
    public EmitterMode getEmitterMode() {
        return emitterMode;
    }

    /**
     * @param emitterMode the emitterMode to set
     */
    public void setEmitterMode(EmitterMode emitterMode) {
        this.emitterMode = emitterMode;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }
    
}
