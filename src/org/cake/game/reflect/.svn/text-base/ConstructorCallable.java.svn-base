/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.cake.game.exception.GameException;

/**
 *
 * @author Aaron
 */
public class ConstructorCallable<T> implements Callable {
    
    Constructor<T> c;
    
    public ConstructorCallable(Constructor<T> c) {
        this.c = c;
    }
    
    public ConstructorCallable(Class<T> clazz, Class[] paramTypes) {
        c = Reflect.getConstructor(clazz, paramTypes);
    }

    @Override
    public T call(Object[] args) {
        try {
            return c.newInstance(args);
        } catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException ex) {
            throw new GameException(ex);
        }
    }
    
    public Constructor getConstructor() {
        return c;
    }

    @Override
    public boolean isValidArgs(Object[] args) {
        return Reflect.areValidArgs(c.getParameterTypes(), args);
    }

    @Override
    public T callv(Object... args) {
        return call(args);
    }
    
}
