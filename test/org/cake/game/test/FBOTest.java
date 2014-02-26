/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cake.game.test;

import javax.media.opengl.GL2;
import org.cake.game.AWTGameWindow;
import org.cake.game.CenterOriginViewportMetrics;
import org.cake.game.Color;
import org.cake.game.FitViewportMetrics;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.Image;
import org.cake.game.Log;
import org.cake.game.StretchViewportMetrics;
import org.cake.game.iGameAdapter;
import org.cake.game.iGameRunnable;
import org.cake.game.input.GameKeyEvent;
import org.cake.game.input.GameMouseEvent;
import org.cake.game.input.Key;
import org.cake.game.input.iGameKeyListener;
import org.cake.game.input.iGameMouseListener;
import org.cake.game.io.ResourceManager;
import org.cake.opengl.FBORenderer;
import org.cake.opengl.FullscreenFBORenderer;
import org.cake.opengl.ScreenSectionFBORenderer;

/**
 *
 * @author Aaron
 */


public class FBOTest implements iGameAdapter, iGameKeyListener, iGameMouseListener {

    public static void main(String[] args) {

        AWTGameWindow window = new AWTGameWindow(800, 600, true, false);
        FBOTest callback = new FBOTest();
        
        window.setViewportMetrics(new StretchViewportMetrics(800, 600));

        window.setGameAdapter(callback);
        window.addListener(callback);

        window.start();

    }
    private ScreenSectionFBORenderer ssr;

    @Override
    public void init(final Game game) {
        ResourceManager.getDefault().loadResourceMap("resources/resources.ini");
        Image.get("images.smiley");
        final int width = 800, height = 600;
        ssr = new ScreenSectionFBORenderer(game, 100, 100, 200, 200, null);
        
    }

    @Override
    public void update(Game game, float delta) {
        
    }

    @Override
    public void render(Game game, Graphics graphics) {
        GL2 gl = graphics.getGL();
        
        graphics.setColor(Color.red);
        
        graphics.drawRectangle(0, 0, 800, 600);
        
        graphics.setColor(Color.white);
        
        //renderer.draw(gl, 0, 0, 800, 600);
        
        graphics.drawString(game.getFPS(), 0, 23);
        
        ssr.setPosition(game.getMouse().getPos());
        
        ssr.beginRendering(gl);
        
        graphics.clearScreen();
        
        graphics.setColor(Color.blue);
        
        graphics.fillRectangle(0, 0, 500, 500);
        
        graphics.setColor(Color.white);
        
        graphics.drawCircle(200, 200, 100);
        
        ssr.endRendering(gl);
        
        ssr.draw(gl);
        
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
