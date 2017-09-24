package com.bigdata.iplocation.utils;

import java.net.InetAddress;

/**
 * Ipv4 address utils
 * Created on 2017/9/23.
 */
public class Ipv4Address {

    public static long str2Long(String strIp) {
        String[] ip = strIp.split("\\.");
        if (ip.length == 4) {
            long l0, l1, l2, l3;
            l0 = Long.parseLong(ip[0]);
            l1 = Long.parseLong(ip[1]);
            l2 = Long.parseLong(ip[2]);
            l3 = Long.parseLong(ip[3]);
            if (0 <= l0 && l0 <= 255 && 0 <= l1 && l1 <= 255 && 0 <= l2 && l2 <= 255 && 0 <= l3 && l3 <= 255) {
                return (l0 << 24) + (l1 << 16) + (l2 << 8) + l3;
            }
        }
        return 0L;
    }

    public static String long2Str(Long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    public static Long inetAddress2Long(InetAddress inetAddress) {
        byte[] ipByte = inetAddress.getAddress();
        long[] ipLong = new long[ipByte.length];
        for (int i = 0; i < ipByte.length; i++) {
            byte ipSegment = ipByte[i];
            long newIPSegment = (ipSegment < 0) ? 256 + ipSegment : ipSegment;
            ipLong[i] = newIPSegment;
        }
        return (ipLong[0] << 24) + (ipLong[1] << 16) + (ipLong[2] << 8) + ipLong[3];

    }
}
