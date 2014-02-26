package org.cake.jogl.test;


import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class FullscreenAWTTest implements KeyListener, WindowListener {
    
    public static void main(String[] args) {
        FullscreenAWTTest test = new FullscreenAWTTest(false);
        test.start();
    }
    
    private FPSAnimator animator;
    private GLCanvas canvas;
    private Frame windowFrame, fullscreenFrame;
    private GraphicsDevice screen;
    private boolean fullscreen;
    
    public FullscreenAWTTest(boolean fs) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        canvas = new GLCanvas(caps);
        animator = new FPSAnimator(canvas, 60);
        canvas.addGLEventListener(new CommonTest());
        canvas.addKeyListener(this);
        windowFrame = new Frame("FullscreenAWTTest");
        windowFrame.setSize(800, 600);
        fullscreenFrame = new Frame("FullscreenAWTTest");
        fullscreenFrame.setUndecorated(true);
        windowFrame.addWindowListener(this);
        fullscreenFrame.addWindowListener(this);
        fullscreen = fs;
    }
    
    public void start() {
        if (fullscreen) {
            fullscreenFrame.add(canvas);
            fullscreenFrame.setVisible(true);
            screen.setFullScreenWindow(fullscreenFrame);
        } else {
            windowFrame.add(canvas);
            windowFrame.setVisible(true);
        }
        canvas.requestFocus();
        animator.start();
    }
    
    public void setFullscreen(boolean b) {
        if (b != fullscreen) {
            animator.pause();
            if (fullscreen) {
                windowFrame.setVisible(false);
                windowFrame.remove(canvas);
                fullscreenFrame.add(canvas);
                fullscreenFrame.setVisible(true);
                screen.setFullScreenWindow(fullscreenFrame);
            } else {
                screen.setFullScreenWindow(null);
                fullscreenFrame.setVisible(false);
                fullscreenFrame.remove(canvas);
                windowFrame.add(canvas);
                windowFrame.setVisible(true);
            }
            canvas.requestFocus();
            fullscreen = b;
            animator.resume();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            animator.stop();
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            setFullscreen(!fullscreen);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        new Thread() {
           @Override
           public void run() {
              animator.stop();
              System.exit(0);
           }
        }.start();
    }
    
    @Override public void windowOpened(WindowEvent e) { }
    @Override public void windowClosed(WindowEvent e) { }
    @Override public void windowIconified(WindowEvent e) { }
    @Override public void windowDeiconified(WindowEvent e) { } 
    @Override public void windowActivated(WindowEvent e) { } 
    @Override public void windowDeactivated(WindowEvent e) { }
    
}
