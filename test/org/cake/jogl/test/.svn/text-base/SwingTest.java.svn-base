package org.cake.jogl.test;


import com.jogamp.opengl.util.Animator;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class SwingTest extends JFrame {
    
    public static void main(String[] args) {
        AWTTest test = new AWTTest();
        test.setSize(800, 600);
        test.setVisible(true);
        test.start();
    }
    
    private Animator animator;
    
    public SwingTest() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel canvas = new GLJPanel(caps);
        canvas.addKeyListener(new KeyListener() {
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
                }
            }
            
        });
        animator = new Animator(canvas);
        addWindowListener(new WindowAdapter() {     // For the close button
         @Override
         public void windowClosing(WindowEvent e) {
            // Use a dedicate thread to run the stop() to ensure that the
            // animator stops before program exits.
            new Thread() {
               @Override
               public void run() {
                  animator.stop();
                  System.exit(0);
               }
            }.start();
         }
      });
      add(canvas);
      canvas.addGLEventListener(new CommonTest());
      setTitle("AWT JOGLv2 Test");
    }
    
    public void start() {
        setVisible(true);
        animator.start();
    }
}
