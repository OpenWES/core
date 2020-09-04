package com.openwes.core;

import com.typesafe.config.Config;

/**
 * Inversion of Control
 *
 * @author xuanloc0511@gmail.com
 */
public class IOC {

    public final static IOC INSTANCE = new IOC();

    public final static IOC instance() {
        return INSTANCE;
    }

    private final IOCRegistry registry = new IOCRegistry();

    private IOC() {

    }

    synchronized void start(Config config) {

    }

    public final static <P extends Object, T extends P> T resolve(Class<P> clzz) {
        return null;
    }
}
