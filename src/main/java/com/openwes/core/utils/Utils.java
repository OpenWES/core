package com.openwes.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author xuanloc0511@gmail.com
 * 
 */
public final class Utils {

    public final static String EMPTY_STRING = "";
    public final static int MIN_PORT = 1;
    public final static int MAX_PORT = 65535;
    public final static Gson GSON = new GsonBuilder().create();
    public final static Charset UTF8 = Charset.forName("UTF-8");
    public final static AtomicLong GLOBAL_COUNTER = new AtomicLong(0);

    /**
     * Encode string to byte array
     *
     * @param s
     * @return a byte array
     */
    public final static byte[] bytes(String s) {
        Validate.notEmpty(s);
        return s.getBytes(UTF8);
    }

    /**
     * Encode object to bytes
     *
     * @param obj
     * @return a byte array
     */
    
    public final static byte[] bytes(Object obj) {
        Validate.notNull(obj);
        return bytes(marshal(obj));
    }

    /**
     * Decode string from byte array
     *
     * @param bytes
     * @return string
     */
    public final static String string(byte[] bytes) {
        return new String(bytes, UTF8);
    }

    /**
     * Decode object from JSON message that was encoded as byte array
     *
     * @param <T>
     * @param jsonInBytes
     * @param clzz
     * @return an object
     */
    public final static <T extends Object> T object(byte[] jsonInBytes, Class<T> clzz) {
        return unmarshal(string(jsonInBytes), clzz);
    }

    /**
     * Serialize an object to JSON
     * @param o
     * @return JSON string
     */
    public final static String marshal(Object o) {
        return GSON.toJson(o);
    }

    /**
     * De-serialize JSON string to a specific object
     * @param <T>
     * @param str
     * @param clzz
     * @return an object
     */
    public final static <T extends Object> T unmarshal(String str, Class<T> clzz) {
        return GSON.fromJson(str, clzz);
    }

    /**
     * Extract external name from path of a file
     * @param filePath
     * @return external name of file
     */
    public final static String getExtFile(String filePath) {
        for (int i = filePath.length() - 1; i >= 0; i--) {
            char c = filePath.charAt(i);
            if (c == '.') {
                return filePath.substring(i);
            }
        }
        return filePath;
    }

    /**
     * Check port is either already bind or not
     * @param host
     * @param port
     * @return true if it is available
     */
    public final static boolean checkHostAddress(String host, int port) {
        return checkHostAddress(host, port, 5000);
    }

    /**
     * Check port is either already bind or not within a duration
     * @param host
     * @param port
     * @param timeoutMs
     * @return true if it is available
     */
    public final static boolean checkHostAddress(String host, int port, int timeoutMs) {
        Socket socket = new Socket();
        try {
            SocketAddress inetSocket = new InetSocketAddress(host, port);
            socket.connect(inetSocket, timeoutMs);
            return true;
        } catch (IOException ex) {
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
            }
        }
        return false;
    }

    /**
     * Convert byte array to hex
     * @param arr
     * @return string
     */
    public final static String printMessageHex(byte[] arr) {
        StringBuilder mBuilder = new StringBuilder();
        try {
            for (byte a : arr) {
                mBuilder.append(String.format("%02x ", a));
            }
            return mBuilder.toString();
        } finally {
            mBuilder.setLength(0);
        }
    }

    /**
     * Extract a set of key from configuration
     * @param config
     * @param dotCounter
     * @return set of key
     */
    public final static Set<String> getSetOfKey(Config config, int dotCounter) {
        return config.entrySet()
                .stream().map((Map.Entry<String, ConfigValue> entry) -> {
                    String fullPath = entry.getKey();
                    return Utils.getPrefixOfKey(fullPath, 1);
                })
                .filter((t) -> {
                    return !Validate.isEmpty(t);
                })
                .collect(Collectors.toSet());
    }

    /**
     * Extract prefix of a specific key
     * @param fullKey
     * @param dotCounter
     * @return prefix
     */
    public final static String getPrefixOfKey(String fullKey, int dotCounter) {
        StringBuilder mBuilder = new StringBuilder();
        int dotCount = 0;
        for (char c : fullKey.toCharArray()) {
            if (c == '.') {
                dotCount++;
            }
            if (dotCount == dotCounter) {
                break;
            }
            mBuilder.append(c);
        }
        return mBuilder.toString();
    }

    public final static void createFolderIfNotExist(String path) {
        createFolderIfNotExist(path, true);
    }

    public final static void createFolderIfNotExist(String path, boolean required) {
        File f = new File(path);
        if (!f.exists()) {
            if (!f.mkdirs() && required) {
                throw new RuntimeException("Can not create folder at " + path);
            }
        }
    }

    public final static Set<String> getConfigNames(Config parent) {
        return parent.entrySet()
                .stream()
                .filter((entry) -> {
                    return !Validate.isEmpty(entry.getKey());
                })
                .map((Map.Entry<String, ConfigValue> entry) -> {
                    return Utils.getPrefixOfKey(entry.getKey(), 1);
                }).collect(Collectors.toSet());
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
