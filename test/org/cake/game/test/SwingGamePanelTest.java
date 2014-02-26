/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import org.cake.game.*;
import org.cake.game.input.GameKeyEvent;
import org.cake.game.input.Key;
import org.cake.game.input.iGameKeyListener;

/**
 *
 * @author Aaron
 */
public class SwingGamePanelTest implements iGameAdapter, iGameKeyListener {

    public static void main(String[] args) {

        final JFrame frame = new JFrame("CakeGame");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        final SwingGamePanel panel = new SwingGamePanel(frame, BorderLayout.CENTER, true, true);

        //panel.setFullscreen(true);

        frame.add(new JTextArea(), BorderLayout.SOUTH);

        SwingGamePanelTest test = new SwingGamePanelTest();

        panel.setGameAdapter(test);
        panel.addListener(test);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                panel.requestExit();
            }
        });

        panel.addListener(new iGameLifecycleListener() {

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
        panel.start();
        panel.setDisplaySize(800, 600);
        frame.setLocationRelativeTo(null);
        panel.getPanel().requestFocus();

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
