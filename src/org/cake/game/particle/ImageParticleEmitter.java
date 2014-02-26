/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.particle;

import java.util.ArrayDeque;
import java.util.Queue;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.cake.game.*;
import org.cake.game.Graphics.BlendingMode;
import org.cake.game.io.objectxml.ObjectXML;
import org.cake.game.io.objectxml.ObjectXMLNode;
import org.cake.game.io.objectxml.iObjectXMLSerializer;

/**
 *
 * @author Aaron Cake
 */
public class ImageParticleEmitter implements iParticleEmitter_Old {

    static {
        ObjectXML.registerParser(ImageParticleEmitter.class, new iObjectXMLSerializer<ImageParticleEmitter>() {

            @Override
            public ImageParticleEmitter deserialize(ObjectXMLNode node) {
                ImageParticleEmitter emitter = new ImageParticleEmitter();
                emitter.setImage((Image)node.getNamedChild("image").getAsObject());
                emitter.gradient = (iGradient1)node.getNamedChild("gradient").getAsObject();
                emitter.posX = (FloatRange)node.getNamedChild("posXRange").getAsObject();
                emitter.posY = (FloatRange)node.getNamedChild("posYRange").getAsObject();
                emitter.velX = (FloatRange)node.getNamedChild("velXRange").getAsObject();
                emitter.velY = (FloatRange)node.getNamedChild("velYRange").getAsObject();
                emitter.life = (FloatRange)node.getNamedChild("lifeRange").getAsObject();
                emitter.initSize = (FloatRange)node.getNamedChild("startSizeRange").getAsObject();
                emitter.endSize = (FloatRange)node.getNamedChild("endSizeRange").getAsObject();
                emitter.x = node.getAttrNumber("x").floatValue();
                emitter.y = node.getAttrNumber("y").floatValue();
                emitter.delay = node.getAttrNumber("delay").floatValue();
                emitter.myLife = node.getAttrNumber("life").floatValue();
                emitter.canDie = node.getAttrBoolean("canDie");
                emitter.repeats = node.getAttrBoolean("repeats");
                emitter.blendMode = BlendingMode.valueOf(node.getAttr("blendMode"));
                emitter.setParticleRate((FloatRange)node.getNamedChild("particleRate").getAsObject());
                return emitter;
            }

            @Override
            public boolean serialize(ObjectXMLNode node, ImageParticleEmitter emitter) {
                node.addChild(emitter.img, "image");
                node.addChild(emitter.gradient, "gradient");
                node.addChild(emitter.posX, "posXRange");
                node.addChild(emitter.posY, "posYRange");
                node.addChild(emitter.velX, "velXRange");
                node.addChild(emitter.velY, "velYRange");
                node.addChild(emitter.life, "lifeRange");
                node.addChild(emitter.initSize, "startSizeRange");
                node.addChild(emitter.endSize, "endSizeRange");
                node.addChild(emitter.particleRate, "particleRate");
                node.setAttr("x", emitter.y);
                node.setAttr("y", emitter.x);
                node.setAttr("delay", emitter.delay);
                node.setAttr("life", emitter.myLife);
                node.setAttr("canDie", emitter.canDie);
                node.setAttr("repeats", emitter.repeats);
                node.setAttr("blendMode", emitter.blendMode.name());
                return true;
            }
        });
    }

    protected Image img;
    protected ParticleEffect parent;
    protected Queue<Particle> waiting, particles, temp;
    protected int maxParticleCount;
    protected float delay, myLife, lastSpawn, quota;
    protected float x, y, wait, delayWait, lifeLeft;
    protected boolean canDie, active, repeats;
    protected FloatRange posX, posY, velX, velY, life, initSize, endSize, particleRate, waitPeriod;
    protected iGradient1 gradient;
    protected BlendingMode blendMode;

