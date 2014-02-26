/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.reflect;

import java.lang.reflect.Array;

/**
 *
 * @author Aaron
 */
public class ArgumentCallable<T> implements Callable, Runnable{
    
    private Callable<? extends T> callable;
    private Callable argProducer;
    private Object[] defaultArgs;
    private T result;
    
    public ArgumentCallable(Callable<? extends T> callable, Object[] defaultArgs) {
        this.callable = callable;
        this.defaultArgs = defaultArgs;
    }
    
    public ArgumentCallable(Callable<? extends T> callable, Callable argumentProducer) {
        this.callable = callable;
        this.argProducer = argumentProducer;
        this.defaultArgs = Callable.EMPTY_ARGS;
    }
    
    public ArgumentCallable(Callable<? extends T> callable) {
        this.callable = callable;
        this.defaultArgs = Callable.EMPTY_ARGS;
    }

    @Override
    public T call(Object[] args) {
        result = callable.call((argProducer != null ? toObjectArray(argProducer.call(Callable.EMPTY_ARGS)) : defaultArgs));
        return result;
    }

    @Override
    public boolean isValidArgs(Object[] args) {
        return args.length == 0;
    }

    @Override
    public void run() {
        result = callable.call((argProducer != null ? toObjectArray(argProducer.call(Callable.EMPTY_ARGS)) : defaultArgs));
    }
    
    public T getResult() {
        return result;
    }

    private Object[] toObjectArray(Object obj) {
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return (Object[])obj;
            } else {
                Object[] arr = new Object[Array.getLength(obj)];
                for (int i=0; i<arr.length; i++) {
                    arr[i] = Array.get(obj, i);
                }
                return arr;
            }
        } else {
            return new Object[]{obj};
        }
    }

    @Override
    public T callv(Object... args) {
        return call(args);
    }
    
}
