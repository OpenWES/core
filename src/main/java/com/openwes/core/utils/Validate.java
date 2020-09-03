package com.openwes.core.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Validate {

    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_EMPTY_EX_MESSAGE = "The validated string is empty";

    public final static boolean isEmpty(String string) {
        return string == null || StringUtils.trimToEmpty(string).length() == 0;
    }

    public final static void notEmpty(String string) {
        if (isEmpty(string)) {
            throw new NullPointerException(DEFAULT_IS_EMPTY_EX_MESSAGE);
        }
    }

    public final static boolean isNull(Object object) {
        return object == null;
    }

    public final static <T> T notNull(final T object) {
        return notNull(object, DEFAULT_IS_NULL_EX_MESSAGE);
    }

    public final static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }

    public final static void isNumber(String s) {
        String tmp = s;
        if (tmp.startsWith("-")) {
            tmp = tmp.substring(1);
        }
        for (char c : tmp.toCharArray()) {
            if (c < '0' || c > '9') {
                throw new RuntimeException(s + " is not number");
            }
        }
    }

    // COLLECTION VALIDATION
    public final static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public final static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public final static boolean isNullOrEmpty(Iterator iterator) {
        return iterator == null || !iterator.hasNext();
    }

    public final static boolean isNullOrEmpty(Object... array) {
        return array == null || array.length == 0;
    }
}