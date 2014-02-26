/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.bakery;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.media.opengl.GL2;
import org.cake.game.*;
import org.cake.game.geom.Vector2;
import org.cake.game.input.GameMouseEvent;
import org.cake.game.input.iGameMouseListener;
import org.cake.game.particle.ParticleEffect;

/**
 *
 * @author Aaron
 */
public class ParticleDisplay extends SwingGamePanel implements iGameAdapter, iGameMouseListener{

    public ParticleDisplay(Container parent) {
        super(parent, BorderLayout.CENTER, false, false);
        setViewportMetrics(new CenterOriginViewportMetrics());
        setGameAdapter(this);
        addListener(this);
        effect = new ParticleEffect();
        effect.stop();
    }

    private ParticleEffect effect;
    float time = 0;
    int count = 0;

    public ParticleEffect getEffect() {
        return effect;
    }

    public void setBackgroundColor(iColor c) {
        setBackgroundColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public void setBackgroundColor(java.awt.Color c) {
        setBackgroundColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    private void setBackgroundColor(final float r, final float g, final float b, final float a) {
        inject(new iGameRunnable() {
            @Override
            public void run(Game game) {
                GL2 gl = game.getGraphics().getGL();
                gl.glClearColor(r, g, b, a);
            }
        });
    }

    @Override
    public void init(Game game) {
        //game.setClearScreen(false);
        //effect.setOrigin(game.getSize().multiply(0.5f));
    }

    @Override
    public void update(Game game, float delta) {

        effect.update(game, delta);
        time += delta;
        if (time >= 1) {
            time = 0;
            count = effect.getParticleCount();
        }
    }

    @Override
    public void render(Game game, Graphics graphics) {
        float left = -game.getWidth()/2;
        float top = -game.getHeight()/2;
        graphics.drawString("FPS: " + Util.format(game.getFPS(), 0, 1), left + 5, top + 25);
        graphics.drawString("Particles: " + count, left + 5, top + 50);
        effect.render(game, graphics);
        float x = effect.getOriginX(), y = effect.getOriginY();
        graphics.drawLine(x - 10, y, x + 10, y);
        graphics.drawLine(x, y - 10, x, y + 10);
    }

    @Override
    public boolean exitRequested(Game game) {
        return false;
    }

    @Override
    public void exit(Game game) {

    }

    @Override
    public void finished() {

    }

    @Override
    public void buttonPressed(Game g, GameMouseEvent e) {
        if (e.button == 1) {
            effect.setOrigin(e.pos);
        } else if (e.button == 3) {
            effect.setOrigin(0, 0);
        }
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
        if (e.button == 1) {
            effect.setOrigin(e.pos);
        }
    }

    @Override
    public void wheelScrolled(Game g, GameMouseEvent e) {

    }


}
