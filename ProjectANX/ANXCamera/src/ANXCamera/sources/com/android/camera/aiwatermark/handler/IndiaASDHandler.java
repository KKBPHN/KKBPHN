package com.android.camera.aiwatermark.handler;

import android.content.Context;
import com.android.camera.aiwatermark.data.WatermarkItem;
import java.util.ArrayList;

public class IndiaASDHandler extends ASDHandler {
    public IndiaASDHandler(boolean z, Context context) {
        super(z, context);
    }

    /* access modifiers changed from: protected */
    public WatermarkItem findWatermark() {
        return super.findWatermark();
    }

    /* access modifiers changed from: protected */
    public ArrayList getASDWatermarkList() {
        return this.mData.getForAI();
    }
}
