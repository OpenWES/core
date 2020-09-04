package com.openwes.core.utils;

import java.util.Map;

/**
 *
 * @author Deadpool {@literal (locngo@fortna.com)}
 * @since Jun 24, 2019
 * @version 1.0.0
 *
 */
public class StrConv {

    public final static boolean stringToBoolean(String v) {
        return Validate.isEmpty(v) ? false : Boolean.valueOf(v);
    }

    //Number utilities
    public final static int stringToInt(String v) {
        try {
            return Validate.isEmpty(v) ? 0 : Integer.valueOf(v);
        } catch (NumberFormatException e) {

        }
        return 0;
    }

    public final static int stringToIntOrErr(String v) {
        try {
            return Validate.isEmpty(v) ? 0 : Integer.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not cast " + v + " to integer");
        }
    }

    public final static short stringToShort(String v) {
        try {
            return Validate.isEmpty(v) ? 0 : Short.valueOf(v);
        } catch (NumberFormatException e) {

        }
        return 0;
    }

    public final static short stringToShortOrErr(String v) {
        try {
            return Validate.isEmpty(v) ? 0 : Short.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not cast " + v + " to short");
        }
    }

    public final static long stringToLong(String v) {
        try {
            return Validate.isEmpty(v) ? 0 : Long.valueOf(v);
        } catch (NumberFormatException e) {

        }
        return 0;
    }

    public final static long stringToLongOrErr(String v) {
        try {
            return Validate.isEmpty(v) ? 0 : Long.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not cast " + v + " to long");
        }
    }

    public final static float stringToFloat(String v) {
        try {
            return Validate.isEmpty(v) ? 0f : Float.valueOf(v);
        } catch (NumberFormatException e) {

        }
        return 0f;
    }

    public final static float stringToFloatOrErr(String v) {
        try {
            return Validate.isEmpty(v) ? 0f : Float.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not cast " + v + " to float");
        }
    }

    public final static double stringToDouble(String v) {
        try {
            return Validate.isEmpty(v) ? 0f : Double.valueOf(v);
        } catch (NumberFormatException e) {

        }
        return 0f;
    }

    public final static double stringToDoubleOrErr(String v) {
        try {
            return Validate.isEmpty(v) ? 0f : Double.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not cast " + v + " to double");
        }
    }

    //FROM MAP
    public final static boolean getBoolean(Map<String, String> map, String key) {
        return StrConv.stringToBoolean(map.get(key));
    }

    public final static short getShort(Map<String, String> map, String key) {
        return StrConv.stringToShort(map.get(key));
    }

    public final static short getShortOrErr(Map<String, String> map, String key) {
        return StrConv.stringToShortOrErr(map.get(key));
    }

    public final static int getInt(Map<String, String> map, String key) {
        return StrConv.stringToInt(map.get(key));
    }

    public final static int getIntOrErr(Map<String, String> map, String key) {
        return StrConv.stringToIntOrErr(map.get(key));
    }

    public final static long getLong(Map<String, String> map, String key) {
        return StrConv.stringToLong(map.get(key));
    }

    public final static long getLongOrErr(Map<String, String> map, String key) {
        return StrConv.stringToLongOrErr(map.get(key));
    }

    public final static float getFloat(Map<String, String> map, String key) {
        return StrConv.stringToFloat(map.get(key));
    }

    public final static float getFloatOrError(Map<String, String> map, String key) {
        return StrConv.stringToFloatOrErr(map.get(key));
    }

    public final static double getDouble(Map<String, String> map, String key) {
        return StrConv.stringToDouble(map.get(key));
    }

    public final static double getDoubleOrErr(Map<String, String> map, String key) {
        return StrConv.stringToDoubleOrErr(map.get(key));
    }
    //End
}
