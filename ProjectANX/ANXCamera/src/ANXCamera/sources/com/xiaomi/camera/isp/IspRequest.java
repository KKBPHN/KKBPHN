package com.xiaomi.camera.isp;

import android.os.Parcelable;
import java.util.ArrayList;

public class IspRequest {
    public int[] cropRegion;
    public int flipMode;
    public int functionType;
    public ArrayList inputBuffers;
    public int requestNumber;
    public Parcelable settings;

    public IspRequest(int i, int i2, Parcelable parcelable, ArrayList arrayList, int[] iArr, int i3) {
        this.requestNumber = i;
        this.flipMode = i2;
        this.settings = parcelable;
        this.inputBuffers = arrayList;
        this.cropRegion = iArr;
        this.functionType = i3;
    }
}
