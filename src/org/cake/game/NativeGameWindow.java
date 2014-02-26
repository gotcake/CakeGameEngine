/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import java.awt.Dimension;
import java.awt.DisplayMode;
import javax.media.nativewindow.util.InsetsImmutable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import org.cake.game.exception.GameException;
import org.cake.game.input.iGameKeyListener;
import org.cake.game.input.iGameMouseListener;

/**
 *
 * @author Aaron Cake
 */
public class NativeGameWindow extends Game {

    public NativeGameWindow(int width, int height) {
        glp = GLProfile.getDefault();
        glCaps = new GLCapabilities(glp);
        glCaps.setSampleBuffers(true);
        window = GLWindow.create(glCaps);
        window.setSize(width, height);
        window.setDefaultCloseOperation(GLWindow.WindowClosingMode.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDestroyNotify(WindowEvent e) {
                System.out.println("windowDestroyNotify");
                NativeGameWindow.this.requestExit();
            }

            @Override
            public void windowDestroyed(WindowEvent e) {
                System.out.println("windowDestroyed");
            }

        });
        window.setTitle("CakeGame");
    }

    private GLWindow window;
    private GLCapabilities glCaps;
    private GLProfile glp;

    @Override
    protected void readyDisplay() {
        //window.setAnimator(getAnimator());
        getAnimator().add(window);
        window.setVisible(true);
    }

    @Override
    protected void unloadDisplay() {
        window.setVisible(false);
    }

    @Override
    protected void addGLEventListener(GLEventListener gle) {
        window.addGLEventListener(gle);
    }

    @Override
    public void setFullscreen(final boolean b) throws GameException {
        if (window.isFullscreen() == b) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                window.setFullscreen(b);
            }
        }).start();
    }

    @Override
    public void setFullscreen(boolean b, DisplayMode dm) throws GameException {
        setFullscreen(b);
    }

    @Override
    public void setDisplaySize(Dimension d) {
        window.setSize(d.width, d.height);
    }

    @Override
    public void setDisplaySize(int width, int height) {
        window.setSize(width, height);
    }

    @Override
    public Dimension getDisplaySize() {
        return new Dimension(window.getWidth(), window.getHeight());
    }

    @Override
    public int getDisplayHeight() {
        return window.getHeight();
    }

    @Override
    public int getDisplayWidth() {
        return window.getWidth();
    }

    @Override
    public boolean isFullscreen() {
        return window.isFullscreen();
    }

    @Override
    public GLProfile getProfile() {
        return glp;
    }

    public String getTitle() {
        return window.getTitle();
    }

    public void setTitle(String title) {
        window.setTitle(title);
    }

    @Override
    public void addListener(Object listener) {
        super.addListener(listener);
        if (listener instanceof KeyListener) {
            window.addKeyListener((KeyListener)listener);
        }
        if (listener instanceof MouseListener) {
            window.addMouseListener((MouseListener)listener);
        }
        if (listener instanceof iGameMouseListener) {
            mouse.addListener((iGameMouseListener)listener);
        }
        if (listener instanceof iGameKeyListener) {
            keyboard.addListener((iGameKeyListener)listener);
        }
    }

    @Override
    public void removeListener(Object listener) {
        super.addListener(listener);
        if (listener instanceof KeyListener) {
           window.removeKeyListener((KeyListener)listener);
        }
        if (listener instanceof MouseListener) {
            window.removeMouseListener((MouseListener)listener);
        }
        if (listener instanceof iGameMouseListener) {
            mouse.removeListener((iGameMouseListener)listener);
        }
        if (listener instanceof iGameKeyListener) {
            keyboard.removeListener((iGameKeyListener)listener);
        }
    }

}
