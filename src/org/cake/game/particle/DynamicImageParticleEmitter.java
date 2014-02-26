/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle;

import javax.media.opengl.GL2;
import org.cake.game.Color;
import org.cake.game.Graphics;

/**
 *
 * @author Aaron
 */
public class DynamicImageParticleEmitter extends ImageParticleEmitter {

    public static final int ALPHA_FADE_OUT = 2;
    public static final int FIZZLE_OUT = 1;

    public DynamicImageParticleEmitter() {
        canDie = true;
        fadeMode = FIZZLE_OUT;
        startX = startY = startVX = startVY = 0;
    }

    private float startX, startY, startVX, startVY, vx, vy;
    private int fadeMode;

    public void setFadeMode(int mode) {
        fadeMode = mode;
    }

    @Override
    public void reset() {
        super.reset();
        x = startX + parent.getOffsetX();
        y = startY + parent.getOffsetY();
        vx = startVX;
        vy = startVY;
    }

    public void setEmitterStartPos(float x, float y) {
        startX = x;
        startY = y;
    }

    public float getEmitterStartPosX() {
        return startX;
    }

    public float getEmitterStartPosY() {
        return startY;
    }

    public void setEmitterStartVelocity(float vx, float vy) {
        startVX = vx;
        startVY = vy;
    }

    public float getEmitterStartVelocityX() {
        return startVX;
    }

    public float getEmitterStartVelocityY() {
        return startVY;
    }

    @Override
    public void update(float delta) {
        x += vx * delta;
        y += vy * delta;
        vx += parent.getWind() * delta;
        vy += parent.getGravity() * delta;
        super.update(delta);
    }

    @Override
    public void render(GL2 gl) {
        if (fadeMode != ALPHA_FADE_OUT) {
            super.render(gl);
            return;
        }
        float et = 1 -  lifeLeft / myLife;
        Graphics.BlendingMode.glPushBlendMode(gl);
        blendMode.glBind(gl);
        Color.glPushColor(gl);
        img.beginUse(gl);
        float t, po2;
        for (Particle p: particles) {
            if (p.life > 0) {
                t = 1.0f - (p.life / p.initialLife);
                po2 = p.size/2;
                gradient.getColor(t).interpolate(Color.transparentBlack, et).glBindColor(gl);
                img.drawInUse(gl, p.x - po2, p.y - po2, p.size, p.size);
            }
        }
        img.endUse(gl);
        Color.glPopColor(gl);
        Graphics.BlendingMode.glPopBlendMode(gl);
    }

    @Override
    public void setCanDie(boolean b) {
        // ignore (must allways die)
    }

    @Override
    protected void spawnParticles(float delta) {
        float t = lifeLeft / myLife;
        wait -= delta;
        if (active) {
            lastSpawn += delta;
            if (wait <= 0) {
                quota += particleRate.randomRange() * lastSpawn;
                lastSpawn = 0;
                wait = waitPeriod.randomRange();
            }
            for (int i=0; i<quota && !waiting.isEmpty(); i++) {
                Particle p = waiting.poll();
                if (fadeMode == FIZZLE_OUT) {
                    float life = this.life.randomRange();
                    float ss = initSize.randomRange();
                    float es = endSize.randomRange();
                    p.reset(posX, posY, velX, velY, life * t, ss, (es - ss) * t + ss, life);
                } else {
                    p.reset(posX, posY, velX, velY, life, initSize, endSize);
                }
                p.x += x;
                p.y += y;
                particles.add(p);
                quota--;
            }
        }
    }





}
