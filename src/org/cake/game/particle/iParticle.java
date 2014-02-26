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

import javax.media.opengl.GL2;

/**
 * Represents a single particle in a particle effect.
 * @author Aaron Cake
 */
public interface iParticle {
    
    public void draw(GL2 gl);
    
    public void update(float delta, float ax, float ay);
    
    public void init(float x, float y, float dx, float dy, float life, float emiiterLifeT);
    
    public boolean isDead();

}
