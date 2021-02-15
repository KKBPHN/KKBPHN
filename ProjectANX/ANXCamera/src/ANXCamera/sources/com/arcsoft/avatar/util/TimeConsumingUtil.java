package com.arcsoft.avatar.util;

import java.util.HashMap;

public class TimeConsumingUtil {
    public static boolean DEBUG = false;
    private static HashMap a = new HashMap();
    private static final String b = "PERFORMANCE";

    public static void startTheTimer(String str) {
        if (DEBUG) {
            HashMap hashMap = a;
            if (hashMap != null) {
                hashMap.put(str, Long.valueOf(System.currentTimeMillis()));
            }
        }
    }

    public static void stopTiming(String str) {
        if (DEBUG) {
            HashMap hashMap = a;
            if (hashMap != null && hashMap.containsKey(str)) {
                long longValue = ((Long) a.get(str)).longValue();
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(str);
                sb.append(" : ");
                sb.append(System.currentTimeMillis() - longValue);
                LOG.d(b, sb.toString());
            }
        }
    }

    public static void stopTiming(String str, String str2) {
        if (DEBUG) {
            HashMap hashMap = a;
            if (hashMap != null && hashMap.containsKey(str2)) {
                long longValue = ((Long) a.get(str2)).longValue();
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(str2);
                sb.append(" : ");
                sb.append(System.currentTimeMillis() - longValue);
                LOG.d(str, sb.toString());
            }
        }
    }
}
