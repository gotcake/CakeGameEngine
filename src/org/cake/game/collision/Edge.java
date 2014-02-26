/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import org.cake.game.geom.Line2;

/**
 * A class keeping track of an edge as well at it's index
 * @author Aaron Cake
 */
public class Edge {

    public Line2 line;
    public int indexA, indexB;

    public Edge(Line2 line, int a, int b) {
        this.line = line;
        indexA = a;
        indexB = b;
    }

    public Edge() {
        this.line = null;
        indexA = 0;
        indexB = 0;
    }

}
