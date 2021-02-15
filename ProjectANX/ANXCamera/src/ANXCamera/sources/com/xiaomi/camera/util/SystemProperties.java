package com.xiaomi.camera.util;

import com.android.camera.log.Log;

public class SystemProperties {
    private static final Class SP = getSystemPropertiesClass();
    private static final String TAG = "SystemProperties";

    private SystemProperties() {
    }

    public static String get(String str) {
        try {
            return (String) SP.getMethod("get", new Class[]{String.class}).invoke(null, new Object[]{str});
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting system property: ", (Throwable) e);
            return null;
        }
    }

    public static String get(String str, String str2) {
        try {
            return (String) SP.getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{str, str2});
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting system property: ", (Throwable) e);
            return str2;
        }
    }

    public static boolean getBoolean(String str, boolean z) {
        try {
            return ((Boolean) SP.getMethod("getBoolean", new Class[]{String.class, Boolean.TYPE}).invoke(null, new Object[]{str, Boolean.valueOf(z)})).booleanValue();
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting system property: ", (Throwable) e);
            return z;
        }
    }

    public static float getFloat(String str, float f) {
        return Float.valueOf(get(str, String.valueOf(f))).floatValue();
    }

    public static int getInt(String str, int i) {
        try {
            return ((Integer) SP.getMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke(null, new Object[]{str, Integer.valueOf(i)})).intValue();
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting system property: ", (Throwable) e);
            return i;
        }
    }

    public static long getLong(String str, long j) {
        try {
            return ((Long) SP.getMethod("getLong", new Class[]{String.class, Long.TYPE}).invoke(null, new Object[]{str, Long.valueOf(j)})).longValue();
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting system property: ", (Throwable) e);
            return j;
        }
    }

    private static Class getSystemPropertiesClass() {
        try {
            return Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException unused) {
            Log.e(TAG, "'android.os.SystemProperties' not found");
            return null;
        }
    }
}
