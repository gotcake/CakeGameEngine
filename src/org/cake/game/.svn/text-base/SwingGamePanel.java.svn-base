/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.cake.game.exception.GameException;
import org.cake.game.input.iGameKeyListener;
import org.cake.game.input.iGameMouseListener;
import org.cake.game.reflect.Reflect;

/**
 *
 * @author Aaron
 */
public class SwingGamePanel extends Game {

    public SwingGamePanel(Container parent, Object layoutData, boolean allowResizing, boolean allowFullscreen) {

        wl = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                requestExit();
            }
        };

        fullscreenFrame = new JFrame("CakeGame");
        fullscreenFrame.setUndecorated(true);
        fullscreenFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fullscreenFrame.addWindowListener(wl);

        glp = GLProfile.getDefault();
        glCaps = new GLCapabilities(glp);
        glCaps.setSampleBuffers(true);
        panel = new GLJPanel(glCaps);
        panel.setFocusable(true);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                panel.requestFocus();
            }
        });

        getAnimator().add(panel);

        this.allowResizing = allowResizing;
        this.allowFullscreen = allowFullscreen;
        this.layoutData = layoutData;
        changeRequested = fullscreen = false;
        this.parent = parent;

        inited = false;

    }

    private GLJPanel panel;
    private GLCapabilities glCaps;
    private GLProfile glp;
    private JFrame fullscreenFrame;
    private Container parent;
    private WindowListener wl;
    private boolean fullscreen, allowResizing, allowFullscreen, changeRequested, inited;
    private DisplayMode targetMode;
    private Object layoutData = null;
    private int width, height;

    public JPanel getPanel() {
        return panel;
    }

    @Override
    protected void readyDisplay() {
        inited = true;
        if (fullscreen) {
            fullscreenFrame.add(panel);
            fullscreenFrame.setVisible(true);
            Display.setFullscreenWindow(fullscreenFrame);
            if (!Display.isCurrentDisplayMode(targetMode)) {
                Display.setDisplayMode(targetMode);
            }
        } else {
            parent.add(panel, layoutData);
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
            fullscreenFrame.remove(panel);
            if (!Display.isCurrentDisplayMode(targetMode)) {
                Display.setDisplayMode(targetMode);
            }
        } else {
            parent.remove(panel);
        }
    }

    @Override
    protected void addGLEventListener(GLEventListener gle) {
        panel.addGLEventListener(gle);
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
            final boolean hasFocus = panel.hasFocus();
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
                        fullscreenFrame.remove(panel);
                        fullscreen = false;
                        parent.add(panel);
                        parent.validate();
                        panel.setSize(width, height);
                        alertExitFullscreen();
                        animator.resume();
                        if (hasFocus) {
                            panel.requestFocus();
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
                        width = panel.getWidth();
                        height = panel.getHeight();
                        alertEnterFullscreen();
                        parent.remove(panel);
                        fullscreen = true;
                        fullscreenFrame.add(panel);
                        fullscreenFrame.setVisible(true);
                        Display.setFullscreenWindow(fullscreenFrame);
                        if (!Display.isCurrentDisplayMode(dm)) {
                            Display.setDisplayMode(dm);
                        }
                        animator.resume();
                        if (hasFocus) {
                            panel.requestFocus();
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
                            panel.requestFocus();
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
            panel.setPreferredSize(d);
            alertResize(d.width, d.height);
        }
    }

    @Override
    public void setDisplaySize(int width, int height) {
        if (allowResizing) {
            panel.setPreferredSize(new Dimension(width, height));
            alertResize(width, height);
        }
    }

    @Override
    public void addListener(Object listener) {
        super.addListener(listener);
        if (listener instanceof MouseListener) {
            panel.addMouseListener((MouseListener)listener);
        }
        if (listener instanceof MouseMotionListener) {
            panel.addMouseMotionListener((MouseMotionListener)listener);
        }
        if (listener instanceof MouseWheelListener) {
            panel.addMouseWheelListener((MouseWheelListener)listener);
        }
        if (listener instanceof MouseListener) {
            panel.addMouseListener((MouseListener)listener);
        }
        if (listener instanceof KeyListener) {
            panel.addKeyListener((KeyListener)listener);
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
            panel.removeMouseListener((MouseListener)listener);
        }
        if (listener instanceof MouseMotionListener) {
            panel.removeMouseMotionListener((MouseMotionListener)listener);
        }
        if (listener instanceof MouseWheelListener) {
            panel.removeMouseWheelListener((MouseWheelListener)listener);
        }
        if (listener instanceof MouseListener) {
            panel.removeMouseListener((MouseListener)listener);
        }
        if (listener instanceof KeyListener) {
            panel.removeKeyListener((KeyListener)listener);
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
        return panel.getSize();
    }

    @Override
    public int getDisplayHeight() {
        return panel.getHeight();
    }

    @Override
    public int getDisplayWidth() {
        return panel.getWidth();
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
