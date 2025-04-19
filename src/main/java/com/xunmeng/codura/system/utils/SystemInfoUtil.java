package com.xunmeng.codura.system.utils;
import com.intellij.openapi.application.ApplicationInfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
public abstract class SystemInfoUtil {
    /*获取IDE版本信息*/
    public static String ideVersion(){
        ApplicationInfo appInfo = ApplicationInfo.getInstance();
        String versionName = appInfo.getVersionName();  // IDE名称，如IntelliJ IDEA
        String fullVersion = appInfo.getFullVersion();  // 完整版本号
        String buildNumber = appInfo.getBuild().asString();  // 构建号
        return versionName + " " + fullVersion + " (Build " + buildNumber + ")";
    }

    /**
     * 获取非localhost的IP地址，可以是局域网IP
     * @return 非localhost的IP地址
     */
    public static String ipAddress() {
        InetAddress ip = getIPAddress();
        return (ip != null) ? ip.getHostAddress() : "127.0.0.1";
    }
    
    /*获取MAC地址*/
    public static String macAddress() {
        try {
            InetAddress ip = getIPAddress();
            if (ip == null) {
                return ":::";
            }
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] macBytes = network.getHardwareAddress();
            StringBuilder macAddress = new StringBuilder();
            for (int i = 0; i < macBytes.length; i++) {
                macAddress.append(String.format("%02X%s", macBytes[i], (i < macBytes.length - 1) ? ":" : ""));
            }
            return macAddress.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return ":::";

        }
    }
    
    /**
     * 获取非localhost的IP地址的帮助方法
     * @return InetAddress对象，代表非localhost的IP
     */
    private static InetAddress getIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                return null;
            }
        }
        return null;
    }
}
