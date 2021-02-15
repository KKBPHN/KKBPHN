package com.arcsoft.supernight;

import android.graphics.Rect;
import com.arcsoft.supernight.SuperNightProcess.FaceInfo;
import com.arcsoft.supernight.SuperNightProcess.InputInfo;
import com.arcsoft.supernight.SuperNightProcess.RawInfo;

public class SuperNightJni {
    private SuperNightJni a;
    private long b = 0;

    static {
        System.loadLibrary("arcsoft_supernight_jni");
        System.loadLibrary("arcsoft_sn_mtk_apu");
        System.loadLibrary("arcsoft_super_night_raw_mtk");
        System.loadLibrary("arcsoft_super_night_raw_mpbase");
    }

    private native int nativeAddOneInputInfo(long j, RawInfo rawInfo, InputInfo inputInfo);

    private native int nativeCancelSuperNight();

    private native long nativeInit(int i, int i2, int i3, int i4);

    private native int nativePostProcess(long j);

    private native int nativePreProcess(long j);

    private native int nativeProcess(long j, FaceInfo faceInfo, InputInfo inputInfo, int i, Rect rect, ProgressCallback progressCallback);

    private native int nativeProcessEx(long j, FaceInfo faceInfo, byte[] bArr, int i, int i2, int i3, int i4, int i5, Rect rect);

    private native int nativeSetDumpImageFile(boolean z);

    private native int nativeUninit(long j);

    public synchronized int addOneInputInfo(RawInfo rawInfo, InputInfo inputInfo) {
        int nativeAddOneInputInfo;
        TimeConsumingUtil.startTheTimer("SN addOneInputInfo");
        nativeAddOneInputInfo = nativeAddOneInputInfo(this.b, rawInfo, inputInfo);
        TimeConsumingUtil.stopTiming("SN addOneInputInfo");
        return nativeAddOneInputInfo;
    }

    public void cancelSuperNight() {
        nativeCancelSuperNight();
    }

    public void init(int i, int i2, int i3, int i4) {
        String str = "SN init";
        TimeConsumingUtil.startTheTimer(str);
        this.b = nativeInit(i, i2, i3, i4);
        TimeConsumingUtil.stopTiming(str);
    }

    public int postProcess() {
        String str = "SN postProcess";
        TimeConsumingUtil.startTheTimer(str);
        int nativePostProcess = nativePostProcess(this.b);
        TimeConsumingUtil.stopTiming(str);
        return nativePostProcess;
    }

    public int preProcess() {
        String str = "SN preProcess";
        TimeConsumingUtil.startTheTimer(str);
        int nativePreProcess = nativePreProcess(this.b);
        TimeConsumingUtil.stopTiming(str);
        return nativePreProcess;
    }

    public int process(FaceInfo faceInfo, InputInfo inputInfo, int i, Rect rect, ProgressCallback progressCallback) {
        String str = "SN process";
        TimeConsumingUtil.startTheTimer(str);
        int nativeProcess = nativeProcess(this.b, faceInfo, inputInfo, i, rect, progressCallback);
        TimeConsumingUtil.stopTiming(str);
        return nativeProcess;
    }

    public int processEx(FaceInfo faceInfo, byte[] bArr, int i, int i2, int i3, int i4, int i5, Rect rect) {
        return nativeProcessEx(this.b, faceInfo, bArr, i, i2, i3, i4, i5, rect);
    }

    public void setDumpImageFile(boolean z) {
        nativeSetDumpImageFile(z);
    }

    public int unInit() {
        String str = "SN unInit";
        TimeConsumingUtil.startTheTimer(str);
        int nativeUninit = nativeUninit(this.b);
        TimeConsumingUtil.stopTiming(str);
        return nativeUninit;
    }
}
