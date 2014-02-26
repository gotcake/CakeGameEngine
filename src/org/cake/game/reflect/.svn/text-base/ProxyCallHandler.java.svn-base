/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cake.game.exception.GameException;

/**
 *
 * @author Aaron
 */
public class ProxyCallHandler implements InvocationHandler {

    private Map<Method, Callable> handlerMap;
    private Class<?> clazz;
    private Class<?>[] interfaces;

    public ProxyCallHandler(Class... interfaces) {
        handlerMap = new HashMap<>();
        this.interfaces = interfaces;
        clazz = Proxy.getProxyClass(ClassLoader.getSystemClassLoader(), interfaces);
    }
    public ProxyCallHandler(Class interfac) {
        handlerMap = new HashMap<>();
        this.interfaces = new Class[] {interfac};
        clazz = Proxy.getProxyClass(ClassLoader.getSystemClassLoader(), interfaces);
    }


    public Class<?> getProxyClass() {
        return clazz;
    }

    public Class[] getInterfaces() {
        return interfaces;
    }

    public Method[] getMethods() {
        List<Method> methods = new ArrayList<>();
        for (Class<?> c: interfaces) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods.toArray(new Method[methods.size()]);
    }

    public void mapMethod(Method m, Callable callable) {
        handlerMap.put(m, callable);
    }

    public void mapMethod(String methodName, Class[] paramTypes, Callable callable) {
        for (Class<?> c: interfaces) {
            try {
                handlerMap.put(Reflect.getMethod(c, methodName, paramTypes), callable);
            } catch (GameException e) {}
        }
    }

    public void mapMethod(String methodName, Callable callable) {
        for (Class<?> c: interfaces) {
            try {
                handlerMap.put(Reflect.getMethod(c, methodName), callable);
            } catch (GameException e) {}
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Callable c = handlerMap.get(method);
        if (c == null) {
            return null;
        }
        return c.call(args);
    }

}
