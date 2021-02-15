package com.android.camera.aiwatermark.handler;

import com.android.camera.aiwatermark.data.WatermarkItem;
import java.util.ArrayList;
import java.util.Calendar;

public class TimeHandler extends AbstractHandler {
    protected ArrayList mWatermarkItems = new ArrayList();

    public TimeHandler(boolean z) {
        super(z);
    }

    /* access modifiers changed from: protected */
    public WatermarkItem findWatermark() {
        if (this.mWatermarkItems.isEmpty()) {
            this.mWatermarkItems = this.mData.getForAI();
        }
        return (WatermarkItem) this.mWatermarkItems.get(0);
    }

    public int getDayOfWeek() {
        return Calendar.getInstance().get(7) - 1;
    }
}
