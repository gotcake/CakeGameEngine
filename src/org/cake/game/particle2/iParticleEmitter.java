/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle2;

import javax.media.opengl.GL2;
import org.cake.game.collision.BoundingBox;

/**
 *
 * @author Aaron
 */
public interface iParticleEmitter {
    
    public void setRenderer(iParticleRenderer renderer);
    
    public void update(float delta);
    
    public void draw(GL2 gl);
    
    public int getActiveParticleCount();
    
    public BoundingBox getMaxParticleBounds();
    
}
