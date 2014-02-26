
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

package org.cake.game.input;

import org.cake.game.geom.Vector2;

/**
 * A class describing a mouse event such as a button press, the mouse being moved, dragged, or the wheel being scrolled
 * @author Aaron Cake
 */
public class GameMouseEvent {

    public static final int BUTTON_PRESSED = 1;
    public static final int BUTTON_RELEASED = 2;
    public static final int BUTTON_CLICKED = 3;
    public static final int MOUSE_MOVED = 4;
    public static final int MOUSE_DRAGGED = 5;
    public static final int WHEEL_SCROLLED = 6;

    public final int type, button, clickCount;
    public final long time;
    public final String buttonName;
    public final float ds;
    public final Vector2 pos, dpos;

    public GameMouseEvent(int type, int button, int count, long time, String name, Vector2 pos, Vector2 dpos, float ds) {
        this.type = type;
        this.button = button;
        this.buttonName = name;
        this.clickCount = count;
        this.dpos = dpos;
        this.ds = ds;
        this.pos = pos;
        this.time = time;
    }

    public boolean isButtonPressed() {
        return type == BUTTON_PRESSED;
    }

    public boolean isButtonReleased() {
        return type == BUTTON_RELEASED;
    }

    public boolean isButtonClicked() {
        return type == BUTTON_CLICKED;
    }

    public boolean isMouseMoved() {
        return type == MOUSE_MOVED;
    }

    public boolean isMouseDragged() {
        return type == MOUSE_DRAGGED;
    }

    public boolean isWheelScrolled() {
        return type == WHEEL_SCROLLED;
    }

}
