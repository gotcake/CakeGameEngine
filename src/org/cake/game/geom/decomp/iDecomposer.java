/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom.decomp;

import java.util.List;
import org.cake.game.geom.Decomposition;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iContour;
import org.cake.game.geom.iConvex;

/**
 * An interface for an object that decomposes iContours into iDecompContours.
 * @author Aaron Cake
 */
public interface iDecomposer {
    public Decomposition decomposeMulti(List<iContour> contours);
    public Decomposition decomposeSingle(List<Vector2> contour);
}
