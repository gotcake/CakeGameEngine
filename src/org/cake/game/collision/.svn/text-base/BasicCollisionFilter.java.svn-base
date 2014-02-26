/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.collision;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aaron
 */
public class BasicCollisionFilter extends aCollisionFilter {

    private List<Integer> detectors;

    public BasicCollisionFilter(int id) {
        super(id);
        detectors = new ArrayList<>();
    }

    public void addDetector(int id) {
        detectors.add(id);
    }

    public void addDetector(aCollisionFilter other) {
        detectors.add(other.getID());
    }

    @Override
    public boolean detects(aCollisionFilter other) {
        return detectors.contains(other.getID());
    }

}
