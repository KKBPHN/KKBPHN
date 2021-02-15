package com.xiaomi.recordmediaprocess;

import android.util.Log;
import java.util.Map;

public class MediaEffectGraph {
    private static String TAG = "MediaEffectGraph";
    private long mGraphLine = 0;

    public MediaEffectGraph() {
        Log.d(TAG, " ");
    }

    private static native long AddAudioTrackJni(String str, boolean z);

    private static native boolean AddEffectJni(long j, long j2);

    private static native void AddSourceAndEffectByTemplateJni(String[] strArr, String str);

    private static native void AddSourcesAndEffectBySourcesPathJni(String[] strArr, float[] fArr);

    private static native boolean AddTransitionEffectJni(long j, long j2, long j3);

    private static native boolean AddVideoBackGroudJni(String str);

    private static native long AddVideoSourceJni(String str);

    private static native long ConstructMediaEffectGraphJni();

    private static native void DestructMediaEffectGraphJni();

    private static native boolean RemoveAudioTrackJni(long j);

    private static native boolean RemoveEffectJni(long j, long j2);

    private static native boolean RemoveTransitionEffectJni(long j, long j2, long j3);

    private static native boolean RemoveVideoBackGroudJni();

    private static native boolean RemoveVideoSourceJni(long j);

    private static native void SetAudioMuteJni(boolean z);

    private static native void SetCurrentSourceJni(long j);

    private static native boolean SetParamsForVideoSourceJni(long j, String[] strArr);

    private static native boolean SwapVideoSourceJni(long j, long j2);

    public long AddAudioTrack(String str, boolean z) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("add video source: ");
        sb.append(str);
        sb.append(" play loop: ");
        sb.append(z);
        Log.d(str2, sb.toString());
        if (this.mGraphLine == 0) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("GraphLine is null, failed add audio source: ");
            sb2.append(str);
            Log.e(str3, sb2.toString());
            return 0;
        }
        long AddAudioTrackJni = AddAudioTrackJni(str, z);
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("add audio source: ");
        sb3.append(AddAudioTrackJni);
        sb3.append("--");
        sb3.append(str);
        Log.d(str4, sb3.toString());
        return AddAudioTrackJni;
    }

    public boolean AddEffect(long j, long j2) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, add effect failed!");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("add effect: ");
        sb.append(j);
        sb.append(", ");
        sb.append(j2);
        Log.d(str, sb.toString());
        return AddEffectJni(j, j2);
    }

    public boolean AddEffect(long j, long j2, long j3) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, add effect failed! ");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("add effect: ");
        sb.append(j);
        String str2 = ", ";
        sb.append(str2);
        sb.append(j2);
        sb.append(str2);
        sb.append(j3);
        Log.d(str, sb.toString());
        return AddTransitionEffectJni(j, j2, j3);
    }

    public void AddSourceAndEffectByTemplate(String[] strArr, String str) {
        AddSourceAndEffectByTemplateJni(strArr, str);
    }

    public void AddSourcesAndEffectBySourcesPath(String[] strArr, float[] fArr) {
        AddSourcesAndEffectBySourcesPathJni(strArr, fArr);
    }

    public boolean AddVideoBackGroud(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("add video backgroud: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        if (this.mGraphLine != 0) {
            return AddVideoBackGroudJni(str);
        }
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("GraphLine is null, failed add video backgroud: ");
        sb2.append(str);
        Log.e(str3, sb2.toString());
        return false;
    }

    public long AddVideoSource(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        String str3 = "add video source: ";
        sb.append(str3);
        sb.append(str);
        Log.d(str2, sb.toString());
        if (this.mGraphLine == 0) {
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("GraphLine is null, failed add video source: ");
            sb2.append(str);
            Log.e(str4, sb2.toString());
            return 0;
        }
        long AddVideoSourceJni = AddVideoSourceJni(str);
        String str5 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str3);
        sb3.append(AddVideoSourceJni);
        sb3.append("--");
        sb3.append(str);
        Log.d(str5, sb3.toString());
        return AddVideoSourceJni;
    }

    public void ConstructMediaEffectGraph() {
        this.mGraphLine = ConstructMediaEffectGraphJni();
        Log.d(TAG, "Construct: ");
    }

    public void DestructMediaEffectGraph() {
        Log.d(TAG, "Destruct");
        this.mGraphLine = 0;
        DestructMediaEffectGraphJni();
    }

    public long GetGraphLine() {
        return this.mGraphLine;
    }

    public boolean RemoveAudioTrack(long j) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null , failed remove video source!");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("remove video source: ");
        sb.append(j);
        Log.d(str, sb.toString());
        return RemoveAudioTrackJni(j);
    }

    public boolean RemoveEffect(long j, long j2) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, remove effect failed!");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("remove effect: ");
        sb.append(j);
        sb.append(", ");
        sb.append(j2);
        Log.d(str, sb.toString());
        return RemoveEffectJni(j, j2);
    }

    public boolean RemoveEffect(long j, long j2, long j3) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, remove effect failed!");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("remove effect: ");
        sb.append(j);
        String str2 = ", ";
        sb.append(str2);
        sb.append(j2);
        sb.append(str2);
        sb.append(j3);
        Log.d(str, sb.toString());
        return RemoveTransitionEffectJni(j, j2, j3);
    }

    public boolean RemoveVideoBackGroud() {
        Log.d(TAG, "remove video backgroud");
        if (this.mGraphLine != 0) {
            return RemoveVideoBackGroudJni();
        }
        Log.e(TAG, "GraphLine is null , failed remove video backgroud!");
        return false;
    }

    public boolean RemoveVideoSource(long j) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null , failed remove video source!");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("remove video source: ");
        sb.append(j);
        Log.d(str, sb.toString());
        return RemoveVideoSourceJni(j);
    }

    public void SetAudioMute(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("SetAudioMute: ");
        sb.append(z);
        Log.d(str, sb.toString());
        SetAudioMuteJni(z);
    }

    public void SetCurrentSource(long j) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("set current source id: ");
        sb.append(j);
        Log.d(str, sb.toString());
        SetCurrentSourceJni(j);
    }

    public boolean SetParamsForVideoSource(long j, Map map) {
        String[] strArr;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("set param for source, source id: ");
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
        return SetParamsForVideoSourceJni(j, strArr);
    }

    public boolean SwapVideoSource(long j, long j2) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null , failed Swap video source!");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Swap video source: ");
        sb.append(j);
        sb.append(", ");
        sb.append(j2);
        Log.d(str, sb.toString());
        return SwapVideoSourceJni(j, j2);
    }
}
