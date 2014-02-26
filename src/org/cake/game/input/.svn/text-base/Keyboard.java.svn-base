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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import org.cake.game.Game;
import org.cake.collections.MultiMap;
import org.cake.game.input.KeyAction.ModifierPolicy;

/**
 * A class providing mid-level keyboard support
 * @author Aaron Cake
 */
public final class Keyboard {

    public static final byte STATE_NONE = 0x00, STATE_DOWN = 0x01, STATE_PRESSED = 0x02, STATE_TOGGLED = 0x04;
    public static final byte EVENT_PRESSED = 0x01, EVENT_RELEASED = 0x02, EVENT_TOGGLED = 0x04, EVENT_UNTOGGLED = 0x08, EVENT_TYPED = 0x10;
    private static final int[] lockingKeys = new int[]{KeyEvent.VK_NUM_LOCK, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_SCROLL_LOCK, KeyEvent.VK_KANA_LOCK};
    //private static String[] keyNames;

    private byte[] keyStates;
    private StringBuffer text;
    private Game game;
    private List<iGameKeyListener> listeners;
    private Queue<org.cake.game.input.GameKeyEvent> events;
    private MultiMap<Integer, KeyAction> actions;

    /**
     * Creates a new Keyboard instance for a given Game
     * @param g the Game
     */
    public Keyboard(Game g) {
        game = g;
        listeners = new ArrayList<>();
        events = new ArrayDeque<>();
        keyStates = new byte[KeyEvent.KEY_LAST];
        text = new StringBuffer();
        actions = new MultiMap<>();
    }

