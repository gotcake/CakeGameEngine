/*
    This file is part of CakeGame engine.

    CakeGame engine is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CakeGame engine is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CakeGame engine.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cake.opengl;

import javax.media.opengl.GL2;
import org.cake.game.Game;
import org.cake.game.Log;
import org.cake.game.geom.Vector2;
import org.cake.game.iGameLifecycleListener;
import org.cake.game.iGameResizeListener;
import org.cake.game.iGameRunnable;
import org.cake.game.iViewportMetrics;

/**
 * A frame buffer object renderer that scales to fit the current virtual game window
 * @author Aaron Cake
 */
public class ScreenSectionFBORenderer extends FBORenderer {
    
    private Vector2 virtualTopLeft, virtualSize, virtualBottomRight, scale;
    
    private ScreenSectionFBORenderer(Game g, iGameRunnable r) {
        super(g.getGraphics().getGL(), r);
        g.addListener(new iGameLifecycleListener() {
            @Override
            public void gameDisplayResized(Game g, int width, int height) {
                System.out.println("Resized: " + new Vector2(width,  height));
                update(g);
            }
            
            @Override public void gameStarted(Game g) { }
            @Override public void gameEnteringFullscreen(Game g) { }
            @Override public void gameExitedFullscreen(Game g) { }
            @Override public void gameFinished() { }
        });
    }
    
    public ScreenSectionFBORenderer(Game game, Vector2 pos, Vector2 size, iGameRunnable runnable) {
        this(game, runnable);
        setRegion(game, pos, size);
    }
    
    public ScreenSectionFBORenderer(Game game, float x, float y, float width, float height, iGameRunnable runnable) {
        this(game, runnable);
        setRegion(game, x, y, width, height);
    }
    
    public void setRegion(Game g, float x, float y, float width, float height) {
        if (virtualTopLeft == null 
                || virtualBottomRight == null || virtualSize == null
                || virtualBottomRight.x - virtualTopLeft.x != width
                || virtualBottomRight.y - virtualTopLeft.y != height
        ) {
            virtualTopLeft = new Vector2(x, y);
            virtualBottomRight = new Vector2(x + width, y + height);
            virtualSize = new Vector2(width, height);
            update(g);
        } else {
            virtualTopLeft = new Vector2(x, y);
            virtualBottomRight = new Vector2(x + width, y + height);
        }
    }
    
    public void setRegion(Game g, Vector2 pos, Vector2 size) {
        setRegion(g, pos.x, pos.y, size.x, size.y);
    }
    
    public void setPosition(float x, float y) {
        Vector2 diff = new Vector2(x, y).subtract(virtualTopLeft);
        virtualTopLeft.add(diff);
        virtualBottomRight.add(diff);
    }
    
    public void setPosition(Vector2 pos) {
        Vector2 diff = pos.difference(virtualTopLeft);
        virtualTopLeft.add(diff);
        virtualBottomRight.add(diff);
    }
    
    protected void update(Game g) {
        iViewportMetrics vpm = g.getViewportMetrics();
        GL2 gl = g.getGraphics().getGL();
        destroy(gl);
        
        // calculate coordinates
        Vector2 realBottomRight = vpm.inverseTransformPoint(virtualBottomRight);
        Vector2 realTopLeft = vpm.inverseTransformPoint(virtualTopLeft);
        
        Log.getDefault().debug("virtualTopLeft = " + virtualTopLeft);
        Log.getDefault().debug("virtualBottomRight = " + virtualBottomRight);
        Log.getDefault().debug("realTopLeft = " + realTopLeft);
        Log.getDefault().debug("realBottomRight = " + realBottomRight);
        
        Log.getDefault().debug("virtualSize = " + new Vector2(vpm.getVirtualWidth(), vpm.getVirtualHeight()));
        
      
        int width = Math.abs((int)(realBottomRight.x - realTopLeft.x));
        int height = Math.abs((int)(realBottomRight.y - realTopLeft.y));
        
        scale = new Vector2(vpm.getDisplayWidth() / vpm.getVirtualWidth(), vpm.getDisplayHeight() / vpm.getVirtualHeight());
        
        Log.getDefault().debug("scale = " + scale);
        
        
        
        super.update(gl, width, height);
    }
    
    public void draw(GL2 gl) {
        draw(gl, virtualTopLeft.x, virtualTopLeft.y, virtualSize.x, virtualSize.y);
    }
    
    @Override
    public void beginRendering(GL2 gl) {
        gl.glPushMatrix();
        gl.glPushMatrix();
        super.beginRendering(gl);
        gl.glScalef(scale.x, scale.y, 1);
        gl.glTranslatef(-virtualTopLeft.x, -virtualTopLeft.y, 0);
        
        
    }
    
    @Override
    public void endRendering(GL2 gl) {
        gl.glPopMatrix();
        super.endRendering(gl);
    }

}
