package com.android.camera.aiwatermark.handler;

import android.text.TextUtils;
import com.android.camera.aiwatermark.algo.ChinaDateConvertor;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class ChinaFestivalHandler extends FestivalHandler {
    private static final String TAG = "ChinaFestivalHandler";

    public ChinaFestivalHandler(boolean z) {
        super(z);
    }

    private WatermarkItem findFestivalWatermark(String str) {
        ArrayList forAI = this.mData.getForAI();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("list.size() = ");
        sb.append(forAI.size());
        Log.d(str2, sb.toString());
        Iterator it = forAI.iterator();
        while (it.hasNext()) {
            WatermarkItem watermarkItem = (WatermarkItem) it.next();
            if (TextUtils.equals(str, watermarkItem.getKey())) {
                return watermarkItem;
            }
        }
        return null;
    }

    private String getChinaDate() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChinaDateConvertor.today());
        sb.append(WatermarkConstant.SUFFIX);
        return sb.toString();
    }

    private String getDate() {
        return ChinaDateConvertor.getDate();
    }

    /* access modifiers changed from: protected */
    public WatermarkItem findWatermark() {
        String chinaDate = ChinaDateConvertor.isChineseEve() ? WatermarkConstant.CHINESE_EVE : getChinaDate();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ChineseDate = ");
        sb.append(chinaDate);
        Log.d(str, sb.toString());
        WatermarkItem findFestivalWatermark = findFestivalWatermark(chinaDate);
        if (findFestivalWatermark != null) {
            return findFestivalWatermark;
        }
        WatermarkItem findFestivalWatermark2 = findFestivalWatermark(getDate());
        if (findFestivalWatermark2 != null) {
            return findFestivalWatermark2;
        }
        Log.d(TAG, "Festival watermark null");
        return null;
    }
}