    /**
     * Called only once by the Game implementation.
     */
    public void setup() {
        game.addListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() > keyStates.length)
                    return;
                if (filter(e.getKeyChar())) {
                    text.append(e.getKeyChar());
                    events.add(new GameKeyEvent(GameKeyEvent.KEY_TYPED, e.getKeyCode(), e.getModifiersEx(), e.getWhen(), e.getKeyChar(), true, (keyStates[e.getKeyCode()] & STATE_TOGGLED) != 0));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code > keyStates.length)
                    return;
                if ((keyStates[code] & STATE_DOWN) == 0) {
                    keyStates[code] |= STATE_DOWN | STATE_PRESSED;
                    if ((keyStates[code] & STATE_TOGGLED) == 0) {
                        keyStates[code] |= STATE_TOGGLED;
                    } else {
                        keyStates[code] &= ~STATE_TOGGLED;
                    }
                    events.add(new GameKeyEvent(GameKeyEvent.KEY_PRESSED, code, e.getModifiersEx(), e.getWhen(), e.getKeyChar(), true, (keyStates[code] & STATE_TOGGLED) != 0));
                    //events.add(new GameKeyEvent(GameKeyEvent.KEY_TOGGLED, code, e.getModifiersEx(), e.getWhen(), e.getKeyChar(), (keyStates[code] & STATE_TOGGLED) != 0));
                } else {
                    keyStates[e.getKeyCode()] &= ~STATE_PRESSED;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code > keyStates.length)
                    return;
                keyStates[code] &= ~STATE_PRESSED;
                keyStates[code] &= ~STATE_DOWN;
                events.add(new GameKeyEvent(GameKeyEvent.KEY_RELEASED, code, e.getModifiersEx(), e.getWhen(), e.getKeyChar(), false, (keyStates[code] & STATE_TOGGLED) != 0));
            }
        });

        game.addListener(new com.jogamp.newt.event.KeyListener() {

            @Override
            public void keyPressed(com.jogamp.newt.event.KeyEvent e) {
                int code = e.getKeyCode();
                if (code > keyStates.length)
                    return;
                if ((keyStates[code] & STATE_DOWN) == 0) {
                    keyStates[code] |= STATE_DOWN | STATE_PRESSED;
                    if ((keyStates[code] & STATE_TOGGLED) == 0) {
                        keyStates[code] |= STATE_TOGGLED;
                    } else {
                        keyStates[code] &= ~STATE_TOGGLED;
                    }
                    events.add(new GameKeyEvent(GameKeyEvent.KEY_PRESSED, code, e.getModifiers(), e.getWhen(), e.getKeyChar(), true, (keyStates[code] & STATE_TOGGLED) != 0));
                    //events.add(new GameKeyEvent(GameKeyEvent.KEY_TOGGLED, code, e.getModifiers(), e.getWhen(), e.getKeyChar(), (keyStates[code] & STATE_TOGGLED) != 0));
                } else {
                    keyStates[e.getKeyCode()] &= ~STATE_PRESSED;
                }
            }

            @Override
            public void keyReleased(com.jogamp.newt.event.KeyEvent e) {
                int code = e.getKeyCode();
                if (code > keyStates.length)
                    return;
                keyStates[code] &= ~STATE_PRESSED;
                keyStates[code] &= ~STATE_DOWN;
                events.add(new GameKeyEvent(GameKeyEvent.KEY_RELEASED, code, e.getModifiers(), e.getWhen(), e.getKeyChar(), false, (keyStates[code] & STATE_TOGGLED) != 0));
            }

            @Override
            public void keyTyped(com.jogamp.newt.event.KeyEvent e) {
                if (e.getKeyCode() > keyStates.length)
                    return;
                if (filter(e.getKeyChar())) {
                    text.append(e.getKeyChar());
                    events.add(new GameKeyEvent(GameKeyEvent.KEY_TYPED, e.getKeyCode(), e.getModifiers(), e.getWhen(), e.getKeyChar(), true, (keyStates[e.getKeyCode()] & STATE_TOGGLED) != 0));
                }
            }

        });

        init();
    }

    /**
     * Pushes all events since the last call through and calls all listeners for each event.
     */
    public void poll() {
        while (!events.isEmpty()) {
            GameKeyEvent e = events.poll();
            for (iGameKeyListener l: listeners) {
                if (e.isKeyTyped()) {
                    l.keyTyped(game, e);
                } else if (e.isKeyPressed()) {
                    l.keyPressed(game, e);
                    for (KeyAction action: actions.get(e.key.vkCode)) {
                        int amask = action.getModifierMask(), emask = e.getModifierMask();
                        ModifierPolicy policy = action.getModifierPolicy();
                        if (policy == ModifierPolicy.Exact) {
                            if (amask == emask)
                                action.actionPreformed(game);
                        } else if (policy == ModifierPolicy.Inclusive) {
                            if ((amask & emask) == amask)
                                action.actionPreformed(game);
                        } else if (policy == ModifierPolicy.Exclusive) {
                            if ((amask & emask) == 0)
                                action.actionPreformed(game);
                        }
                    }

                } else if (e.isKeyReleased()) {
                    l.keyReleased(game, e);
                }/* else if (e.isKeyToggled()) {
                    l.keyToggled(game, e);
                }*/
            }
        }
    }

    public void purge() {
        while (!events.isEmpty()) {
            events.poll();
        }
    }


    public void init() {
        for (int i = 0; i < keyStates.length; i++) {
            keyStates[i] = STATE_NONE;
        }
        for (int i : lockingKeys) {
            try {
                if (Toolkit.getDefaultToolkit().getLockingKeyState(i)) {
                    keyStates[i] &= STATE_TOGGLED;
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean isKeyDown(Key key) {
        return (keyStates[key.vkCode] & STATE_DOWN) != 0;
    }

    public boolean isKeyPressed(Key key) {
        if ((keyStates[key.vkCode] & STATE_PRESSED) != 0) {
            keyStates[key.vkCode] &= ~STATE_PRESSED;
            return true;
        }
        return false;
    }

    public int getModifiersMask() {
        int mask = 0;
        if ((keyStates[KeyEvent.VK_SHIFT] & STATE_DOWN) != 0)
            mask |= KeyEvent.SHIFT_DOWN_MASK;
        if ((keyStates[KeyEvent.VK_META] & STATE_DOWN) != 0)
            mask |= KeyEvent.META_DOWN_MASK;
        if ((keyStates[KeyEvent.VK_ALT] & STATE_DOWN) != 0)
            mask |= KeyEvent.ALT_DOWN_MASK;
        if ((keyStates[KeyEvent.VK_CONTROL] & STATE_DOWN) != 0)
            mask |= KeyEvent.CTRL_DOWN_MASK;
        return mask;

    }

    public EnumSet<Modifier> getModifiers() {
        int mask = 0;
        if ((keyStates[KeyEvent.VK_SHIFT] & STATE_DOWN) != 0)
            mask |= KeyEvent.SHIFT_DOWN_MASK;
        if ((keyStates[KeyEvent.VK_META] & STATE_DOWN) != 0)
            mask |= KeyEvent.META_DOWN_MASK;
        if ((keyStates[KeyEvent.VK_ALT] & STATE_DOWN) != 0)
            mask |= KeyEvent.ALT_DOWN_MASK;
        if ((keyStates[KeyEvent.VK_CONTROL] & STATE_DOWN) != 0)
            mask |= KeyEvent.CTRL_DOWN_MASK;
        return Modifier.getSetByMask(mask);
    }

    public boolean isKeyToggled(Key key) {
        return (keyStates[key.vkCode] & STATE_TOGGLED) != 0;
    }

    private static boolean filter(char c) {
        return true;
    }

    /*public static String getKeyName(int key) {
        if (keyNames == null)
            keyNames = new String[KeyEvent.KEY_LAST];
            for (Field f: KeyEvent.class.getFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.getName().startsWith("VK_")) {
                try {
                    int val = f.getInt(null);
                    if (val >= 0 && val <= KeyEvent.KEY_LAST) {
                        keyNames[val] = f.getName().substring(3);
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {

                }
            }
        }
        if (key > keyNames.length || keyNames[key] == null) {
            return "UNKNOWN_" + key;
        } else {
            return keyNames[key];
        }
    }*/

    public String getText() {
        String str = text.toString();
        text = new StringBuffer();
        return str;
    }

    public void addListener(iGameKeyListener l) {
        listeners.add(l);
    }

    public void removeListener(iGameKeyListener l) {
        listeners.remove(l);
    }

    public void registerKeyAction(KeyAction action) {
        actions.put(action.getKey().vkCode, action);
    }

    public void unregisterKeyAction(KeyAction action) {
        actions.removeValue(action.getKey().vkCode, action);
    }

    public void unregisterKeyActionKey(Key key) {
        actions.removeKey(key.vkCode);
    }

    public void unregisterAllKeyActions() {
        actions.clear();
    }
}
