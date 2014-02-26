/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.cake.game.exception.GameException;

/**
 *
 * @author Aaron
 */
public class Reflect {

    public static Object[] args(Object... args) {
        return args;
    }

    public static Class[] getObjectTypes(Object[] objects) {
        Class[] carr = new Class[objects.length];
        for (int i=0; i<objects.length; i++) {
            carr[i] = objects[i].getClass();
        }
        return carr;
    }

    public static boolean areValidArgs(Class[] paramTypes, Object[] args) {
        if (paramTypes.length != args.length) return false;
        for (int i=0; i<args.length; i++) {
            if (!paramTypes[i].isInstance(args[i]))
                return false;
        }
        return true;
    }

    public static boolean areValidArgTypes(Class[] paramTypes, Class[] argTypes) {
        if (paramTypes.length != argTypes.length) return false;
        for (int i=0; i<argTypes.length; i++) {
            if (!paramTypes[i].isAssignableFrom(argTypes[i]))
                return false;
        }
        return true;
    }

    public static Method getMethod(Class clazz, String name, Class[] paramTypes) {
        try {
            Method m = clazz.getDeclaredMethod(name, paramTypes);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException|SecurityException ex) {
            throw new GameException(ex);
        }
    }

    public static Method getMethod(Class clazz, String name) {
        Method[] methods = getMethods(clazz, name);
        if (methods.length == 0) {
            throw new GameException("No method named '" + name + "' found in class " + clazz.getSimpleName());
        } else if (methods.length > 1) {
            throw new GameException("Multiple methods named '" + name + "' found in class " + clazz.getSimpleName());
        }
        return methods[0];
    }

    public static Method getMethod(String classAndMethodName, Class[] paramTypes) {
        int pos = classAndMethodName.lastIndexOf('.');
        if (pos == -1 || pos == 0 || pos == classAndMethodName.length() - 1) {
            throw new GameException("Both a class name and a method name must be provided, separated by a period.");
        }
        Class clazz = null;
        try {
            clazz = Class.forName(classAndMethodName.substring(0, pos));
        } catch (ClassNotFoundException ex) {
            throw new GameException(ex);
        }
        return getMethod(clazz, classAndMethodName.substring(pos + 1), paramTypes);
    }

    public static Method getMethod(String classAndMethodName) {
        int pos = classAndMethodName.lastIndexOf('.');
        if (pos == -1 || pos == 0 || pos == classAndMethodName.length() - 1) {
            throw new GameException("Both a class name and a method name must be provided, separated by a period.");
        }
        Class clazz = null;
        try {
            clazz = Class.forName(classAndMethodName.substring(0, pos));
        } catch (ClassNotFoundException ex) {
            throw new GameException(ex);
        }
        Method[] methods = getMethods(clazz, classAndMethodName.substring(pos + 1));
        if (methods.length > 1) {
            throw new GameException("Multiple methods named '" + classAndMethodName.substring(pos + 1) + "' found in class '" + clazz.getSimpleName() + "'.");
        }
        return methods[0];
    }

