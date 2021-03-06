/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cake.game.test;

import javax.media.opengl.GL2;
import org.cake.game.*;
import org.cake.game.collision.CollisionUtil;
import org.cake.game.collision.iBoundingBox;
import org.cake.game.collision.iBoundingCircle;
import org.cake.game.geom.*;
import org.cake.game.input.GameKeyEvent;
import org.cake.game.input.GameMouseEvent;
import org.cake.game.input.iGameKeyListener;
import org.cake.game.input.iGameMouseListener;

/**
 *
 * @author Aaron
 */
public class GeomTest implements iGameAdapter, iGameKeyListener, iGameMouseListener {

    public static void main(String[] args) {
        Game g = new AWTGameWindow(800, 600, true, false);
        GeomTest adapter = new GeomTest();
        g.setGameAdapter(adapter);
        g.addListener(adapter);
        g.start();
    }

    Triangle triangle;
    Rectangle rectangle;
    Polygon polygon, poly2;
    Circle circle;

    iConvex[] shapes;

    @Override
    public void init(Game game) {


        triangle = new Triangle(200, 200, 100);
        rectangle = new Rectangle(400, 400, 200, 100);
        polygon = new Polygon(200, 300, 10, 100);
        poly2 = new Polygon(200, 300, 10, 100);
        circle = new Circle(600, 300, 100);

        shapes = new iConvex[] {poly2, polygon};

    }

    @Override
    public void update(Game game, float delta) {

    }

    @Override
    public void render(Game game, Graphics graphics) {

        GL2 gl = graphics.getGL();

        //gl.glLineWidth(2);

        if (poly2.intersects(polygon)) {
            graphics.setColor(Color.red);
        } else {
            graphics.setColor(Color.white);
        }

        for (iConvex shape: shapes) {
            graphics.fill(shape);
        }

        graphics.setColor(Color.blue);

        for (iConvex shape: shapes) {
            iBoundingCircle bc = shape.getBoundingCircle();
            graphics.drawCircle(bc.getCenter(), bc.getRadius());
        }

        graphics.setColor(Color.green);

        for (iConvex shape: shapes) {
            iBoundingBox box = shape.getAABB();
            graphics.drawRectangle(box.getMinX(), box.getMinY(), box.getMaxX() - box.getMinX(), box.getMaxY() - box.getMinY());
        }



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
        /*long time = System.nanoTime();
        CollisionUtil.hasSeparatingAxis(polygon.getPoints(), poly2.getPoints());
        time = System.nanoTime() - time;
        System.out.println("Separating axis: " + time);
        time = System.nanoTime();
        CollisionUtil.intersects(polygon, poly2);
        time = System.nanoTime() - time;
        System.out.println("Intersection:    " + time);
        time = System.nanoTime();
        polygon.intersects(poly2);
        time = System.nanoTime() - time;
        System.out.println("Default:         " + time);*/
    }

    @Override
    public void mouseMoved(Game g, GameMouseEvent e) {
        poly2.setCenter(e.pos);
    }

    @Override
    public void mouseDragged(Game g, GameMouseEvent e) {
        poly2.setCenter(e.pos);
    }

    @Override
    public void wheelScrolled(Game g, GameMouseEvent e) {
        poly2.rotate(GeomUtil.PI  * 0.05f);
    }

}
