package com.miui.internal.util;

import android.util.Base64;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import miui.security.DigestUtils;
import miui.text.ExtraTextUtils;

public class DataUpdateUtils {
    public static boolean checkSignedUrl(Map map, String str, String str2) {
        String str3 = "appkey";
        String str4 = (String) map.get(str3);
        if (str4 == null || str4.length() == 0) {
            return false;
        }
        map.remove(str3);
        return str4.equals(genUrlSign(map, str, str2));
    }

    public static final String decodeBase64(String str) {
        return new String(Base64.decode(str, 0));
    }

    public static final byte[] decodeBase64Bytes(String str) {
        return Base64.decode(str, 0);
    }

    public static final String encodeBase64(String str, String str2) {
        return Base64.encodeToString(str.getBytes(str2), 2);
    }

    public static final String encodeBase64(byte[] bArr) {
        return Base64.encodeToString(bArr, 2);
    }

    public static String genUrlSign(Map map, String str, String str2) {
        if (map.isEmpty()) {
            return "";
        }
        String[] strArr = (String[]) map.keySet().toArray(new String[0]);
        Arrays.sort(strArr);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (String str3 : strArr) {
            sb.append(str3);
            sb.append((String) map.get(str3));
        }
        sb.append(str2);
        return ExtraTextUtils.toHexReadable(DigestUtils.get((CharSequence) sb.toString(), "SHA-1")).toUpperCase();
    }

    public static String getSignedUrl(Map map, String str, String str2) {
        String genUrlSign = genUrlSign(map, str, str2);
        if (genUrlSign.length() == 0) {
            return genUrlSign;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("appkey=");
        sb.append(str);
        sb.append("&sign=");
        sb.append(genUrlSign);
        for (Entry entry : map.entrySet()) {
            sb.append('&');
            sb.append((String) entry.getKey());
            sb.append('=');
            sb.append((String) entry.getValue());
        }
        return sb.toString();
    }

    public static long normalizedNo(String str) {
        return 123456;
    }
}
