package org.cake.jogl.test;


import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class CommonTest implements GLEventListener {
    
    GLU glu;
    
    @Override
    public void init(GLAutoDrawable drawable) {
        System.out.println("Initing...");
        glu = GLU.createGLU();
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_SMOOTH);
        /*GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);*/
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.out.println("Disposing...");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        gl.glLoadIdentity(); 
        gl.glColor3f(1, 0, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(0, 300);
        gl.glVertex2f(300, 300);
        gl.glVertex2f(300, 0);
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        System.out.println("Reshaping: (" + width + ", " + height + ")");
      GL2 gl = drawable.getGL().getGL2();
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL2.GL_PROJECTION);  
      gl.glLoadIdentity();
      glu.gluOrtho2D(0, width, height, 0);
      gl.glMatrixMode(GL2.GL_MODELVIEW);
      gl.glLoadIdentity();
   }
}
