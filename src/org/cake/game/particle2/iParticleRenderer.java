/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle2;

import javax.media.opengl.GL2;
import org.cake.game.collision.BoundingBox;
import org.cake.game.collision.iBoundingBox;

/**
 * An interface for a generic particle renderer
 * @author Aaron Cake
 */
public interface iParticleRenderer {
    
    public void init(GL2 gl);
    public void renderParticle(GL2 gl, Particle p, float emitterLife);
    public void beginRendering(GL2 gl);
    public void endRendering(GL2 gl);
    public iBoundingBox getParticleBounds();
    
}
