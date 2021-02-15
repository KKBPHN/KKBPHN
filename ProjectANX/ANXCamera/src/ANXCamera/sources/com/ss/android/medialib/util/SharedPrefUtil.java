package com.ss.android.medialib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SharedPrefUtil {
    private static final String sDeviceId = "DeviceId";
    private static final String sIsOpenBeauty = "IsOpenBeauty";
    private static final String sIsSensorsDataUpload = "IsSensorsDataUpload";
    private static final String sPerfConfigPrefix = "PerfConfig_";
    private static final String sShortVideo = "ShortVideo";
    private static final String sVideoHeight = "VideoHeight";
    private static final String sVideoLocalPath = "VideoLocalPath";
    private static final String sVideoText = "VideoText";
    private static final String sVideoWidth = "VideoWidth";
    private static final String sVidoThumb = "VideoThumb";

    @RestrictTo({Scope.LIBRARY})
    public static String getDeviceId(Context context) {
        return context.getSharedPreferences(sShortVideo, 0).getString(sDeviceId, null);
    }

    public static boolean getIsOpenBeauty(Context context) {
        String str = sIsOpenBeauty;
        return context.getSharedPreferences(str, 0).getBoolean(str, true);
    }

    @RestrictTo({Scope.LIBRARY})
    public static boolean getIsSensorsDataUpload(Context context) {
        return context.getSharedPreferences(sShortVideo, 0).getBoolean(sIsSensorsDataUpload, false);
    }

    public static Map getPerformanceConfig(Context context) {
        HashMap hashMap = new HashMap();
        for (Entry entry : context.getSharedPreferences(sShortVideo, 0).getAll().entrySet()) {
            if (((String) entry.getKey()).startsWith(sPerfConfigPrefix)) {
                hashMap.put(((String) entry.getKey()).substring(11), (Integer) entry.getValue());
            }
        }
        return hashMap;
    }

    public static int getVideoHeight(Context context) {
        return context.getSharedPreferences(sShortVideo, 0).getInt(sVideoHeight, 0);
    }

    public static String getVideoLocalPath(Context context) {
        return context.getSharedPreferences(sShortVideo, 0).getString(sVideoLocalPath, null);
    }

    public static String getVideoText(Context context) {
        return context.getSharedPreferences(sShortVideo, 0).getString(sVideoText, null);
    }

    public static String getVideoThumb(Context context) {
        return context.getSharedPreferences(sShortVideo, 0).getString(sVidoThumb, null);
    }

    public static int getVideoWidth(Context context) {
        return context.getSharedPreferences(sShortVideo, 0).getInt(sVideoWidth, 0);
    }

    @RestrictTo({Scope.LIBRARY})
    public static void setDeviceId(Context context, String str) {
        context.getSharedPreferences(sShortVideo, 0).edit().putString(sDeviceId, str).apply();
    }

    public static void setIsOpenBeauty(Context context, boolean z) {
        String str = sIsOpenBeauty;
        Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    @RestrictTo({Scope.LIBRARY})
    public static void setIsSensorsDataUpload(Context context) {
        context.getSharedPreferences(sShortVideo, 0).edit().putBoolean(sIsSensorsDataUpload, true).apply();
    }

    public static void setPerformanceConfig(Context context, Map map) {
        if (map != null) {
            Set<Entry> entrySet = map.entrySet();
            SharedPreferences sharedPreferences = context.getSharedPreferences(sShortVideo, 0);
            for (Entry entry : entrySet) {
                Editor edit = sharedPreferences.edit();
                StringBuilder sb = new StringBuilder();
                sb.append(sPerfConfigPrefix);
                sb.append((String) entry.getKey());
                edit.putInt(sb.toString(), ((Integer) entry.getValue()).intValue()).apply();
            }
        }
    }

    public static void setVideoHeight(Context context, int i) {
        Editor edit = context.getSharedPreferences(sShortVideo, 0).edit();
        edit.putInt(sVideoHeight, i);
        edit.apply();
    }

    public static void setVideoLocalPath(Context context, String str) {
        Editor edit = context.getSharedPreferences(sShortVideo, 0).edit();
        edit.putString(sVideoLocalPath, str);
        edit.apply();
    }

    public static void setVideoText(Context context, String str) {
        Editor edit = context.getSharedPreferences(sShortVideo, 0).edit();
        edit.putString(sVideoText, str);
        edit.apply();
    }

    public static void setVideoThumb(Context context, String str) {
        Editor edit = context.getSharedPreferences(sShortVideo, 0).edit();
        edit.putString(sVidoThumb, str);
        edit.apply();
    }

    public static void setVideoWidth(Context context, int i) {
        Editor edit = context.getSharedPreferences(sShortVideo, 0).edit();
        edit.putInt(sVideoWidth, i);
        edit.apply();
    }
}
