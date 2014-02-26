/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import org.cake.game.*;
import org.cake.game.input.GameKeyEvent;
import org.cake.game.input.Key;
import org.cake.game.input.iGameKeyListener;

/**
 *
 * @author Aaron
 */
public class NativeWindowTest implements iGameAdapter, iGameKeyListener {

    public static void main(String[] args) {

        NativeGameWindow window = new NativeGameWindow(800, 600);

        NativeWindowTest test = new NativeWindowTest();

        window.setViewportMetrics(new StretchViewportMetrics(800, 600));

        window.setGameAdapter(test);

        window.addListener(test);

        window.start();

    }


    @Override
    public void init(Game game) {
        System.out.println("Init.");
    }

    @Override
    public void update(Game game, float delta) {

    }

    @Override
    public void render(Game game, Graphics graphics) {

        graphics.drawString("Hello World!", 100, 100);

        graphics.drawElipse(400, 300, 400, 300);

    }

    @Override
    public boolean exitRequested(Game game) {
        System.out.println("Exit Requested.");
        return true;
    }

    @Override
    public void exit(Game game) {
        System.out.println("Exit.");
    }

    @Override
    public void finished() {
        System.out.println("Finished.");
    }

    @Override
    public void keyTyped(Game g, GameKeyEvent e) {

    }

    @Override
    public void keyPressed(Game g, GameKeyEvent e) {
        if (e.key == Key.Escape) {
            g.requestExit();
        }
        if (e.key == Key.F1) {
            g.setFullscreen(!g.isFullscreen());
        }
    }

    @Override
    public void keyReleased(Game g, GameKeyEvent e) {

    }

}
