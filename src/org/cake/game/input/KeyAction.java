/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.input;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.cake.game.Game;
import org.cake.game.reflect.Reflect;

/**
 *
 * @author Aaron Cake
 */
public class KeyAction implements iAction {

    public enum ModifierPolicy {
        Exact,
        Inclusive,
        Exclusive
    }

    public KeyAction(Key key) {
        this(key, ModifierPolicy.Inclusive);
    }

    public KeyAction(Key key, Modifier... modifiers) {
        this(key, ModifierPolicy.Exact, modifiers);
    }

    public KeyAction(Key key, ModifierPolicy modifierPolicy, Modifier... modifiers) {
        this.key = key;
        this.policy = modifierPolicy;
        this.modifiers = 0;
        for (Modifier m: modifiers)
            this.modifiers |= m.mask;
        listeners = new ArrayList<>(1);
    }


    private Key key;
    private int modifiers;
    private ModifierPolicy policy;
    private List<iActionListener> listeners;

    public Key getKey() {
        return key;
    }

    public int getModifierMask() {
        return modifiers;
    }

    public EnumSet<Modifier> getModifiers() {
        return Modifier.getSetByMask(modifiers);
    }

    public ModifierPolicy getModifierPolicy() {
        return policy;
    }

    @Override
    public void addListener(iActionListener listener) {
        listeners.add(listener);
    }

    @Override
    public iActionListener addListener(Class clazz, String staticMethodName) {
        iActionListener il = Reflect.createProxy(iActionListener.class, "actionPreformed", clazz, staticMethodName);
        listeners.add(il);
        return il;
    }

    @Override
    public iActionListener addListener(Object obj, String methodName) {
        iActionListener il = Reflect.createProxy(iActionListener.class, "actionPreformed", obj, methodName);
        listeners.add(il);
        return il;
    }

    @Override
    public void removeListener(iActionListener listener) {
        listeners.add(listener);
    }

    public void actionPreformed(Game g) {
        for (iActionListener al: listeners)
            al.actionPreformed(g, this);
    }



}

