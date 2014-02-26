/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.jogl.test;

import com.jogamp.opengl.util.FPSAnimator;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

/**
 *
 * @author Aaron
 */
public class FullscreenSwingTest implements KeyListener, WindowListener {
    
    public static void main(String[] args) {
        FullscreenSwingTest test = new FullscreenSwingTest(false);
        test.start();
    }
    
    private FPSAnimator animator;
    private GLJPanel panel;
    private JFrame windowFrame, fullscreenFrame;
    private GraphicsDevice screen;
    private boolean fullscreen;
    
    public FullscreenSwingTest(boolean fs) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        panel = new GLJPanel(caps);
        animator = new FPSAnimator(panel, 60);
        panel.addGLEventListener(new CommonTest());
        panel.addKeyListener(this);
        windowFrame = new JFrame("FullscreenAWTTest");
        windowFrame.setSize(800, 600);
        fullscreenFrame = new JFrame("FullscreenAWTTest");
        fullscreenFrame.setUndecorated(true);
        windowFrame.addWindowListener(this);
        fullscreenFrame.addWindowListener(this);
        fullscreen = fs;
    }
    
    public void start() {
        if (fullscreen) {
            fullscreenFrame.add(panel);
            fullscreenFrame.setVisible(true);
            screen.setFullScreenWindow(fullscreenFrame);
        } else {
            windowFrame.add(panel);
            windowFrame.setVisible(true);
        }
        panel.requestFocus();
        animator.start();
    }
    
    public void setFullscreen(boolean b) {
        if (b != fullscreen) {
            animator.pause();
            if (fullscreen) {
                windowFrame.setVisible(false);
                windowFrame.remove(panel);
                fullscreenFrame.add(panel);
                fullscreenFrame.setVisible(true);
                screen.setFullScreenWindow(fullscreenFrame);
            } else {
                screen.setFullScreenWindow(null);
                fullscreenFrame.setVisible(false);
                fullscreenFrame.remove(panel);
                windowFrame.add(panel);
                windowFrame.setVisible(true);
            }
            panel.requestFocus();
            fullscreen = b;
            animator.resume();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            animator.stop();
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            setFullscreen(!fullscreen);
        }
        
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
