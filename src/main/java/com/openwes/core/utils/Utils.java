package com.openwes.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import java.io.File;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author locngo@fortna.com
 * @since Jul 7, 2017
 * @version 1.0.0
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
     * @return
     */
    public final static byte[] bytes(String s) {
        Validate.notEmpty(s);
        return s.getBytes(UTF8);
    }

    /**
     * Encode object to bytes
     *
     * @param obj
     * @return
     */
    public final static byte[] bytes(Object obj) {
        Validate.notNull(obj);
        return bytes(marshal(obj));
    }

    /**
     * Decode string from byte array
     *
     * @param bytes
     * @return
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
     * @return
     */
    public final static <T extends Object> T object(byte[] jsonInBytes, Class<T> clzz) {
        return unmarshal(string(jsonInBytes), clzz);
    }

    public final static String marshal(Object o) {
        return GSON.toJson(o);
    }

    public final static <T extends Object> T unmarshal(String str, Class<T> clzz) {
        return GSON.fromJson(str, clzz);
    }

    public final static String getExtFile(String filePath) {
        for (int i = filePath.length() - 1; i >= 0; i--) {
            char c = filePath.charAt(i);
            if (c == '.') {
                return filePath.substring(i);
            }
        }
        return filePath;
    }

    public final static boolean checkHostAddress(String host, int port) {
        return checkHostAddress(host, port, 5000);
    }

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

    public final static Set<String> getIpProbable() {
        Set<String> probabilities = new HashSet<>();
        try {
            InetAddress inet = InetAddress.getLocalHost();
            InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
            if (ips != null) {
                for (InetAddress ip : ips) {
                    if (ip instanceof Inet6Address) {
                        continue;
                    }
                    probabilities.add(ip.getHostAddress());
                }
            }
        } catch (UnknownHostException e) {

        }
        return probabilities;
    }

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
