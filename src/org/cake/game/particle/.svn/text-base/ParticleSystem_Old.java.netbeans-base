/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle;

import java.util.List;
import javax.media.opengl.GL2;
import org.cake.game.Game;
import org.cake.game.Graphics;
import org.cake.game.geom.Transform2;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iRotatable2;
import org.cake.game.geom.iScalable1;
import org.cake.game.geom.iTranslateable2;
import org.cake.game.scene.iEntity;

/**
 *
 * @author Aaron Cake
 */
public class ParticleSystem_Old implements iTranslateable2, iRotatable2, iScalable1, iEntity {

    List<iParticleEmitter_Old> emitters;
    private int renderPriority;
    private Transform2 transform;


    public Vector2 getOrigin() {
        return transform.transformLocal(new Vector2());
    }

    public void setOrigin(Vector2 pos) {
        translate(pos.difference(getOrigin()));
    }

    public void setOrigin(float x, float y) {
        Vector2 o = getOrigin();
        translate(x - o.x, y - o.y);
    }

    @Override
    public void translate(float dx, float dy) {
        transform.translateLocal(dx, dy);
    }

    @Override
    public void translate(Vector2 dpos) {
        transform.translateLocal(dpos);
    }

    @Override
    public void rotate(float angle) {
        transform.rotateLocal(angle, 0, 0);
    }

    @Override
    public void scale(float s) {
        transform.scaleLocal(s, s);
    }

    @Override
    public void render(Game game, Graphics g) {
        GL2 gl = g.getGL();
        for (iParticleEmitter_Old emitter: emitters) {
            emitter.render(gl);
        }
    }

    @Override
    public int getZIndex() {
        return renderPriority;
    }

    @Override
    public void update(Game g, float delta) {
        for (iParticleEmitter_Old emitter: emitters) {
            emitter.update(delta);
        }
    }

    public void reset() {
        for (iParticleEmitter_Old emitter: emitters) {
            emitter.reset();
        }
    }

    @Override
    public void setZIndex(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
