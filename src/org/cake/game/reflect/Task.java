/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.reflect;

import java.lang.reflect.Array;
import java.util.concurrent.ForkJoinTask;

/**
 *
 * @author Aaron Cake
 */
public class Task<T> extends ForkJoinTask {
   
    private Callable<? extends T> callable;
    private Callable argProducer;
    private Object[] defaultArgs;
    private T result;
    
    public Task(Callable<? extends T> callable, Object[] defaultArgs) {
        this.callable = callable;
        this.defaultArgs = defaultArgs;
    }
    
    public Task(Callable<? extends T> callable, Callable argumentProducer) {
        this.callable = callable;
        this.argProducer = argumentProducer;
        this.defaultArgs = Callable.EMPTY_ARGS;
    }
    
    public Task(Callable<? extends T> callable) {
        this.callable = callable;
        this.defaultArgs = Callable.EMPTY_ARGS;
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
    public Object getRawResult() {
        return result;
    }

    @Override
    protected void setRawResult(Object value) {
        result = (T)value;
    }

    @Override
    protected boolean exec() {
        result = callable.call((argProducer != null ? toObjectArray(argProducer.call(Callable.EMPTY_ARGS)) : defaultArgs));
        return true;        
    }
    
}