    public static Method[] getMethods(Class clazz, String methodName) {
        List<Method> methods = new ArrayList<>();
        for (Method m: clazz.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
                m.setAccessible(true);
                methods.add(m);
            }
        }
        if (methods.isEmpty())
            throw new GameException("No method named '" + methodName + "' found in class '" + clazz.getSimpleName() + "'.");
        return methods.toArray(new Method[methods.size()]);
    }

    public static Method[] getMethods(String classAndMethodName) {
        int pos = classAndMethodName.lastIndexOf('.');
        if (pos == -1 || pos == 0 || pos == classAndMethodName.length() - 1) {
            throw new GameException("Both a class name and a method name must be provided, separated by a period.");
        }
        Class clazz = null;
        try {
            clazz = Class.forName(classAndMethodName.substring(0, pos));
        } catch (ClassNotFoundException ex) {
            throw new GameException(ex);
        }
        return getMethods(clazz, classAndMethodName.substring(pos + 1));
    }

    public static Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw new GameException(ex);
        }
    }

    public static Field getField(Class clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new GameException(ex);
        }
    }

    public static Field getField(String classAndFieldName) {
        int pos = classAndFieldName.lastIndexOf('.');
        if (pos == -1 || pos == 0 || pos == classAndFieldName.length() - 1) {
            throw new GameException("Both a class name and a field name must be provided, separated by a period.");
        }
        Class clazz = null;
        try {
            clazz = Class.forName(classAndFieldName.substring(0, pos));
        } catch (ClassNotFoundException ex) {
            throw new GameException(ex);
        }
        return getField(clazz, classAndFieldName.substring(pos + 1));
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class[] paramTypes) {
        try {
            Constructor c = clazz.getDeclaredConstructor(paramTypes);
            c.setAccessible(true);
            return c;
        } catch (NoSuchMethodException|SecurityException ex) {
            throw new GameException(ex);
        }
    }

    public static <T> Constructor<T>[] getConstructors(Class<T> clazz) {
        Constructor[] con = clazz.getDeclaredConstructors();
        for (Constructor c: con)
            c.setAccessible(true);
        return con;
    }

    /**
     * Creates a proxy from a proxy call handler and a list of interfaces that it should handle.
     * @param handler
     * @param interfaces
     * @return
     */
    public  static Object createProxy(ProxyCallHandler handler, Class[] interfaces) {
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces, handler);
    }

    /**
     * Creates a proxy from an object that has methods tagged by the @Implements annotation
     * @param <T>
     * @param obj
     * @param interfaces
     * @return
     */
    public static <T> T createProxy(Object obj, Class<? extends T>... interfaces) {
        ProxyCallHandler handler = new ProxyCallHandler(interfaces);
        for (Method m2: obj.getClass().getDeclaredMethods()) {
            Implements a = m2.getAnnotation(Implements.class);
            if (a != null) {
                for (Class c: interfaces) {
                    for (String methodName: a.value()) {
                        try {
                            Method im = c.getDeclaredMethod(methodName, m2.getParameterTypes());
                            m2.setAccessible(true);
                            handler.mapMethod(im, new MethodCallable(obj, m2));
                        } catch (NoSuchMethodException | SecurityException ex) { }
                    }
                }
            }
        }
        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces, handler);
    }

    /**
     * Creates a proxy from a class that has methods tagged with the @Implements annotation
     * @param <T>
     * @param clazz
     * @param interfaces
     * @return
     */
    public static <T> T createProxy(Class clazz, Class<? extends T>... interfaces) {
        ProxyCallHandler handler = new ProxyCallHandler(interfaces);
        for (Method m2: clazz.getDeclaredMethods()) {
            Implements a = m2.getAnnotation(Implements.class);
            if (a != null) {
                for (Class c: interfaces) {
                    for (String methodName: a.value()) {
                        try {
                            Method im = c.getDeclaredMethod(methodName, m2.getParameterTypes());
                            m2.setAccessible(true);
                            handler.mapMethod(im, new MethodCallable(clazz, m2));
                        } catch (NoSuchMethodException | SecurityException ex) { }
                    }
                }
            }
        }
        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces, handler);
    }

    /**
     * Creates a proxy for a single method in an interface with the given object's method.
     * @param <T>
     * @param interfac
     * @param interfaceMethodName
     * @param obj
     * @param methodName
     * @return
     */
    public static <T> T createProxy(Class<T> interfac, String interfaceMethodName, Object obj, String methodName) {
        ProxyCallHandler handler = new ProxyCallHandler(interfac);
        handler.mapMethod(Reflect.getMethod(interfac, interfaceMethodName), new MethodCallable(obj, methodName));
        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] {interfac}, handler);
    }

    /**
     * Creates a proxy for a single method in an interface with the given class' static method.
     * @param <T>
     * @param interfac
     * @param interfaceMethodName
     * @param clazz
     * @param staticMethodName
     * @return
     */
    public static <T> T createProxy(Class<T> interfac, String interfaceMethodName, Class clazz, String staticMethodName) {
        ProxyCallHandler handler = new ProxyCallHandler(interfac);
        handler.mapMethod(Reflect.getMethod(interfac, interfaceMethodName), new MethodCallable(clazz, staticMethodName));
        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] {interfac}, handler);
    }

    /**
     * Creates a proxy for a single method in an interface with the given class' static method.
     * @param <T>
     * @param interfac
     * @param interfaceMethodName
     * @param classAndStaticMethodName
     * @return
     */
    public static <T> T createProxy(Class<T> interfac, String interfaceMethodName, String classAndStaticMethodName) {
        ProxyCallHandler handler = new ProxyCallHandler(interfac);
        handler.mapMethod(Reflect.getMethod(interfac, interfaceMethodName), new MethodCallable(classAndStaticMethodName));
        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] {interfac}, handler);
    }

    /**
     * Create a proxy for a single interface method with the given callable.
     * @param <T>
     * @param interfac
     * @param interfaceMethodName
     * @param callable
     * @return
     */
    public static <T> T createProxy(Class<T> interfac, String interfaceMethodName, Callable callable) {
        ProxyCallHandler handler = new ProxyCallHandler(interfac);
        handler.mapMethod(Reflect.getMethod(interfac, interfaceMethodName), callable);
        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] {interfac}, handler);
    }

}
