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

package org.cake.game;

import javax.media.opengl.GLAutoDrawable;
import org.cake.game.geom.Vector2;

/**
 * An interface that handles the layout of the viewport within the window.
 * @author Aaron Cake
 */
public interface iViewportMetrics {

    /**
     * Lays out the viewport
     * @param game the Game
     * @param drawable the current drawable
     * @param width the width of the drawable
     * @param height the height of the drawable
     */
    public void apply(Game game, GLAutoDrawable drawable, int width, int height);
    /**
     * Transforms a point from external (real) to internal (virtual) coordinates.
     * @param p the point to transform
     * @return the corresponding virtual coordinates.
     */
    public Vector2 transformPoint(Vector2 p);
    /**
     * Transforms a point from external (real) to internal (virtual) coordinates.
     * @param p the point to transform
     * @return the corresponding virtual coordinates.
     */
    public Vector2 inverseTransformPoint(Vector2 p);

    /**
     * Clips the point to the real coordinates containing the game
     * @param p the point to clip
     * @return the clipped point
     */
    public Vector2 clipPoint(Vector2 p);
    /**
     * Checks to see if something changed that requires the viewport to be re-laid out.
     * @return true if it is dirty, false otherwise
     */
    public boolean isDirty();

    public float getVirtualWidth();

    public float getVirtualHeight();
    
    public int getDisplayOffsetX();
    
    public int getDisplayOffsetY();
    
    public int getDisplayWidth();
    
    public int getDisplayHeight();
    
    public Vector2 getVirtualTopLeft();
    
    public Vector2 getVirtualBottomRight();
    
    public Vector2 getVirtualSize();
    
}
