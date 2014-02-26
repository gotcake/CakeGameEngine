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
import org.cake.game.geom.Vector2;
import org.cake.game.iGameLifecycleListener;
import org.cake.game.iGameRunnable;
import org.cake.game.iViewportMetrics;

/**
 *
 * @author Aaron
 */
public class FullscreenFBORenderer extends FBORenderer {
    
    private float scalex, scaley;
    private Vector2 virtualTopLeft, virtualSize;
    
    
    public FullscreenFBORenderer(Game g, iGameRunnable r) {
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
        update(g);
    }
    
    protected void update(Game g) {
        iViewportMetrics vpm = g.getViewportMetrics();
        GL2 gl = g.getGraphics().getGL();
        
        destroy(gl);
        
        int width = vpm.getDisplayWidth();
        int height = vpm.getDisplayHeight();
        
        scalex = width / vpm.getVirtualWidth();
        scaley = height / vpm.getVirtualHeight();
        
        virtualTopLeft = vpm.getVirtualTopLeft();
        virtualSize = vpm.getVirtualSize();
        
        
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
        gl.glTranslatef(-virtualTopLeft.x, -virtualTopLeft.y, 0);
        gl.glScalef(scalex, scaley, 1);
        
    }
    
    @Override
    public void endRendering(GL2 gl) {
        gl.glPopMatrix();
        super.endRendering(gl);
    }

}
