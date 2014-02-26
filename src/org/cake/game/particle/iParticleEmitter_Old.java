/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle;

import javax.media.opengl.GL2;

/**
 *
 * @author Aaron
 */
public interface iParticleEmitter_Old {

    public void init(ParticleEffect parent);
    public void reset();
    public void setEmitterPos(float x, float y);
    public void setEmitterDelay(float seconds);
    public void setEmitterLife(float life);
    public void setCanDie(boolean canDie);
    public void setRepeats(boolean repeats);
    public boolean isFinished();
    public void setActive(boolean active);
    public void update(float delta);
    public void render(GL2 gl);
    public int currentParticles();

}
