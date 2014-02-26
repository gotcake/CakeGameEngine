/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.reflect;

import org.cake.game.exception.GameException;

/**
 *
 * @author Aaron
 */
public class OverloadCallable<T> implements Callable {
    
    private Callable<? extends T>[] callables;
    
    public OverloadCallable(Callable<? extends T>[] callables) {
        this.callables = callables;
    }

    @Override
    public T call(Object[] args) {
        for (Callable<? extends T> c: callables)
            if (c.isValidArgs(args))
                return c.call(args);
        throw new GameException("No overloaded callable found for the given args.");
    }

    @Override
    public boolean isValidArgs(Object[] args) {
        for (Callable c: callables)
            if (c.isValidArgs(args))
                return true;
        return false;
    }

    @Override
    public Object callv(Object... args) {
        return call(args);
    }
    
    
    
}
