/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cake.game.exception.GameException;

/**
 *
 * @author Aaron
 */
public class MethodCallable<T> implements Callable {
    
    private Method method;
    private Object instance;
    
    public MethodCallable(Object thisObj, Method m) {
        instance = thisObj;
        method = m;
    }
    
    public MethodCallable(Object thisObj, String methodName) {
        instance = thisObj;
        method = Reflect.getMethod(instance.getClass(), methodName);
    }
    
    public MethodCallable(Class clazz, String staticMethodName) {
        instance = null;
        method = Reflect.getMethod(clazz, staticMethodName);
    }
    
    public MethodCallable(String classAndStaticMethodName) {
        instance = null;
        method = Reflect.getMethod(classAndStaticMethodName);
    }
    
    public MethodCallable(Object thisObj, String methodName, Class[] paramTypes) {
        instance = thisObj;
        method = Reflect.getMethod(thisObj.getClass(), methodName, paramTypes);
    }
    
    public MethodCallable(Class clazz, String staticMethodName, Class[] paramTypes) {
        instance = null;
        method = Reflect.getMethod(clazz, staticMethodName, paramTypes);
    }
    
    public MethodCallable(String classAndStaticMethodName, Class[] paramTypes) {
        instance = null;
        method = Reflect.getMethod(classAndStaticMethodName, paramTypes);
    }

    @Override
    public T call(Object[] args) {
        try {
            return (T)method.invoke(instance, args);
        } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException ex) {
            throw new GameException(ex);
        }
    }

    public Method getMethod() {
        return method;
    }
    
    public Object getInstance() {
        return instance;
    }

    @Override
    public boolean isValidArgs(Object[] args) {
        return Reflect.areValidArgs(method.getParameterTypes(), args);
    }

    @Override
    public T callv(Object... args) {
        return call(args);
    }
    
    
}
