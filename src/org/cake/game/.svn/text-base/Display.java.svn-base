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

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

/**
 * A class providing easy access to display methods.
 * @author Aaron Cake
 */
public class Display {

    private static final GraphicsDevice defaultDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static final DisplayMode defaultMode = defaultDevice.getDisplayMode();


    public static DisplayMode getDefaultDisplayMode() {
        return defaultMode;
    }

    public static boolean isCurrentDisplayMode(DisplayMode dm) {
        return defaultDevice.getDisplayMode().equals(dm);
    }

    public static boolean isCurrentDisplayModeDefault() {
        return defaultDevice.getDisplayMode().equals(defaultMode);
    }

    public static DisplayMode getCurrentDisplayMode() {
        return defaultDevice.getDisplayMode();
    }

    public static void setDisplayMode(DisplayMode dm) {
        defaultDevice.setDisplayMode(dm);
    }

    public static void setDisplayModeDefault() {
        defaultDevice.setDisplayMode(defaultMode);
    }

    public static void setFullscreenWindow(Window w) {
        defaultDevice.setFullScreenWindow(w);
    }

    public static boolean isDisplayModeChangeSupported() {
        return defaultDevice.isDisplayChangeSupported();
    }

    public static boolean isFullscreenSupported() {
        return defaultDevice.isFullScreenSupported();
    }

    /**
     * Attempts to get the closest display mode to the one requested with the current bit depth and refresh rate.
     * Returns the current display mode if one could not be found.
     * @param width
     * @param height
     * @param maintainAspect
     * @return
     */
    public static DisplayMode getBestDisplayMode(int width, int height, boolean maintainAspect) {
        GraphicsDevice gd = defaultDevice;
        DisplayMode best = gd.getDisplayMode();
        int depth = best.getBitDepth();
        int rate = best.getRefreshRate();
        double targetAspect = (double)width/height;
        for (DisplayMode mode: gd.getDisplayModes()) {
            if (mode.getBitDepth() == depth && mode.getRefreshRate() == rate) {
                int curSizeDiff = Math.abs(width * height - mode.getHeight() * mode.getWidth());
                int bestSizeDiff = Math.abs(width * height - best.getHeight() * best.getWidth());
                if (maintainAspect) {
                    double curAspect = (double)width/height;
                    double bestAspect = (double)best.getWidth()/best.getHeight();
                    if (curAspect == targetAspect) {
                        if (bestAspect != targetAspect) {
                            best = mode;
                        } else if (curSizeDiff < bestSizeDiff) {
                            best = mode;
                        }
                    }
                } else if (curSizeDiff < bestSizeDiff) {
                    best = mode;
                }
            }
        }
        return best;
    }

    public static DisplayMode[] getDisplayModes() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes();
    }
}
