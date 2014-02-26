/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.GL3;
import javax.media.opengl.glu.GLU;
import org.cake.game.AWTGameWindow;
import org.cake.game.Color;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.LinearGradient;
import org.cake.game.MultiColorGradient;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.geom.Circle;
import org.cake.game.geom.Line2;
import org.cake.game.geom.Shape;
import org.cake.game.geom.Triangle;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iContour;
import org.cake.game.geom.iShape;
import org.cake.game.iGameAdapter;
import org.cake.game.input.GameKeyEvent;
import org.cake.game.input.GameMouseEvent;
import org.cake.game.input.Key;
import org.cake.game.input.Keyboard;
import org.cake.game.input.Mouse;
import org.cake.game.input.iGameKeyListener;
import org.cake.game.input.iGameMouseListener;
import org.cake.game.io.ResourceManager;
import org.cake.game.particle2.FadeCurve;
import org.cake.game.particle2.FillShapeParticleRenderer;
import org.cake.game.particle2.StaticParticleEmitter;
import org.cake.game.particle2.StaticParticleEmitter.EmitterMode;

/**
 *
 * @author Aaron
 */
public class BasicParticleEmitterTest implements iGameAdapter, iGameKeyListener, iGameMouseListener {

    public static void main(String[] args) {

        AWTGameWindow window = new AWTGameWindow(800, 600, true, false);
        BasicParticleEmitterTest callback = new BasicParticleEmitterTest();
        //window.setViewportMetrics(new FitViewportMetrics(800, 600));

        window.setGameAdapter(callback);
        window.addListener(callback);

        window.start();

    }
    StaticParticleEmitter emitter;
    MultiColorGradient grad;
    LinearGradient lgrad;
    Shape s;
    iBoundingBox bounds;
    
    @Override
    public void init(Game game) {
        GL2 gl = game.getGraphics().getGL();
        ResourceManager.getDefault().loadResourceMap("resources/resources.ini");
        grad = new MultiColorGradient();
        grad.setColor(Color.blue, 0);
        grad.setColor(Color.blue, 0);
        grad.setColor(Color.cyan, 0.2f);
        grad.setColor(Color.green, 0.3f);
        grad.setColor(Color.yellow, 0.5f);
        grad.setColor(Color.orange, 0.6f);
        grad.setColor(Color.red, 0.7f);
        grad.setColor(Color.magenta, 1f);
        grad.setRepeats(true);
        emitter = StaticParticleEmitter.createDefault();
        FillShapeParticleRenderer renderer = FillShapeParticleRenderer.createDefault();
        renderer.setGradient(grad);
        renderer.setParticleFade(FadeCurve.QuadraticInOut);
        renderer.setBlendingMode(Graphics.BlendingMode.BlendAlpha);
        emitter.setParticleRenderer(renderer);
        emitter.setPosition(400,300);
        emitter.setEmitterMode(EmitterMode.Continuous);
        emitter.setActive(true);
        emitter.reset();
        //emitter.reset();
        bounds = emitter.getMaxParticleBounds();
        System.out.println("Bounds: " + bounds);
        
        Font f = new Font("times new roman", Font.PLAIN, 50);
        GlyphVector glyph = f.createGlyphVector(new FontRenderContext(new AffineTransform(), false, true), "Hello World, Here is a number: 13");
        java.awt.Shape outline = glyph.getOutline(100, 400);
        s = javaShapeToGameShape(outline, 2);
        System.out.println("Triangle Count: " + s.getDecomposition().getTris().size());
        gl.glEnable(GL2.GL_POLYGON_SMOOTH);
        //gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL2.GL_FASTEST);
        lgrad = new LinearGradient(0, 0, 800, 0, grad);
        int[] arr = new int[16];
        Arrays.fill(arr, 0);
        gl.glGetIntegerv(GL2.GL_MAX_SAMPLES, arr, 0);
        gl.glGetIntegerv(GL2.GL_SAMPLES, arr, 1);
        System.out.println("Samples: " + Arrays.toString(arr));
    }
    
