
import com.jogamp.opengl.test.junit.graph.TestRegionRendererNEWT01;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL2;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.cake.game.geom.Shape;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iContour;
import org.cake.game.geom.iShape;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class Test extends Canvas {
    
    public static void main(String[] args) throws IOException {
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);
        JSpinner spinner = new JSpinner();
        spinner.getModel().setValue(2);
        panel.add(spinner, BorderLayout.NORTH);
        panel.add(new Test(spinner), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        
    }
    
    public Test(final JSpinner spinner) {
        count = Math.max((int)spinner.getValue(), 1);
        System.out.println("count: " + count);
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                count = Math.max((int)spinner.getValue(), 1);
                System.out.println("count: " + count);
                Test.this.repaint();
            }
        });
    }
    
    private int count;
    
    Graphics2D g2;
    
    @Override
    public void paint(Graphics g) {
        g2 = (Graphics2D)g;
        Font f = new Font("arial", Font.PLAIN, 30);
        GlyphVector glyph = f.createGlyphVector(g.getFontMetrics().getFontRenderContext(), "Hello World, Here is a number: 13");
        java.awt.Shape outline = glyph.getOutline(100, 400);
        Shape s = javaShapeToGameShape(outline, count);
        drawGameShape(s, g2);
        System.out.println("Triangle Count: " + s.getDecomposition().getTris().size());
        
        //drawGameShape(s2, g2);
        
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
                    s.addPoints(generateBezierCurve(bezCurve, curvePoints));
                    break;
                case PathIterator.SEG_CUBICTO:
                    bezCurve = new ArrayList();
                    bezCurve.add(last);
                    bezCurve.add(new Vector2(pt[0], pt[1]));
                    bezCurve.add(new Vector2(pt[2], pt[3]));
                    last = new Vector2(pt[4], pt[5]);
                    bezCurve.add(last);
                    s.addPoints(generateBezierCurve(bezCurve, curvePoints));
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
    
     public void drawGameShape(iShape shape, Graphics2D g) {
        for (iContour c: shape.getContours()) {
            List<Vector2> points = c.getPoints();
            if (points.size() >= 2) {
                Vector2 last = points.get(0);
                Vector2 current;
                for (int i=1; i<points.size(); i++) {
                    current = points.get(i);
                    g.draw(new Line2D.Float(last.x, last.y, current.x, current.y));
                    last = current;
                }
                if (c.isClosed()) {
                    current = points.get(0);
                    g.draw(new Line2D.Float(last.x, last.y, current.x, current.y));
                }
            }
        }
    }
    
    
    
    
}
