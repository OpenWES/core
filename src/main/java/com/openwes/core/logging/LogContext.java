package com.openwes.core.logging;

import org.slf4j.MDC;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class LogContext {

    public final static String TXID = "txid";

    public final static void set(String key, String value) {
        MDC.put(key, value);
    }

    public final static void remove(String key) {
        MDC.remove(key);
    }
}
