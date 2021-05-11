package com.blr19c.common.remote.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * ip获取器 一定程度上支持多网卡
 *
 * @author blr
 * @since 2021.5.11
 */
public class IPUtil {

    public static String getIp() {
        try {
            List<String> hostList = new ArrayList<>();
            Enumeration<NetworkInterface> networkInterfaceList = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceList.hasMoreElements()) {
                NetworkInterface netInterface = networkInterfaceList.nextElement();
                // 去除回环接口，子接口，未运行和接口
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp())
                    continue;
                Enumeration<InetAddress> addressList = netInterface.getInetAddresses();
                while (addressList.hasMoreElements()) {
                    InetAddress ip = addressList.nextElement();
                    if (ip instanceof Inet4Address) {
                        hostList.add(ip.getHostAddress());
                    }
                }
            }
            //最早注册的有效网卡
            return hostList.isEmpty() ? InetAddress.getLocalHost().getHostAddress() : hostList.get(hostList.size() - 1);
        } catch (Exception e) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException unknownHostException) {
                throw new IllegalArgumentException(unknownHostException.getMessage(), e);
            }
        }
    }
}
