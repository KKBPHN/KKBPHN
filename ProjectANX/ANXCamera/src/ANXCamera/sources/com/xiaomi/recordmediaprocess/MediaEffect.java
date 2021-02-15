package com.xiaomi.recordmediaprocess;

import android.util.Log;
import java.util.Map;

public class MediaEffect {
    private static String TAG = "MediaEffect";

    public static long CreateEffect(EffectType effectType) {
        long CreateEffectJni = CreateEffectJni(effectType.ordinal());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("create effect, type id: ");
        sb.append(effectType);
        sb.append(", effect id:");
        sb.append(CreateEffectJni);
        Log.d(str, sb.toString());
        return CreateEffectJni;
    }

    public static native long CreateEffectJni(int i);

    public static void DestoryEffect(long j) {
        DestoryEffectJni(j);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("destory effect id: ");
        sb.append(j);
        Log.d(str, sb.toString());
    }

    public static native void DestoryEffectJni(long j);

    public static void SetCoverCallback(long j, EffectCoverNotifier effectCoverNotifier) {
        Log.d(TAG, "set EffectCoverCallback");
        SetCoverCallbackJni(j, effectCoverNotifier);
    }

    public static native void SetCoverCallbackJni(long j, EffectCoverNotifier effectCoverNotifier);

    public static boolean SetParamsForAudioTrack(long j, Map map) {
        String[] strArr;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("set param for audio track  id: ");
        sb.append(j);
        Log.d(str, sb.toString());
        int i = 0;
        if (map == null || map.size() == 0) {
            Log.d(TAG, "Param Map: <null, null>");
            strArr = new String[0];
        } else {
            strArr = new String[(map.size() * 2)];
            for (String str2 : map.keySet()) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Param Map: <");
                sb2.append(str2);
                sb2.append(", ");
                sb2.append((String) map.get(str2));
                sb2.append(">");
                Log.d(str3, sb2.toString());
                int i2 = i * 2;
                strArr[i2] = str2.toLowerCase();
                strArr[i2 + 1] = (String) map.get(str2);
                i++;
            }
        }
        return SetParamsForAudioTrackJni(j, strArr);
    }

    public static native boolean SetParamsForAudioTrackJni(long j, String[] strArr);

    public static boolean SetParamsForEffect(EffectType effectType, long j, Map map) {
        String[] strArr;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("set param for effect, effect type: ");
        sb.append(effectType);
        sb.append(", effect id: ");
        sb.append(j);
        Log.d(str, sb.toString());
        int i = 0;
        if (map == null || map.size() == 0) {
            Log.d(TAG, "Param Map: <null, null>");
            strArr = new String[0];
        } else {
            strArr = new String[(map.size() * 2)];
            for (String str2 : map.keySet()) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Param Map: <");
                sb2.append(str2);
                sb2.append(", ");
                sb2.append((String) map.get(str2));
                sb2.append(">");
                Log.d(str3, sb2.toString());
                int i2 = i * 2;
                strArr[i2] = str2.toLowerCase();
                strArr[i2 + 1] = (String) map.get(str2);
                i++;
            }
        }
        return SetParamsForEffectJni(effectType.ordinal(), j, strArr);
    }

    public static native boolean SetParamsForEffectJni(int i, long j, String[] strArr);
}
