package com.openwes.core.utils;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public final class ClassLoadException extends Exception {

    public ClassLoadException() {
    }

    public ClassLoadException(String message) {
        super(message);
    }

    public ClassLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassLoadException(Throwable cause) {
        super(cause);
    }

    public ClassLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