    public Shape javaShapeToGameShape(java.awt.Shape shape, int curvePoints) {
        PathIterator pathIterator = shape.getPathIterator(new AffineTransform());
        float[] pt = new float[6];
        Shape s = new Shape();
        List<Vector2> bezCurve;
        Vector2 last = null;
        while (!pathIterator.isDone()) {
            int type = pathIterator.currentSegment(pt);
            switch (type) {
                case PathIterator.SEG_CLOSE:
                    s.closeContour();
                    break;
                case PathIterator.SEG_LINETO:
                    last = new Vector2(pt[0], pt[1]);
                    s.addPoint(last);
                    break;
                case PathIterator.SEG_MOVETO:
                    s.newContour();
                    last = new Vector2(pt[0], pt[1]);
                    s.addPoint(last);
                    break;
                case PathIterator.SEG_QUADTO:
                    bezCurve = new ArrayList();
                    bezCurve.add(last);
                    bezCurve.add(new Vector2(pt[0], pt[1]));
                    last = new Vector2(pt[2], pt[3]);
                    bezCurve.add(last);
                    s.addPoints(generateBezierCurve(bezCurve, 3));
                    break;
                case PathIterator.SEG_CUBICTO:
                    bezCurve = new ArrayList();
                    bezCurve.add(last);
                    bezCurve.add(new Vector2(pt[0], pt[1]));
                    bezCurve.add(new Vector2(pt[2], pt[3]));
                    last = new Vector2(pt[4], pt[5]);
                    bezCurve.add(last);
                    s.addPoints(generateBezierCurve(bezCurve, 4));
                    break;
            }
            pathIterator.next();
        }
        return s;
    }
    
    public List<Vector2> generateBezierCurve(List<Vector2> points, int count) {
        List<Vector2> pts = new ArrayList();
        List<Vector2> dummy = new ArrayList();
        for (int i=1; i<=count; i++) {
            dummy.addAll(points);
            Vector2 pt = bezierInterpolate(dummy, (float)i / count);
            pts.add(pt);
            dummy.clear();
        }
        return pts;
    }
    
    public Vector2 bezierInterpolate(List<Vector2> pts, float t) {
        while (pts.size() > 1) {
            for (int i=0; i<pts.size() - 1; i++) {
                pts.set(i, pts.get(i).interpolateTo(pts.get(i + 1), t));
            }
            pts.remove(pts.size() - 1);
        }
        return pts.get(0);
        
    }

    @Override
    public void update(Game game, float delta) {
        Keyboard k = game.getKeyboard();
        Mouse m = game.getMouse();

        emitter.setPosition(m.getX(), m.getY());

        emitter.update(delta);


        //emitter.update(delta);
    }

    @Override
    public void render(Game game, Graphics graphics) {
        GL2 gl = graphics.getGL();
        emitter.draw(gl);
        graphics.setColor(Color.white);
        iBoundingBox bounds = emitter.getMaxParticleBounds();
        //s.getDecomposition().render(gl, lgrad);
        graphics.setColor(Color.white);
        //s.draw(gl);
        renderEdges(gl, s, 100);
        //gl.glTranslatef(0, 100, 0);
        
        Color.white.glBindColor(gl);
        
        //s.getDecomposition().render(gl);
        //gl.glTranslatef(0, -100, 0);
        graphics.drawRectangle(bounds.getMinX(), bounds.getMinY(), bounds.getMaxX() - bounds.getMinX(), bounds.getMaxY() - bounds.getMinY());
        graphics.drawString("Fps: " + (int)game.getFPS(), 5, 25);
        graphics.drawString("Particles: " + emitter.getActiveParticleCount(), 5, 50);
    }
    
    void renderEdges(GL2 gl, Shape shape, float depth) {
        Color.gray.glBindColor(gl);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        GLU glu = GLU.createGLU(gl);
        glu.gluPerspective(30, 1.33, 10, 10000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        
        /*gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-20, 20, -100);
        gl.glVertex3f(20, 20, -100);
        gl.glVertex3f(20, -20, -100);
        gl.glVertex3f(-20, -20, -100);
        gl.glEnd();*/
        
        gl.glScalef(1, -1, 1);
        gl.glTranslatef(-450, -350, -1100);
        gl.glRotatef(30, 0, 1, 0);
        
        for (iContour contour: shape.getContours()) {
            boolean dir = false;
            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (Vector2 pt: contour.getPoints()) {
                gl.glVertex3f(pt.x, pt.y, -10);
                gl.glVertex3f(pt.x, pt.y, 0);
            }
            gl.glEnd();
        }
        //gl.glTranslatef(0, 0, -1000);
        Color.white.glBindColor(gl);
        shape.getDecomposition().fill(gl);
        Color.red.glBindColor(gl);
        //c.draw(gl);
        //t.draw(gl);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
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
        if (e.key == Key.F1) {
            g.setFullscreen(!g.isFullscreen());
        } else if (e.key == Key.Escape) {
            g.exit();
        }
    }

    @Override
    public void keyReleased(Game g, GameKeyEvent e) {

    }

    @Override
    public void buttonPressed(Game g, GameMouseEvent e) {
        
    }

    @Override
    public void buttonReleased(Game g, GameMouseEvent e) {
        
    }

    @Override
    public void buttonClicked(Game g, GameMouseEvent e) {

    }

    @Override
    public void mouseMoved(Game g, GameMouseEvent e) {
    }

    @Override
    public void mouseDragged(Game g, GameMouseEvent e) {
        
    }

    @Override
    public void wheelScrolled(Game g, GameMouseEvent e) {
    }
}
