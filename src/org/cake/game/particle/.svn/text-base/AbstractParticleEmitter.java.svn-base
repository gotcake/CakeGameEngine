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

package org.cake.game.particle;

import java.util.ArrayDeque;
import java.util.Queue;
import javax.media.opengl.GL2;

/**
 * An abstract class that implements many of the features common to particle emitters
 * @author Aaron Cake
 */
public abstract class AbstractParticleEmitter implements iParticleEmitter {
    
    protected iParticleFactory factory;
    protected float offsetX, offsetY, initialDelay, spawnRate, breakLength, spawnLength, emitterLife;
    protected float maxParticleDistace, minParticleVelocity, maxParticleVelocity, minParticleVelocityAngle, maxParticleVelocityAngle;
    protected int maxParticles;
    protected Queue<iParticle> liveParticles, deadParticles;

    
    @Override
    public void init(GL2 gl,iParticleFactory factory) {
        this.factory = factory;
        this.factory.init(gl);
        liveParticles = new ArrayDeque<>(maxParticles);
        deadParticles = new ArrayDeque<>(maxParticles);
        for (int i=0; i<maxParticles; i++)
            deadParticles.offer(factory.createParticle(gl));
    }

    @Override
    public void destroy(GL2 gl) {
        liveParticles.clear();
        deadParticles.clear();
        liveParticles = null;
        deadParticles = null;
        factory.destroy(gl);
    }

    @Override
    public void update(float delta, float ax, float ay) {
        
    }

    @Override
    public void draw(GL2 gl) {
        gl.glTranslatef(offsetX, offsetY, 0);
        for (iParticle particle: liveParticles) {
            particle.draw(gl);
        }
        gl.glTranslatef(-offsetX, -offsetY, 0);
    }

}
