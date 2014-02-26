/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game;

import javax.media.opengl.GL2;
import org.cake.game.geom.*;
import org.cake.game.io.objectxml.iObjectXMLSerializable;
import org.cake.game.scene.iEntity;

/**
 *
 * @author Aaron Cake
 */
public class Sprite implements iObjectXMLSerializable, iDrawable, iRotatable2, iTranslateable2, iScalable1, iEntity {

    private Image img;
    public Vector2 pos, size;
    public float angle;
    private int zIndex;
    
    private Sprite() {}

    public Sprite(String source) {
        img = Image.get(source);
        pos = new Vector2();
        size = new Vector2(img.getWidth(), img.getHeight());
        angle = 0;
        zIndex = 0;
    }

    public Sprite(Image img) {
        this.img = img;
        pos = new Vector2();
        size = new Vector2(img.getWidth(), img.getHeight());
        angle = 0;
    }

    @Override
    public void draw(GL2 gl) {
        if (Math.abs(angle) % GeomUtil.PI2 > GeomUtil.EPSILON) {
            float cx = pos.x + size.x/2, cy = pos.y + size.y/2;
            gl.glPushMatrix();
            gl.glTranslatef(cx, cy, 0);

            gl.glRotatef(angle, 0, 0, 1);
            gl.glTranslatef(-cx, -cy, 0);
            img.draw(gl, pos.x, pos.y, size.x, size.y);
            gl.glPopMatrix();
        } else {
            img.draw(gl, pos.x, pos.y, size.x, size.y);
        }
    }

    public Vector2 getCenter() {
        return new Vector2(pos.x + size.x/2, pos.y + size.y/2);
    }

    public float getCenterX() {
        return pos.x + size.x/2;
    }

    public float getCenterY() {
        return pos.y + size.y/2;
    }

    public void setCenter(Vector2 pos) {
        this.pos = pos.add(-size.x/2, -size.y/2);
    }

    @Override
    public void rotate(float angle) {
        this.angle += angle;
    }

    @Override
    public void translate(float dx, float dy) {
        pos.add(dx, dy);
    }

    @Override
    public void translate(Vector2 dpos) {
        pos.add(dpos);
    }

    @Override
    public void scale(float s) {
        float width = size.x * s;
        float height = size.y * s;
        pos.x += (size.x - width)/2;
        pos.y += (size.y - height)/2;
        size.x = width;
        size.y = height;
    }

    @Override
    public void render(Game game, Graphics g) {
        draw(g.getGL());
    }

    @Override
    public void update(Game g, float delta) {
        // do nothing
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    public void setRenderPriority(int i) {
        zIndex = i;
    }

    @Override
    public void setZIndex(int index) {
        zIndex = index;
    }


}
