package com.openwes.core.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public final class MachineUtils {

    /**
     *
     * @return list of probably IP of current host
     */
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
                    if (ip == null) {
                        continue;
                    }
                    probabilities.add(ip.getHostAddress());
                }
            }
        } catch (UnknownHostException e) {

        }
        return probabilities;
    }

    public final static InetAddress findAnyProbalyIP() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (SocketException | UnknownHostException e) {
            throw new UnknownHostException("Failed to determine LAN address: " + e);
        }
    }

    public final static int getProcessId() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        String jvmName = runtimeBean.getName();
        return Integer.valueOf(jvmName.split("@")[0]);
    }

    public final static byte[] getCurrentIp() {
        try {
            String prefixIp = System.getenv("APP_IP");
            if (Validate.isEmpty(prefixIp)) {
                return ipv4StringToBytes(prefixIp);
            }
            return findAnyProbalyIP().getAddress();
        } catch (UnknownHostException ex) {
        }
        return new byte[]{127, 0, 0, 1};
    }

    public final static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "localhost";
    }

    /**
     * Return a value is hashed of MAC address
     *
     * @return integer
     */
    public final static int getMachineId() {
        int nodeId;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X", mac[i]));
                    }
                }
            }
            nodeId = FNVHash.hash32(sb.toString());
        } catch (Exception ex) {
            nodeId = (new SecureRandom().nextInt());
        }
        nodeId = nodeId & 0xFFFFFF;
        return nodeId;
    }

    /**
     * Return IP of service in format xxx.xxx.xxx.xxx
     *
     * @return string
     */
    public final static String getCurrentIpStr() {
        StringBuilder mBuilder = new StringBuilder();
        for (byte b : getCurrentIp()) {
            if (mBuilder.length() == 0) {
                mBuilder.append(b & 0xFF);
            } else {
                mBuilder.append(".")
                        .append(b & 0xFF);
            }
        }
        return mBuilder.toString();
    }

    /**
     * Convert IP to array of byte from format xxx.xxx.xxx.xxx
     *
     * @param ipaddr String
     * @return byte[]
     */
    public final static byte[] ipv4StringToBytes(String ipaddr) {

        //  Check if the string is valid
        if (ipaddr == null || ipaddr.length() < 7 || ipaddr.length() > 15) {
            return new byte[]{127, 0, 0, 1};
        }

        //  Check the address string, should be n.n.n.n format
        String[] tokens = StringUtils.split(ipaddr, ".");
        if (tokens.length != 4) {
            return new byte[]{127, 0, 0, 1};
        }

        return new byte[]{
            (byte) (Integer.valueOf(tokens[0]) & 0xFF),
            (byte) (Integer.valueOf(tokens[1]) & 0xFF),
            (byte) (Integer.valueOf(tokens[2]) & 0xFF),
            (byte) (Integer.valueOf(tokens[3]) & 0xFF)
        };
    }
}
