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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.*;
import org.cake.game.Game;
import org.cake.game.iViewportMetrics;
import org.cake.game.geom.Vector2;

/**
 * A class providing mid-level mouse support.
 * @author Aaron Cake
 */
public class Mouse {

    public static final int BUTTON_DOWN = 1;
    public static final int BUTTON_CLICKED = 2;
    public static final int BUTTON_DOUBLE_CLICKED = 4;

    private float ds;
    private Vector2 pos, dpos;
    private Queue<GameMouseEvent> events;
    private List<iGameMouseListener> listeners;
    private int[] buttonStates;
    private Stack<Integer> buttonPresses;

    private Game game;

    /**
     * Create a new Mouse instance for the given game
     * @param g a game
     */
    public Mouse(Game g) {
        pos = new Vector2();
        dpos = new Vector2();
        buttonStates = new int[20];
        events = new ArrayDeque<>();
        listeners = new ArrayList<>();
        game = g;
        buttonPresses = new Stack<>();
    }

    /**
     * Called only once by the Game implementation.
     */
    public void setup() {
        game.addListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Vector2 pos = new Vector2(e.getX(), e.getY());
                iViewportMetrics vpm = game.getViewportMetrics();
                pos = vpm.clipPoint(vpm.transformPoint(pos));
                int button = e.getButton();
                if (button >= 0 && button < buttonStates.length) {
                    events.add(new GameMouseEvent(GameMouseEvent.BUTTON_CLICKED, button, e.getClickCount(), e.getWhen(), getButtonName(button), pos, null, 0));
                    if (e.getClickCount() == 1) {
                        buttonStates[button] |= BUTTON_CLICKED;
                    } else if (e.getClickCount() == 2) {
                        buttonStates[button] |= BUTTON_DOUBLE_CLICKED;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Vector2 pos = new Vector2(e.getX(), e.getY());
                iViewportMetrics vpm = game.getViewportMetrics();
                pos = vpm.clipPoint(vpm.transformPoint(pos));
                int button = e.getButton();
                buttonPresses.add(button);
                if (button >= 0 && button < buttonStates.length) {
                    events.add(new GameMouseEvent(GameMouseEvent.BUTTON_PRESSED, button, 0, e.getWhen(), getButtonName(button), pos, null, 0));
                    buttonStates[button] |= BUTTON_DOWN;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Vector2 pos = new Vector2(e.getX(), e.getY());
                iViewportMetrics vpm = game.getViewportMetrics();
                pos = vpm.clipPoint(vpm.transformPoint(pos));
                int button = e.getButton();
                if (!buttonPresses.isEmpty())
                    buttonPresses.pop();
                if (button >= 0 && button < buttonStates.length) {
                    events.add(new GameMouseEvent(GameMouseEvent.BUTTON_RELEASED, button, 0, e.getWhen(), getButtonName(button), pos, null, 0));
                    buttonStates[button] &= ~BUTTON_DOWN;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        game.addListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Vector2 pos = new Vector2(e.getX(), e.getY());
                iViewportMetrics vpm = game.getViewportMetrics();
                pos = vpm.clipPoint(vpm.transformPoint(pos));
                dpos = Mouse.this.pos.vectorTo(pos);
                int button = buttonPresses.isEmpty() ? 0 : buttonPresses.peek();
                Mouse.this.pos = pos;
                events.add(new GameMouseEvent(GameMouseEvent.MOUSE_DRAGGED, button, 0, e.getWhen(), getButtonName(button), pos, dpos, 0));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Vector2 pos = new Vector2(e.getX(), e.getY());
                iViewportMetrics vpm = game.getViewportMetrics();
                pos = vpm.clipPoint(vpm.transformPoint(pos));
                dpos = Mouse.this.pos.vectorTo(pos);
                Mouse.this.pos = pos;
                events.add(new GameMouseEvent(GameMouseEvent.MOUSE_MOVED, 0, 0, e.getWhen(), null, pos, dpos, 0));
            }
        });
        game.addListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                ds = (float)e.getPreciseWheelRotation();
                events.add(new GameMouseEvent(GameMouseEvent.WHEEL_SCROLLED, 0, 0, e.getWhen(), "MOUSE_WHEEL", pos, null, ds));
            }

        });
    }

    /**
     * Polls all queued events and resets clicks
     */
    public void poll() {
        for (int i=0; i<buttonStates.length; i++) {
            buttonStates[i] &= BUTTON_DOWN;
        }
        while (!events.isEmpty()) {
            GameMouseEvent e = events.poll();
            if (e.isButtonClicked()) {
                if (e.clickCount == 1) {
                    buttonStates[e.button] |= BUTTON_CLICKED;
                } else if (e.clickCount == 2) {
                    buttonStates[e.button] |= BUTTON_DOUBLE_CLICKED;
                }
                for (iGameMouseListener l: listeners) {
                    l.buttonClicked(game, e);
                }
            } else if (e.isButtonPressed()) {
                for (iGameMouseListener l: listeners) {
                    l.buttonPressed(game, e);
                }
            } else if (e.isButtonReleased()) {
                for (iGameMouseListener l: listeners) {
                    l.buttonReleased(game, e);
                }
            } else if (e.isMouseDragged()) {
                for (iGameMouseListener l: listeners) {
                    l.mouseDragged(game, e);
                }
            } else if (e.isMouseMoved()) {
                for (iGameMouseListener l: listeners) {
                    l.mouseMoved(game, e);
                }
            } else if (e.isWheelScrolled()) {
                for (iGameMouseListener l: listeners) {
                    l.wheelScrolled(game, e);
                }
            }
        }
    }

    public void purge() {
        while (!events.isEmpty()) {
            events.poll();
        }
        buttonPresses.clear();
    }

    public String getButtonName(int button) {
        return "MOUSE_BUTTON_" + button;
    }

    public Vector2 getPos() {
        return pos.copy();
    }

    public Vector2 getDPos() {
        return dpos.copy();
    }

    public float getScroll() {
        return ds;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public float getDY() {
        return dpos.y;
    }

    public float getDX() {
        return dpos.x;
    }

    public boolean isButtonDown(int button) {
        return button >= 0 && button < buttonStates.length && (buttonStates[button] & BUTTON_DOWN) != 0;
    }

    public boolean isButtonClicked(int button) {
        return button >= 0 && button < buttonStates.length && (buttonStates[button] & BUTTON_CLICKED) != 0;
    }

    public boolean isButtonDoubleClicked(int button) {
        return button >= 0 && button < buttonStates.length && (buttonStates[button] & BUTTON_DOUBLE_CLICKED) != 0;
    }

    public int getButtonState(int button) {
        return (button >= 0 && button < buttonStates.length ? buttonStates[button] : 0);
    }

    public void addListener(iGameMouseListener l) {
        listeners.add(l);
    }

    public void removeListener(iGameMouseListener l) {
        listeners.remove(l);
    }

}
