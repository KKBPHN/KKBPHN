package com.android.camera.aiwatermark.handler;

import com.android.camera.aiwatermark.data.AbstractWatermarkData;
import com.android.camera.aiwatermark.data.ScenicSpotsWatermark;
import java.util.ArrayList;
import java.util.HashMap;

public class IndiaScenicSpotsHandler extends ScenicSpotsHandler {
    public IndiaScenicSpotsHandler(boolean z) {
        super(z);
    }

    /* access modifiers changed from: protected */
    public HashMap getRegionMap() {
        AbstractWatermarkData abstractWatermarkData = this.mData;
        if (abstractWatermarkData instanceof ScenicSpotsWatermark) {
            return ((ScenicSpotsWatermark) abstractWatermarkData).getRegionMap(2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public ArrayList getWatermarkList() {
        return this.mData.getForAI();
    }
}
