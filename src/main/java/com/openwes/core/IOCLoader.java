package com.openwes.core;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class IOCLoader extends ClassLoader {

    public IOCLoader(ClassLoader cl) {
        super(cl);
    }

    public IOCLoader() {
        super();
    }

    private boolean needsModifying(String name) {
        // TODO
        return false;
    }

    private byte[] modifyClass(InputStream original) throws IOException {
        // TODO
        return null;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (needsModifying(name)) {
            try {
                InputStream classData = getResourceAsStream(name.replace('.', '/') + ".class");
                if (classData == null) {
                    throw new ClassNotFoundException("class " + name + " is not findable");
                }
                byte[] array = modifyClass(classData);
                return defineClass(name, array, 0, array.length);
            } catch (IOException io) {
                throw new ClassNotFoundException("", io);
            }
        } else {
            return super.findClass(name);
        }
    }
}
