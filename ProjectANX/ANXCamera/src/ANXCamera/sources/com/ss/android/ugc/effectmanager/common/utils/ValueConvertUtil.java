package com.ss.android.ugc.effectmanager.common.utils;

public class ValueConvertUtil {
    public static long ConvertStringToLong(String str, long j) {
        if (str == null || str.isEmpty()) {
            return j;
        }
        try {
            j = Long.valueOf(str).longValue();
            return j;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return j;
        }
    }
}
