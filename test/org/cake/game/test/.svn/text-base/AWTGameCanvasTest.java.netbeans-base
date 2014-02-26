/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import org.cake.game.*;
import org.cake.game.Graphics;
import org.cake.game.input.GameKeyEvent;
import org.cake.game.input.Key;
import org.cake.game.input.iGameKeyListener;

/**
 *
 * @author Aaron
 */
public class AWTGameCanvasTest implements iGameAdapter, iGameKeyListener {

    public static void main(String[] args) {

        final Frame frame = new Frame("CakeGame");
        frame.setLayout(new BorderLayout());

        final AWTGameCanvas canvas = new AWTGameCanvas(frame, BorderLayout.CENTER, true, true);

        canvas.setFullscreen(true);

        frame.add(new TextArea(), BorderLayout.SOUTH);

        AWTGameCanvasTest test = new AWTGameCanvasTest();

        canvas.setGameAdapter(test);
        canvas.addListener(test);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                canvas.requestExit();
            }
        });

        canvas.addListener(new iGameLifecycleListener() {

            @Override
            public void gameStarted(Game g) {

            }

            @Override
            public void gameDisplayResized(Game g, int width, int height) {
                System.out.println("Resize Request: (" + width + ", " +height + ")");
                frame.pack();
            }


            @Override
            public void gameFinished() {
                frame.setVisible(false);
                frame.dispose();
                System.exit(0);
            }

            @Override
            public void gameEnteringFullscreen(Game g) {

            }

            @Override
            public void gameExitedFullscreen(Game g) {
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        });

        frame.setVisible(true);
        canvas.start();
        canvas.setDisplaySize(800, 600);
        frame.setLocationRelativeTo(null);

    }

    @Override
    public void init(Game game) {

    }

    @Override
    public void update(Game game, float delta) {

    }

    @Override
    public void render(Game game, Graphics graphics) {
        graphics.drawString("Hello World", 100, 100);
    }

    @Override
    public boolean exitRequested(Game game) {
        return true;
    }

    @Override
    public void exit(Game game) {

    }

    @Override
    public void finished() {

    }

    @Override
    public void keyTyped(Game g, GameKeyEvent e) {

    }

    @Override
    public void keyPressed(Game g, GameKeyEvent e) {
        if (e.key == Key.Escape) {
            g.requestExit();
        } else if (e.key == Key.Num1) {
            g.setDisplaySize(g.getDisplayWidth() + 5, g.getDisplayHeight() + 5);
        } else if (e.key == Key.Num2) {
            g.setDisplaySize(g.getDisplayWidth() - 5, g.getDisplayHeight() - 5);
        }else if (e.key == Key.F1) {
            g.setFullscreen(!g.isFullscreen());
        }
    }

    @Override
    public void keyReleased(Game g, GameKeyEvent e) {

    }

}
