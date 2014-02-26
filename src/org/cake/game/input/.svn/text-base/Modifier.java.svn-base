package org.cake.game.input;


import static java.awt.event.KeyEvent.*;
import java.util.Arrays;
import java.util.EnumSet;

/**
 *
 * @author Aaron
 */
public enum Modifier {

    Meta (META_DOWN_MASK),
    Shift (SHIFT_DOWN_MASK),
    Alt (ALT_DOWN_MASK),
    Control (CTRL_DOWN_MASK);

    public final int mask;

    private Modifier(int code) {
        mask = code;
    }

    public static Modifier getByName(String name) {
        for (Modifier m: values()) {
            if (m.name().equalsIgnoreCase(name))
                return m;
        }
        return null;
    }

    public static Modifier getByCode(int mask) {
        for (Modifier m: values()) {
            if (mask == m.mask)
                return m;
        }
        return null;
    }

    public static EnumSet<Modifier> getSetByMask(int mask) {
        EnumSet<Modifier> set = EnumSet.noneOf(Modifier.class);
        for (Modifier m: values()) {
            if ((m.mask & mask) != 0) {
                set.add(m);
            }
        }
        return set;
    }

    public static EnumSet<Modifier> getSetByNames(String... names) {
        EnumSet<Modifier> set = EnumSet.noneOf(Modifier.class);
        for (String s: names) {
            Modifier m = getByName(s);
            if (m != null)
                set.add(m);
        }
        return set;
    }

    public static EnumSet<Modifier> getSet(Modifier... modifiers) {
        return EnumSet.copyOf(Arrays.asList(modifiers));
    }

}