    public ImageParticleEmitter() {
        maxParticleCount = 100;
        particles = new ArrayDeque<>(maxParticleCount);
        waiting = new ArrayDeque<>(maxParticleCount);
        temp = new ArrayDeque<>();
        for (int i=0; i<maxParticleCount; i++) {
            Particle p = new Particle();
            waiting.offer(p);
        }
        blendMode = BlendingMode.BlendAlpha;
        posX = new FloatRange(-5, 5);
        posY = new FloatRange(-5, 5);
        velX = new FloatRange(-20, 20);
        velY = new FloatRange(-20, 20);
        life = new FloatRange(2, 3);
        initSize = new FloatRange(20, 30);
        endSize = new FloatRange(50, 60);
        waitPeriod = new FloatRange(0, 0);
        particleRate = new FloatRange(10, 20);
        gradient = new BiColorGradient(Color.yellow.blend(Color.orange), Color.red.transparent(), false);
        //gradient = new BiColorGradient(Color.white , Color.transparentBlack, false);
        myLife = 2;
        canDie = false;
        active = true;
        lastSpawn = 0;
        quota = 0;
        x = y = 0;
    }

    public void setBlendingMode(BlendingMode mode) {
        blendMode = mode;
    }

    public BlendingMode getBlendingMode() {
        return blendMode;
    }

