package com.ss.android.ttve.utils;

public class TEStringUtils {
    public static String getFilename(String str) {
        if (isEmpty(str)) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf != -1) {
            str = str.substring(lastIndexOf + 1, str.length());
        }
        return str;
    }

    public static String getFilenameWithoutSuffix(String str) {
        if (isEmpty(str)) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf("/") + 1;
        int lastIndexOf2 = str.lastIndexOf(".");
        if (lastIndexOf2 == -1) {
            str = str.substring(0, lastIndexOf - 1);
        } else if (lastIndexOf2 > lastIndexOf) {
            str = str.substring(lastIndexOf, lastIndexOf2);
        }
        return str;
    }

    public static String getFolderPath(String str) {
        if (isEmpty(str)) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf != -1) {
            str = str.substring(0, lastIndexOf + 1);
        }
        return str;
    }

    public static boolean isEmpty(String str) {
        return str == null || str == "";
    }
}
