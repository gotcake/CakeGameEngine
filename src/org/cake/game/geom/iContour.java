/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom;

import java.util.List;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;

/**
 * Represents a single connected loop of points.
 * @author Aaron Cake
 */
public interface iContour {

    /**
     * Get the points of this contour
     * @return the points of this contour
     */
    public List<Vector2> getPoints();
    
    public boolean isClosed();
    
    
}
