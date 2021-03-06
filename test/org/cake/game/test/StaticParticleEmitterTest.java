/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.test;

import javax.media.opengl.GL2;
import org.cake.game.AWTGameWindow;
import org.cake.game.Color;
import org.cake.game.FloatRange;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.MultiColorGradient;
import org.cake.game.iGameAdapter;
import org.cake.game.input.GameKeyEvent;
import org.cake.game.input.GameMouseEvent;
import org.cake.game.input.Key;
import org.cake.game.input.Keyboard;
import org.cake.game.input.Mouse;
import org.cake.game.input.iGameKeyListener;
import org.cake.game.input.iGameMouseListener;
import org.cake.game.io.ResourceManager;
import org.cake.game.particle.ImageParticleEmitter;
import org.cake.game.particle.ParticleEffect;

/**
 *
 * @author Aaron
 */
public class StaticParticleEmitterTest implements iGameAdapter, iGameKeyListener, iGameMouseListener {

    public static void main(String[] args) {

        AWTGameWindow window = new AWTGameWindow(800, 600, true, false);
        GameTest callback = new GameTest();

        window.setGameAdapter(callback);
        window.addListener(callback);

        window.start();

    }
    ImageParticleEmitter emitter;
    ParticleEffect effect;
    MultiColorGradient grad;
    int i=0;

    @Override
    public void init(Game game) {
        ResourceManager.getDefault().loadResourceMap("resources/resources.ini");
        grad = new MultiColorGradient();
        grad.setColor(Color.blue, 0);
        grad.setColor(Color.cyan, 0.2f);
        grad.setColor(Color.green, 0.3f);
        grad.setColor(Color.yellow, 0.5f);
        grad.setColor(Color.orange, 0.6f);
        grad.setColor(Color.red, 0.7f);
        grad.setColor(Color.magenta, 0.8f);
        grad.setColor(Color.transparentBlack, 1);
        grad.setRepeats(true);
        //System.out.println(grad);
        emitter = new ImageParticleEmitter();
        emitter.setImage("resources/images/particle1.png");
        emitter.setGradient(grad);
        emitter.setParticleRate(new FloatRange(2000, 3000));
        emitter.setParticleLife(new FloatRange(4, 5));
        //System.out.println(ObjectXML.serialize(emitter));
        emitter.setBlendingMode(Graphics.BlendingMode.BlendAlpha);
        effect = new ParticleEffect();
        effect.addEmitter(emitter);
        effect.start();
        emitter.setActive(false);
    }

    @Override
    public void update(Game game, float delta) {
        Keyboard k = game.getKeyboard();
        Mouse m = game.getMouse();

        emitter.setPosition(m.getX(), m.getY());

        effect.update(game, delta);


        //emitter.update(delta);
    }

    @Override
    public void render(Game game, Graphics graphics) {
        graphics.drawString("Fps: " + (int)game.getFPS(), 5, 25);
        graphics.drawString("Particles: " + emitter.currentParticles(), 5, 50);
        
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
