/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.SwingUtilities;
import org.cake.game.exception.GameException;
import org.cake.game.input.iGameKeyListener;
import org.cake.game.input.iGameMouseListener;
import org.cake.game.reflect.Reflect;

/**
 *
 * @author Aaron
 */
public class AWTGameCanvas extends Game {

    public AWTGameCanvas(Container parent, Object layoutData, boolean allowResizing, boolean allowFullscreen) {

        wl = Reflect.createProxy(WindowListener.class, "windowClosed", this, "windowClosed");

        fullscreenFrame = new Frame("CakeGame");
        fullscreenFrame.setUndecorated(true);
        fullscreenFrame.addWindowListener(wl);

        glp = GLProfile.getDefault();
        glCaps = new GLCapabilities(glp);
        glCaps.setSampleBuffers(true);
        canvas = new GLCanvas(glCaps);
        getAnimator().add(canvas);

        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {


            }
        });

        this.allowResizing = allowResizing;
        this.allowFullscreen = allowFullscreen;
        this.layoutData = layoutData;
        changeRequested = fullscreen = false;
        this.parent = parent;

        this.inited = false;
    }

    private DisplayMode targetMode;
    private GLCanvas canvas;
    private GLCapabilities glCaps;
    private GLProfile glp;
    private Frame fullscreenFrame;
    private Container parent;
    private WindowListener wl;
    private boolean fullscreen, allowResizing, allowFullscreen, changeRequested, inited;
    private Object layoutData = null;
    private int width, height;

    /*public void setParent(Container c) {
        if (parent != null) {
            parent.remove(canvas);
        }
        parent = c;
        parent.add(canvas);
    }
*/
    public Canvas getCanvas() {
        return canvas;
    }

    private void windowClosed(WindowEvent e) {
        requestExit();
    }

    @Override
    protected void readyDisplay() {
        inited = true;
        if (fullscreen) {
            fullscreenFrame.add(canvas);
            fullscreenFrame.setVisible(true);
            Display.setFullscreenWindow(fullscreenFrame);
            if (!Display.isCurrentDisplayMode(targetMode)) {
                Display.setDisplayMode(targetMode);
            }
        } else {
            parent.add(canvas, layoutData);
        }
    }

    @Override
    protected void unloadDisplay() {
        if (fullscreen) {
            if (!Display.isCurrentDisplayModeDefault()) {
                Display.setDisplayModeDefault();
            }
            Display.setFullscreenWindow(null);
            fullscreenFrame.setVisible(false);
            fullscreenFrame.remove(canvas);
            if (!Display.isCurrentDisplayMode(targetMode)) {
                Display.setDisplayMode(targetMode);
            }
        } else {
            parent.remove(canvas);
        }
    }

    @Override
    protected void addGLEventListener(GLEventListener gle) {
        canvas.addGLEventListener(gle);
    }

    @Override
    public void setFullscreen(boolean b) throws GameException {
        setFullscreen(b, Display.getCurrentDisplayMode());
    }

    @Override
    public void setFullscreen(boolean b, final DisplayMode dm) throws GameException {
        if (allowFullscreen && !inited) {
            fullscreen = b;
            targetMode = dm;
            return;
        }
        if (allowFullscreen && !changeRequested) {
            final boolean hasFocus = canvas.hasFocus();
            if (fullscreen && !b) {
                animator.pause();
                changeRequested = true;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!Display.isCurrentDisplayModeDefault()) {
                            Display.setDisplayModeDefault();
                        }
                        Display.setFullscreenWindow(null);
                        fullscreenFrame.setVisible(false);
                        fullscreenFrame.remove(canvas);
                        fullscreen = false;
                        parent.add(canvas);
                        parent.validate();
                        canvas.setSize(width, height);
                        alertExitFullscreen();
                        animator.resume();
                        if (hasFocus) {
                            canvas.requestFocus();
                        }
                        changeRequested = false;

                    }
                });
            } else if (!fullscreen && b) {
                changeRequested = true;
                animator.pause();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        width = canvas.getWidth();
                        height = canvas.getHeight();
                        alertEnterFullscreen();
                        parent.remove(canvas);
                        fullscreen = true;
                        fullscreenFrame.add(canvas);
                        fullscreenFrame.setVisible(true);
                        Display.setFullscreenWindow(fullscreenFrame);
                        if (!Display.isCurrentDisplayMode(dm)) {
                            Display.setDisplayMode(dm);
                        }
                        animator.resume();
                        if (hasFocus) {
                            canvas.requestFocus();
                        }
                        changeRequested = false;
                    }
                });

            } else if (!Display.isCurrentDisplayMode(dm)) {
                changeRequested = true;
                animator.pause();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Display.setDisplayMode(dm);
                        animator.resume();
                        if (hasFocus) {
                            canvas.requestFocus();
                        }
                        changeRequested = false;
                    }
                });

            }
        }
    }

    @Override
    public void setDisplaySize(Dimension d) {
        if (allowResizing) {
            canvas.setPreferredSize(d);
            alertResize(d.width, d.height);
        }
    }

    @Override
    public void setDisplaySize(int width, int height) {
        if (allowResizing) {
            canvas.setPreferredSize(new Dimension(width, height));
            alertResize(width, height);
        }
    }

    @Override
    public void addListener(Object listener) {
        super.addListener(listener);
        if (listener instanceof MouseListener) {
            canvas.addMouseListener((MouseListener)listener);
        }
        if (listener instanceof MouseMotionListener) {
            canvas.addMouseMotionListener((MouseMotionListener)listener);
        }
        if (listener instanceof MouseWheelListener) {
            canvas.addMouseWheelListener((MouseWheelListener)listener);
        }
        if (listener instanceof KeyListener) {
            canvas.addKeyListener((KeyListener)listener);
        }
        if (listener instanceof iGameKeyListener) {
            keyboard.addListener((iGameKeyListener)listener);
        }
        if (listener instanceof iGameMouseListener) {
            mouse.addListener((iGameMouseListener)listener);
        }
    }

    @Override
    public void removeListener(Object listener) {
        super.addListener(listener);
        if (listener instanceof MouseListener) {
            canvas.removeMouseListener((MouseListener)listener);
        }
        if (listener instanceof MouseMotionListener) {
            canvas.removeMouseMotionListener((MouseMotionListener)listener);
        }
        if (listener instanceof MouseWheelListener) {
            canvas.removeMouseWheelListener((MouseWheelListener)listener);
        }
        if (listener instanceof KeyListener) {
            canvas.removeKeyListener((KeyListener)listener);
        }
        if (listener instanceof iGameKeyListener) {
            keyboard.removeListener((iGameKeyListener)listener);
        }
        if (listener instanceof iGameMouseListener) {
            mouse.removeListener((iGameMouseListener)listener);
        }
    }

    @Override
    public Dimension getDisplaySize() {
        return canvas.getSize();
    }

    @Override
    public int getDisplayHeight() {
        return canvas.getHeight();
    }

    @Override
    public int getDisplayWidth() {
        return canvas.getWidth();
    }

    @Override
    public boolean isFullscreen() {
        return fullscreen;
    }

    @Override
    public GLProfile getProfile() {
        return glp;
    }

}
