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

import java.util.List;
import javax.media.opengl.GL2;

/**
 * An interface for a class that produces particles.
 * @author Aaron Cake
 */
public interface iParticleFactory<E extends iParticle> {
    
    public void init(GL2 gl);
    
    public E createParticle(GL2 gl);
    
    public void destroy(GL2 gl);
    
}
