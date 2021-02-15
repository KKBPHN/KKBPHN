package com.miui.filtersdk.beauty;

import java.util.HashMap;
import java.util.Map;

public abstract class IntelligentBeautyProcessor extends BeautyProcessor {
    private float mExtraSpan = 0.0f;
    protected float[][] mLevelParameters = {new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f}, new float[]{0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f}, new float[]{0.3f, 0.3f, 0.3f, 0.3f, 0.3f, 0.3f}, new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f}, new float[]{0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f}};

    public abstract void clearBeautyParameters();

    public float getExtraSpan() {
        return this.mExtraSpan;
    }

    public final Map getIntelligentLevelParams(int i) {
        HashMap hashMap = new HashMap();
        BeautyParameterType[] supportedBeautyParamTypes = getSupportedBeautyParamTypes();
        for (int i2 = 0; i2 < supportedBeautyParamTypes.length; i2++) {
            hashMap.put(supportedBeautyParamTypes[i2], Float.valueOf(this.mLevelParameters[i][i2]));
        }
        return hashMap;
    }

    public void setExtraSpan(float f) {
        this.mExtraSpan = f;
    }
}