    public void setImage(String img) {
        this.img = Image.get(img);
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public Image getImage() {
        return img;
    }

    public int getMaxParticles() {
        return maxParticleCount;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public FloatRange getWaitPeriod() {
        return waitPeriod;
    }

    public void setWaitPeriod(FloatRange r) {
        waitPeriod = r;
    }

    @Override
    public int currentParticles() {
        return maxParticleCount - waiting.size();
    }

    public void setStartPosX(FloatRange r) {
        posX = r;
    }

    public FloatRange getStartPosX() {
        return posX;
    }

    public void setStartPosY(FloatRange r) {
        posY = r;
    }

    public FloatRange getStartPosY() {
        return posY;
    }

    public void setStartVelocityX(FloatRange r) {
        velX = r;
    }

    public FloatRange getStartVelocityX() {
        return velX;
    }

    public void setStartVelocityY(FloatRange r) {
        velY = r;
    }

    public FloatRange getStartVelocityY() {
        return velY;
    }

    public void setParticleLife(FloatRange r) {
        int newMax = (int) (particleRate.getHigh() * r.getHigh() * 1.3f);
        while (newMax > maxParticleCount) {
            waiting.offer(new Particle());
            maxParticleCount++;
        }
        life = r;
    }

    public FloatRange getParticleLife() {
        return life;
    }

    public void setStartSize(FloatRange r) {
        initSize = r;
    }

    public FloatRange getStartSize() {
        return initSize;
    }

    public void setEndSize(FloatRange r) {
        endSize = r;
    }

    public FloatRange getEndSize() {
        return endSize;
    }

    public void setParticleRate(FloatRange r) {
        particleRate = r;
        int newMax = (int) (r.getHigh() * life.getHigh() * 1.3f);
        while (newMax > maxParticleCount) {
            waiting.offer(new Particle());
            maxParticleCount++;
        }
    }

    public FloatRange getParticleRate() {
        return particleRate;
    }

    public void setGradient(iGradient1 grad) {
        gradient = grad;
    }

    public iGradient1 getGradient() {
        return gradient;
    }

    @Override
    public void reset() {
        waiting.addAll(particles);
        particles.clear();
        delayWait = delay;
        lifeLeft = myLife;
        wait = 0;
        quota = 0;
    }


    @Override
    public void update(float delta) {
        if (delayWait > 0) {
            delayWait -= delta;
            return;
        }
        temp.clear();
        for (Particle p: particles) {
            if (p.life > 0) {
                p.update(delta);
            } else {
                waiting.add(p);
                temp.add(p);
            }
        }

        for (Particle p: temp)
            particles.remove(p);

        if (canDie) {
            if (lifeLeft > 0) {
                lifeLeft -= delta;
                spawnParticles(delta);
            } else if (repeats && particles.isEmpty()) {
                reset();
            }
        } else {
            spawnParticles(delta);
        }
    }

    protected void spawnParticles(float delta) {
        wait -= delta;
        if (active) {
            lastSpawn += delta;
            if (wait <= 0) {
                quota += particleRate.randomRange() * lastSpawn;
                lastSpawn = 0;
                wait = waitPeriod.randomRange();
            }
            for (int i=0; i<quota && !waiting.isEmpty(); i++) {
                Particle p = waiting.poll();
                p.reset(posX, posY, velX, velY, life, initSize, endSize);
                p.x += x + parent.getOffsetX();
                p.y += y + parent.getOffsetY();
                particles.add(p);
                quota--;
            }
        }
    }

    @Override
    public void render(GL2 gl) {
        //gl.glEnable(GL2.GL_CULL_FACE);
        //gl.glDepthMask(false);
        BlendingMode.glPushBlendMode(gl);
        blendMode.glBind(gl);
        Color.glPushColor(gl);
        //gl.glEnable(GL2.GL_POINT_SPRITE);
        //gl.glTexEnvi(GL2.GL_POINT_SPRITE, GL2.GL_COORD_REPLACE, GL.GL_TRUE);
        img.beginUse(gl);
        float t, po2;
        gl.glBegin(GL2.GL_POINTS);
        for (Particle p: particles) {
            if (p.life > 0) {
                t = 1.0f - (p.life / p.initialLife);
                po2 = p.size/2;
                //gl.glPointSize(p.size);
                gradient.getColor(t).glBindColor(gl);
                //gl.glVertex2f(p.x, p.y);
                img.drawInUse(gl, p.x - po2, p.y - po2, p.size, p.size);
            }
        }
        gl.glEnd();
        img.endUse(gl);
        Color.glPopColor(gl);
        BlendingMode.glPopBlendMode(gl);
        //gl.glDepthMask(true);
    }

    @Override
    public void init(ParticleEffect parent) {
        this.parent = parent;
        reset();
    }

    public float getEmitterPosX() {
        return this.x;
    }

    public float getEmitterPosY() {
        return this.y;
    }

    @Override
    public void setEmitterDelay(float seconds) {
        this.delay = seconds;
    }

    @Override
    public void setEmitterLife(float life) {
        this.myLife = life;
    }

    @Override
    public void setCanDie(boolean canDie) {
        this.canDie = canDie;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean canDie() {
        return canDie;
    }

    public float getEmitterLife() {
        return myLife;
    }

    public float getEmitterDelay() {
        return delay;
    }

    @Override
    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }

    public boolean getRepeats() {
        return repeats;
    }

    @Override
    public boolean isFinished() {
        return !repeats && canDie && lifeLeft <= 0 && particles.isEmpty();
    }

    @Override
    public void setEmitterPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    protected class Particle {

        float x, y, dx, dy, life, initialLife, size, dsize;

        public void reset(FloatRange posX, FloatRange posY, FloatRange velX, FloatRange velY, FloatRange life, FloatRange ss, FloatRange es) {
            x = posX.randomRange();
            y = posY.randomRange();
            dx = velX.randomRange();
            dy = velY.randomRange();
            size = ss.randomRange();
            initialLife = this.life = life.randomRange();
            dsize = (es.randomRange() - size) / initialLife;
        }

        public void reset(FloatRange posX, FloatRange posY, FloatRange velX, FloatRange velY, float life, float ss, float es, float initLife) {
            x = posX.randomRange();
            y = posY.randomRange();
            dx = velX.randomRange();
            dy = velY.randomRange();
            size = ss;
            this.life = life;
            dsize = (es - size) / this.life;
            this.initialLife = initLife;
        }

        public void update(float delta) {
            life -= delta;
            size += dsize * delta;
            x += dx * delta;
            y += dy * delta;
            dx += parent.getWind() * delta;
            dy += parent.getGravity() * delta;
        }

    }

}
