package com.openwes.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class ClassUtils {

    /**
     * @param c
     * @return get super class from a class
     */
    private static Set<Class<?>> supers(Class<?> c) {
        if (c == null) {
            return new HashSet<>();
        }

        Set<Class<?>> s = supers(c.getSuperclass());
        s.add(c);
        return s;
    }

    /**
     * @param a
     * @param b
     * @return lowest common super class of a and b
     */
    private static Class<?> lowestCommonSuper(Class<?> a, Class<?> b) {
        Set<Class<?>> aSupers = supers(a);
        while (!aSupers.contains(b)) {
            b = b.getSuperclass();
        }
        return b;
    }

    /**
     *
     * @param collection
     * @return guess component type of java collection
     */
    public final static Class<?> guessElementType(Collection<?> collection) {
        Class<?> guess = null;
        for (Object o : collection) {
            if (o != null) {
                if (guess == null) {
                    guess = o.getClass();
                } else if (guess != o.getClass()) {
                    guess = lowestCommonSuper(guess, o.getClass());
                }
            }
        }
        return guess;
    }

    /**
     *
     * @param iterator
     * @return component type of java iterator
     */
    public final static Class<?> guessElementType(Iterator<?> iterator) {
        Class<?> guess = null;
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o != null) {
                if (guess == null) {
                    guess = o.getClass();
                } else if (guess != o.getClass()) {
                    guess = lowestCommonSuper(guess, o.getClass());
                }
            }
        }
        return guess;
    }

    /**
     * @param key
     * @return class object that associated by key provided as parameter without
     * initialized.
     * @throws ClassLoadException
     */
    public final static Class load(String key) throws ClassLoadException {
        try {
            return findClass(key);
        } catch (SecurityException | ClassNotFoundException | IllegalArgumentException ex) {
            throw new ClassLoadException(ex);
        }
    }

    /**
     * @param <T>
     * @param key
     * @return object that associated with key provided without parameters in
     * constructor.
     * @throws ClassLoadException
     */
    public final static <T> T object(String key) throws ClassLoadException {
        try {
            /**
             * lookup from local class loader
             */
            Class clzz = findClass(key);
            Constructor<?> contructor = clzz.getConstructor();
            contructor.setAccessible(true);
            Object obj = contructor.newInstance();
            return (T) obj;
        } catch (NoSuchMethodException | ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new ClassLoadException(ex);
        }
    }

    private static Class findClass(String key) throws ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
        }
        return Class.forName(cleanASCIIText(key), false, loader);
    }

    /**
     * @param key
     * @param args
     * @param parameters
     * @return object that associated with key provided with many parameters in
     * constructor.
     * @throws ClassLoadException
     */
    public final static <T> T object(String key, Class[] parameters, Object[] args) throws ClassLoadException {
        try {
            Class clzz = findClass(key);
            Constructor<?> constructor = clzz.getConstructor(parameters);
            constructor.setAccessible(true);
            Object obj = constructor.newInstance(args);
            return (T) obj;
        } catch (NoSuchMethodException | ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new ClassLoadException(ex);
        }
    }

    public final static String cleanASCIIText(String text) {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return StringUtils.trimToEmpty(text);
    }

}
