/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.input;

import static java.awt.event.KeyEvent.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aaron Cake
 */
public enum Key {

    // Letters
    A (VK_A),
    B (VK_B),
    C (VK_C),
    D (VK_D),
    E (VK_E),
    F (VK_F),
    G (VK_G),
    H (VK_H),
    I (VK_I),
    J (VK_J),
    K (VK_K),
    L (VK_L),
    M (VK_M),
    N (VK_N),
    O (VK_O),
    P (VK_P),
    Q (VK_Q),
    R (VK_R),
    S (VK_S),
    T (VK_T),
    U (VK_U),
    V (VK_V),
    W (VK_W),
    X (VK_X),
    Y (VK_Y),
    Z (VK_Z),

    // number bar
    Num1 (VK_1),
    Num2 (VK_2),
    Num3 (VK_3),
    Num4 (VK_4),
    Num5 (VK_5),
    Num6 (VK_6),
    Num7 (VK_7),
    Num8 (VK_8),
    Num9 (VK_9),
    Num0 (VK_0),

    // function keys
    F1 (VK_F1),
    F2 (VK_F2),
    F3 (VK_F3),
    F4 (VK_F4),
    F5 (VK_F5),
    F6 (VK_F6),
    F7 (VK_F7),
    F8 (VK_F8),
    F9 (VK_F9),
    F10 (VK_F10),
    F11 (VK_F11),
    F12 (VK_F12),
    F13 (VK_F13),
    F14 (VK_F14),
    F15 (VK_F15),
    F16 (VK_F16),
    F17 (VK_F17),
    F18 (VK_F18),

    // number pad
    NumPad1 (VK_NUMPAD1),
    NumPad2 (VK_NUMPAD2),
    NumPad3 (VK_NUMPAD3),
    NumPad4 (VK_NUMPAD4),
    NumPad5 (VK_NUMPAD5),
    NumPad6 (VK_NUMPAD6),
    NumPad7 (VK_NUMPAD7),
    NumPad8 (VK_NUMPAD8),
    NumPad9 (VK_NUMPAD9),
    NumPad0 (VK_NUMPAD0),

    // everything else
    Escape (VK_ESCAPE),
    BackQuote (VK_BACK_QUOTE),
    Tab (VK_TAB),
    CapsLock (VK_CAPS_LOCK),
    Shift (VK_SHIFT),
    Control (VK_CONTROL),
    Alt (VK_ALT),
    Meta (VK_META),
    Minus (VK_MINUS),
    Equals (VK_EQUALS),
    LeftBracket (VK_OPEN_BRACKET),
    RightBracket (VK_CLOSE_BRACKET),
    BackSlash (VK_BACK_SLASH),
    BackSpace (VK_BACK_SPACE),
    Semicolon (VK_SEMICOLON),
    Quote (VK_QUOTE),
    Enter (VK_ENTER),
    Comma (VK_COMMA),
    Period (VK_PERIOD),
    Slash (VK_SLASH),
    PrintScreen (VK_PRINTSCREEN),
    ScrollLock (VK_SCROLL_LOCK),
    Pause (VK_PAUSE),
    Insert (VK_INSERT),
    Delete (VK_DELETE),
    Home (VK_HOME),
    End (VK_END),
    PageUp (VK_PAGE_UP),
    PageDown (VK_PAGE_DOWN),
    Up (VK_UP),
    Down (VK_DOWN),
    Left (VK_LEFT),
    Right (VK_RIGHT),
    NumLock (VK_NUM_LOCK),
    Divide (VK_DIVIDE),
    Multiply (VK_MULTIPLY),
    Subtract (VK_SUBTRACT),
    Add (VK_ADD),
    Space (VK_SPACE),
    Clear (VK_CLEAR)

    ;

    public final int vkCode;

    private static final Map<String, Key> nameMap = new HashMap<>();
    private static final Map<Integer, Key> codeMap = new HashMap<>();

    static {
        for (Key k: values()) {
            nameMap.put(k.name().toUpperCase(), k);
            codeMap.put(k.vkCode, k);
        }
    }

    private Key(int code) {
        vkCode = code;
    }

    @Override
    public String toString() {
        return "Key[" + name() + "=" + vkCode + "]";
    }

    public static Key getByName(String name) {
        return nameMap.get(name.toUpperCase());
    }

    public static Key getByCode(int code) {
        return codeMap.get(code);
    }

}
