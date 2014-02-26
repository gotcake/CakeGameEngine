/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

/**
 * A Listener that provides information about the game status and display.
 * Useful for programs that include other GUI components and need to respond to
 * Fullscreen and resize events to modify the display.
 * @author Aaron Cake
 */
public interface iGameLifecycleListener {

    /**
     * Called when the game starts
     * @param g the game
     */
    public void gameStarted(Game g);

    /**
     * Called when the game display is resized via game.setDisplaySize()
     * Depending on the implementation the actual size or the preferred size
     * of the game may be set. If the preferred size is set, the listener should
     * attempt to resize the GUI via a window.pack()
     * @param g the game
     * @param width the width of the game that was set
     * @param height the height of the game that was set
     */
    public void gameDisplayResized(Game g, int width, int height);

    public void gameEnteringFullscreen(Game g);

    public void gameExitedFullscreen(Game g);

    /**
     * Called when the game is stopped (exits)
     * @param g the game
     */
    public void gameFinished();

}
