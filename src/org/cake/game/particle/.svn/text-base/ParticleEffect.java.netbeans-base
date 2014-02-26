/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.media.opengl.GL2;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.geom.Vector2;
import org.cake.game.scene.iEntity;

/**
 *
 * @author Aaron
 */
public class ParticleEffect implements iEntity {

    private float gravityX, gravityY, originX, originY;
    private List<iParticleEmitter_Old> emitters;
    private boolean running, paused, loop, fixed;
    private int renderPriority;

    public ParticleEffect() {
        emitters = new ArrayList<>();
        gravityX = 0;
        gravityY = 20;
        originX = originY = 0;
        loop = fixed = running = paused = false;
        renderPriority = 0;
    }

    public void start() {
        for (iParticleEmitter_Old emitter: emitters) {
            emitter.reset();
        }
        running = true;
        paused = false;
    }

    public void setOrigin(Vector2 pos) {
        originX = pos.x;
        originY = pos.y;
    }

    public void setOrigin(float x, float y) {
        originX = x;
        originY = y;
    }

    public void setFixed(boolean b) {
        fixed = b;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void stop() {
        running = false;
        paused = false;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public int getParticleCount() {
        int tot = 0;
        for (iParticleEmitter_Old e: emitters) {
            tot += e.currentParticles();
        }
        return tot;
    }

    public List<iParticleEmitter_Old> getEmitters() {
        return emitters;
    }

    public void addEmitter(iParticleEmitter_Old emitter) {
        emitters.add(emitter);
        emitter.init(this);
        if (running)
            for (iParticleEmitter_Old e: emitters)
                e.reset();
    }

    public void removeEmitter(iParticleEmitter_Old emitter) {
        emitters.remove(emitter);
        if (running)
            for (iParticleEmitter_Old e: emitters)
                e.reset();
    }

    public float getGravity() {
        return gravityY;
    }

    public float getWind() {
        return gravityX;
    }

    public void setWind(float wind) {
        gravityX = wind;
    }

    public void setGravity(float gravity) {
        gravityY = gravity;
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public Vector2 getOrigin() {
        return new Vector2(originX, originY);
    }

    public float getOffsetX() {
        return fixed ? 0 : originX;
    }

    public float getOffsetY() {
        return fixed ? 0 : originY;
    }

    @Override
    public void render(Game game, Graphics g) {
        GL2 gl = g.getGL();
        if (running) {
            if (fixed) {
                gl.glPushMatrix();
                gl.glTranslatef(originX, originY, 0);
            }
            for (iParticleEmitter_Old emitter: emitters) {
                if (!emitter.isFinished())
                    emitter.render(gl);
            }
            if (fixed) {
                gl.glPopMatrix();
            }
        }
    }

    @Override
    public void update(Game g, float delta) {
        if (running && !paused) {
            boolean run = false;
            for (iParticleEmitter_Old emitter: emitters) {
                emitter.update(delta);
                if (!emitter.isFinished())
                    run = true;
            }
            if (!run) {
                if (loop) {
                    start();
                } else {
                    stop();
                }
            }

        }
    }

    public boolean getLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public int getZIndex() {
        return renderPriority;
    }

    public void setRenderPriority(int renderPriority) {
        this.renderPriority = renderPriority;
    }

    @Override
    public void setZIndex(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
