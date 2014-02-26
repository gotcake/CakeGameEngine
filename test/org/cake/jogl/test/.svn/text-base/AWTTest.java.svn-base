package org.cake.jogl.test;


import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class AWTTest extends Frame {
    
    public static void main(String[] args) {
        AWTTest test = new AWTTest();
        test.setSize(800, 600);
        test.setVisible(true);
        test.start();
    }
    
    private FPSAnimator animator;
    
    public AWTTest() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        animator = new FPSAnimator(canvas, 60);
        addWindowListener(new WindowAdapter() {     // For the close button
         @Override
         public void windowClosing(WindowEvent e) {
            // Use a dedicate thread to run the stop() to ensure that the
            // animator stops before program exits.
            new Thread() {
               @Override
               public void run() {
                  //animator.stop();
                  //System.exit(0);
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
